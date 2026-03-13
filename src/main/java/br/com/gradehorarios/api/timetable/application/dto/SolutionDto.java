package br.com.gradehorarios.api.timetable.application.dto;


import java.time.Instant;

import br.com.gradehorarios.api.shared.domain.service.FileStorageService;
import br.com.gradehorarios.api.timetable.domain.model.Solution;
import br.com.gradehorarios.api.timetable.domain.model.SolverStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object containing solution details")
public record SolutionDto(
    @Schema(description = "Solution's unique ID", example = "10") Long id,
    @Schema(description = "Creation timestamp") Instant createdAt,
    @Schema(description = "Path/URL to the input file") String inputPath,
    @Schema(description = "Path/URL to the output file") String outputPath,
    @Schema(description = "Path/URL to the classroom timetable output file") String classroomOutputPath,
    @Schema(description = "Path/URL to the teacher timetable output file") String teacherOutputPath,
    @Schema(description = "Current solver status", example = "PENDING") SolverStatus solverStatus,
    @Schema(description = "Duration of the generation in milliseconds", example = "15000") Long durationMillis,
    @Schema(description = "Error message if failed") String errorMessage,
    @Schema(description = "Warning message if any") String warningMessage,
    @Schema(description = "Name of the uploaded model file") String modelName,
    @Schema(description = "ID of the institution", example = "1") Long institutionId
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
