package br.com.gradehorarios.gradehorarios.application.dto.solution;


import java.util.Optional;

import br.com.gradehorarios.gradehorarios.domain.entity.SolverStatus;

public record TimetableResultMessage(
    Long solutionId,
    String inputPath,
    Long durationMillis,
    SolverStatus solverStatus,
    String errorMessage,
    String warningMessage,
    String solverType,
    String modelName,
    Long institutionId,
    Long userId,
    Optional<SolverResponseDto> solution
) {}