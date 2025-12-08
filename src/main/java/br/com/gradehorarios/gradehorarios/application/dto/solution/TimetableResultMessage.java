package br.com.gradehorarios.gradehorarios.application.dto.solution;


import br.com.gradehorarios.gradehorarios.domain.entity.SolverStatus;

public record TimetableResultMessage(
    Long solutionId,
    String inputPath,
    Long durationMilis,
    SolverStatus solverStatus,
    String errorMessage,
    String warningMessage,
    String solverType,
    String modelName,
    Long institutionId,
    Long userId,
    SolverResponseDto solution
) {}