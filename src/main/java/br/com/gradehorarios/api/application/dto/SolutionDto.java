package br.com.gradehorarios.api.application.dto;


import java.time.Duration;
import java.time.Instant;

import br.com.gradehorarios.api.domain.entity.college.SolverStatus;

public record SolutionDto(
    Integer id,
    SolutionInputDto input,
    SolutionOutputDto output,
    SolverStatus status,
    Duration timeToSolve,
    Instant createdAt
    ) { 
}
