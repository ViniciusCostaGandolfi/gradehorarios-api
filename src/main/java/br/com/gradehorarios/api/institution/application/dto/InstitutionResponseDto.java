package br.com.gradehorarios.api.institution.application.dto;

import java.util.List;

import br.com.gradehorarios.api.institution.domain.model.Institution;
import br.com.gradehorarios.api.shared.domain.service.FileStorageService;
import br.com.gradehorarios.api.timetable.application.dto.SolutionDto;

public record InstitutionResponseDto(
    Long id,
    String name,
    String code,
    boolean active,
    List<SolutionDto> solutions

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
