package br.com.gradehorarios.gradehorarios.application.dto;

import java.util.List;

import br.com.gradehorarios.gradehorarios.domain.entity.Institution;
import br.com.gradehorarios.gradehorarios.shared.service.storage.StorageService;

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
            institution.getSolutions().stream().map(sol -> new SolutionDto(sol)).toList()
        );
    }

    public InstitutionResponseDto(Institution institution, StorageService storageService) {
        this(
            institution.getId(), institution.getName(), institution.getCode(), institution.isActive(),
            institution.getSolutions().stream().map(sol -> new SolutionDto(sol, storageService)).toList()
        );
    }
}
