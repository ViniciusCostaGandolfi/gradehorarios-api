package br.com.gradehorarios.gradehorarios.application.dto;


import java.time.Duration;
import java.time.Instant;

import br.com.gradehorarios.gradehorarios.domain.entity.Solution;
import br.com.gradehorarios.gradehorarios.domain.entity.SolverStatus;

public record SolutionDto(
    Long id,
    Instant createdAt,
    String inputPath,
    String outputPath,
    SolverStatus solverStatus,
    Duration duration,
    String errorMessage,
    String warningMessage,
    String modelName,
    Long institutionId
) {

    public SolutionDto(Solution solution) {
        this(solution.getId(),
             solution.getCreatedAt(),
             solution.getInputPath(),
             solution.getOutputPath(),
             solution.getSolverStatus(),
             solution.getDurationMillis() != null ? Duration.ofMillis(solution.getDurationMillis()) : null,
             solution.getErrorMessage(),
             solution.getWarningMessage(),
             solution.getModelName(),
             solution.getInstitution().getId());
    }

}
