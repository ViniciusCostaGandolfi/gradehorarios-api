package br.com.gradehorarios.gradehorarios.timetable.domain.policy;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.gradehorarios.gradehorarios.timetable.domain.exception.SolutionLimitExceededException;
import br.com.gradehorarios.gradehorarios.timetable.domain.model.SolverStatus;
import br.com.gradehorarios.gradehorarios.timetable.domain.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SolutionCreationPolicyImpl implements SolutionCreationPolicy {
    

    private final SolutionRepository solutionRepository;
    private static final int MAX_SOLUTIONS = 2;
    private static final List<SolverStatus> PROCESSING_STATUSES = Arrays.asList(SolverStatus.PENDING, SolverStatus.RUNNING);

    @Override
    public void checkQuota(Long userId) {
        Long count = solutionRepository.countByUserIdAndSolverStatusIn(userId, PROCESSING_STATUSES);

        if (count >= MAX_SOLUTIONS) {
            throw new SolutionLimitExceededException(
                "Você já possui o limite máximo de " + MAX_SOLUTIONS + " soluções em processamento."
            );
        }
    }
}
