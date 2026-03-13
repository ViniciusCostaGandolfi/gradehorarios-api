package br.com.gradehorarios.api.institution.application.dto;

import java.util.List;

import br.com.gradehorarios.api.institution.domain.model.Institution;
import br.com.gradehorarios.api.shared.domain.service.FileStorageService;
import br.com.gradehorarios.api.timetable.application.dto.SolutionDto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object containing institution details")
public record InstitutionResponseDto(
    @Schema(description = "Institution's unique ID", example = "1") Long id,
    @Schema(description = "Institution name", example = "Universidade Federal") String name,
    @Schema(description = "Institution short code", example = "UF") String code,
    @Schema(description = "Is institution active", example = "true") boolean active,
    @Schema(description = "List of solutions for this institution") List<SolutionDto> solutions

) {
    public InstitutionResponseDto(Institution institution) {
        this(
            institution.getId(), institution.getName(), institution.getCode(), institution.isActive(),
            institution.getSolutions() != null ? institution.getSolutions().stream().map(sol -> new SolutionDto(sol)).toList() : null
        );
    }

    public InstitutionResponseDto(Institution institution, FileStorageService storageService) {
        this(
            institution.getId(), institution.getName(), institution.getCode(), institution.isActive(),
            institution.getSolutions() != null ? institution.getSolutions().stream().map(sol -> new SolutionDto(sol, storageService)).toList() : null
        );
    }
}
