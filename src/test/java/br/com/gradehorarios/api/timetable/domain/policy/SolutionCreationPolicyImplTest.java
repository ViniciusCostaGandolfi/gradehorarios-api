package br.com.gradehorarios.api.timetable.domain.policy;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gradehorarios.api.timetable.domain.exception.SolutionLimitExceededException;
import br.com.gradehorarios.api.timetable.domain.repository.SolutionRepository;

@ExtendWith(MockitoExtension.class)
class SolutionCreationPolicyImplTest {

    @Mock
    private SolutionRepository solutionRepository;

    @InjectMocks
    private SolutionCreationPolicyImpl policy;

    @Test
    void checkQuota_UnderLimit_DoesNotThrowException() {
        when(solutionRepository.countByUserIdAndSolverStatusIn(eq(1L), anyList())).thenReturn(0L);

        assertDoesNotThrow(() -> policy.checkQuota(1L));
    }

    @Test
    void checkQuota_ExactlyAtLimit_ThrowsSolutionLimitExceededException() {
        when(solutionRepository.countByUserIdAndSolverStatusIn(eq(1L), anyList())).thenReturn(2L);

        assertThrows(SolutionLimitExceededException.class, () -> policy.checkQuota(1L));
    }

    @Test
    void checkQuota_OverLimit_ThrowsSolutionLimitExceededException() {
        when(solutionRepository.countByUserIdAndSolverStatusIn(eq(1L), anyList())).thenReturn(5L);

        assertThrows(SolutionLimitExceededException.class, () -> policy.checkQuota(1L));
    }
}
