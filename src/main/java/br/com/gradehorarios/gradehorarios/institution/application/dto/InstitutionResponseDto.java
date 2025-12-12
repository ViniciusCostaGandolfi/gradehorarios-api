package br.com.gradehorarios.gradehorarios.institution.application.dto;

import java.util.List;

import br.com.gradehorarios.gradehorarios.institution.domain.model.Institution;
import br.com.gradehorarios.gradehorarios.shared.domain.service.FileStorageService;
import br.com.gradehorarios.gradehorarios.timetable.application.dto.SolutionDto;

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
