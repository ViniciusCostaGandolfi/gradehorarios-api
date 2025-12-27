package br.com.gradehorarios.gradehorarios.timetable.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gradehorarios.gradehorarios.auth.domain.model.User;
import br.com.gradehorarios.gradehorarios.institution.domain.model.Institution;
import br.com.gradehorarios.gradehorarios.shared.domain.service.EmailService;
import br.com.gradehorarios.gradehorarios.shared.domain.service.FileStorageService;
import br.com.gradehorarios.gradehorarios.timetable.domain.model.Solution;
import br.com.gradehorarios.gradehorarios.timetable.domain.model.SolverStatus;

@ExtendWith(MockitoExtension.class)
public class SolutionNotificationServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private FileStorageService storageService;

    @InjectMocks
    private SolutionNotificationService solutionNotificationService;

    @Captor
    private ArgumentCaptor<Map<String, Object>> variablesCaptor;

    private Solution solution;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {

        solutionNotificationService = new SolutionNotificationService(
            emailService, 
            storageService, 
            "http://teste-url.com"
        );

        User user = new User();
        user.setEmail("professor@teste.com");
        user.setName("Professor Teste");

        Institution institution = new Institution();
        institution.setId(1L);
        institution.setName("Escola Modelo");

        solution = new Solution();
        solution.setId(100L);
        solution.setUser(user);
        solution.setInstitution(institution);
        solution.setDurationMillis(5000L);
    }

    @Test
    @DisplayName("Deve notificar com sucesso injetando a URL corretamente")
    void testNotifySolutionProcessed() {
        // Arrange
        solution.setSolverStatus(SolverStatus.OPTIMAL);
        solution.setTeacherOutputPath("teacher.pdf");
        
        when(storageService.getPublicUrl("teacher.pdf")).thenReturn("http://s3/teacher.pdf");

        // Act
        solutionNotificationService.notifySolutionProcessed(solution);

        // Assert
        verify(emailService).sendEmail(
            eq("professor@teste.com"),
            anyString(),
            eq("email/solution-processed"), 
            variablesCaptor.capture()
        );

        Map<String, Object> vars = variablesCaptor.getValue();
        
        assertEquals("http://teste-url.com/admin/instituicoes/1/solucoes/100", vars.get("solutionUrl"));
        assertEquals("SUCESSO", vars.get("statusText"));
        assertEquals("http://s3/teacher.pdf", vars.get("teacherUrl"));
    }
}