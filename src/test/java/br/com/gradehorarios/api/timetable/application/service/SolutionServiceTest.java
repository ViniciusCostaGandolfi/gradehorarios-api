package br.com.gradehorarios.api.timetable.application.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import br.com.gradehorarios.api.auth.domain.model.User;
import br.com.gradehorarios.api.auth.domain.repository.UserRepository;
import br.com.gradehorarios.api.institution.domain.model.Institution;
import br.com.gradehorarios.api.institution.domain.repository.InstitutionRepository;
import br.com.gradehorarios.api.shared.domain.service.FileStorageService;
import br.com.gradehorarios.api.timetable.domain.model.Solution;
import br.com.gradehorarios.api.timetable.domain.policy.SolutionCreationPolicy;
import br.com.gradehorarios.api.timetable.domain.repository.SolutionRepository;
import br.com.gradehorarios.api.timetable.infra.dto.TimetableRequestMessage;
import br.com.gradehorarios.api.timetable.infra.service.AmqpScheduleProducerService;

@ExtendWith(MockitoExtension.class)
class SolutionServiceTest {

    @Mock
    private FileStorageService storageService;

    @Mock
    private AmqpScheduleProducerService messagingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InstitutionRepository institutionRepository;

    @Mock
    private SolutionRepository solutionRepository;

    @Mock
    private SolutionCreationPolicy solutionCreationPolicy;

    @InjectMocks
    private SolutionService solutionService;

    private User sampleUser;
    private Institution sampleInstitution;
    private Solution sampleSolution;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(solutionService, "msSolverUrl", "http://localhost:8000");

        sampleUser = new User();
        sampleUser.setId(1L);

        sampleInstitution = new Institution();
        sampleInstitution.setId(10L);

        sampleSolution = new Solution();
        sampleSolution.setId(100L);
        sampleSolution.setUser(sampleUser);
        sampleSolution.setInstitution(sampleInstitution);
        sampleSolution.setInputPath("path/to/input.xlsx");
        sampleSolution.setOutputPath("path/to/output.xlsx");
    }

    @Test
    void createSolution_Success() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test-model.xlsx");

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(institutionRepository.findById(10L)).thenReturn(Optional.of(sampleInstitution));
        when(storageService.uploadFile(any(), any())).thenReturn("some-input-path");
        when(solutionRepository.save(any(Solution.class))).thenReturn(sampleSolution);
        when(storageService.getPublicUrl(any())).thenReturn("http://s3.amazonaws.com/some-input-path");

        Solution result = solutionService.createSolution(file, 1L, 10L);

        assertNotNull(result);
        verify(solutionCreationPolicy).checkQuota(1L);
        verify(storageService).uploadFile(any(), any());
        verify(solutionRepository).save(any(Solution.class));
        verify(messagingService).sendScheduleRequest(any(TimetableRequestMessage.class));
    }

    @Test
    void createSolution_UserNotFound_ThrowsException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> solutionService.createSolution(file, 1L, 10L));
    }

    @Test
    void createSolution_InstitutionNotFound_ThrowsException() throws IOException {
        MultipartFile file = mock(MultipartFile.class);

        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(institutionRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> solutionService.createSolution(file, 1L, 10L));
    }

    @Test
    void deleteSolution_Success() {
        when(solutionRepository.findById(100L)).thenReturn(Optional.of(sampleSolution));

        assertDoesNotThrow(() -> solutionService.deleteSolution(100L));

        verify(storageService).deleteFile("path/to/input.xlsx");
        verify(storageService).deleteFile("path/to/output.xlsx");
        verify(solutionRepository).delete(sampleSolution);
    }

    @Test
    void deleteSolution_NotFound_ThrowsException() {
        when(solutionRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> solutionService.deleteSolution(100L));
    }

    @Test
    void retrySolution_Success() {
        when(solutionRepository.findById(100L)).thenReturn(Optional.of(sampleSolution));
        when(solutionRepository.save(any(Solution.class))).thenReturn(sampleSolution);
        when(storageService.getPublicUrl(any())).thenReturn("http://s3.amazonaws.com/path/to/input.xlsx");

        Solution result = solutionService.retrySolution(100L);

        assertNotNull(result);
        verify(storageService).deleteFile("path/to/output.xlsx");
        verify(solutionRepository).save(sampleSolution);
        verify(messagingService).sendScheduleRequest(any(TimetableRequestMessage.class));
    }

    @Test
    void getSolutionByInstitutionIdAndSolutionId_Success() {
        when(solutionRepository.findByIdAndInstitutionId(100L, 10L)).thenReturn(Optional.of(sampleSolution));

        Solution result = solutionService.getSolutionByInstitutionIdAndSolutionId(10L, 100L);

        assertNotNull(result);
        assertEquals(100L, result.getId());
    }

    @Test
    void getSolutionByInstitutionIdAndSolutionId_NotFound_ThrowsException() {
        when(solutionRepository.findByIdAndInstitutionId(100L, 10L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> solutionService.getSolutionByInstitutionIdAndSolutionId(10L, 100L));
    }
}
