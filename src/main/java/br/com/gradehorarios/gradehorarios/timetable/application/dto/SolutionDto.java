package br.com.gradehorarios.gradehorarios.timetable.application.dto;


import java.time.Instant;

import br.com.gradehorarios.gradehorarios.shared.domain.service.FileStorageService;
import br.com.gradehorarios.gradehorarios.timetable.domain.model.Solution;
import br.com.gradehorarios.gradehorarios.timetable.domain.model.SolverStatus;

public record SolutionDto(
    Long id,
    Instant createdAt,
    String inputPath,
    String outputPath,
    String classroomOutputPath,
    String teacherOutputPath,
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
            solution.getOutputPath(),
            solution.getClassroomOutputPath(),
            solution.getTeacherOutputPath(),
            solution.getSolverStatus(),
            solution.getDurationMillis(),
            solution.getErrorMessage(),
            solution.getWarningMessage(),
            solution.getModelName(),
            solution.getInstitution().getId()
        );
    }

    public SolutionDto(Solution solution, FileStorageService storageService) {
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
