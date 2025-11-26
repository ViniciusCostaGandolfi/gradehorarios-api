package br.com.gradehorarios.gradehorarios.application.dto;

import java.time.Duration;

import br.com.gradehorarios.gradehorarios.domain.entity.SolverStatus;

public record TimetableResultMessage(
    Long id,
    String inputPath,
    String outputPath,
    Duration duration,
    Boolean solved,
    SolverStatus status,
    Boolean running,
    String errorMessage,
    String warningMessage,
    String solverType,
    String modelName,
    Long institutionId,
    Long userId
) {}