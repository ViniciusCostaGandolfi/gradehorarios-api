package br.com.gradehorarios.api.timetable.infra.dto;


import java.util.Optional;

import br.com.gradehorarios.api.timetable.domain.model.SolverStatus;

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