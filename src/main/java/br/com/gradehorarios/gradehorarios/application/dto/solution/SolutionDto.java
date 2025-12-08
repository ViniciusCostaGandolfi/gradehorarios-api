package br.com.gradehorarios.gradehorarios.application.dto.solution;


import java.time.Instant;

import br.com.gradehorarios.gradehorarios.application.service.storage.IStorageService;
import br.com.gradehorarios.gradehorarios.domain.entity.Solution;
import br.com.gradehorarios.gradehorarios.domain.entity.SolverStatus;

public record SolutionDto(
    Long id,
    Instant createdAt,
    String inputPath,
    String classroomOutputPath,
    String teacherOutputPath,
    String outputPath,
    SolverStatus solverStatus,
    Long durationMillis,
    String errorMessage,
    String warningMessage,
    String modelName,
    Long institutionId
) {

    public SolutionDto(Solution solution) {
        this(
            solution.getId(),
            solution.getCreatedAt(),
            solution.getInputPath(),
            solution.getClassroomOutputPath(),
            solution.getTeacherOutputPath(),
            solution.getOutputPath(),
            solution.getSolverStatus(),
            solution.getDurationMillis(),
            solution.getErrorMessage(),
            solution.getWarningMessage(),
            solution.getModelName(),
            solution.getInstitution().getId()
        );
    }

    public SolutionDto(Solution solution, IStorageService storageService) {
        this(
            solution.getId(),
            solution.getCreatedAt(),
            storageService.getPublicUrl(solution.getInputPath()),
            storageService.getPublicUrl(solution.getOutputPath()),
            storageService.getPublicUrl(solution.getClassroomOutputPath()),
            storageService.getPublicUrl(solution.getTeacherOutputPath()),
            solution.getSolverStatus(), 
            solution.getDurationMillis(),
            solution.getErrorMessage(),
            solution.getWarningMessage(),
            solution.getModelName(),
            solution.getInstitution().getId()
        );
    }

}
