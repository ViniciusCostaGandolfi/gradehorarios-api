package br.com.gradehorarios.gradehorarios.application.dto.institution;

import java.util.List;

import br.com.gradehorarios.gradehorarios.application.dto.solution.SolutionDto;
import br.com.gradehorarios.gradehorarios.application.service.storage.IStorageService;
import br.com.gradehorarios.gradehorarios.domain.entity.Institution;

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

    public InstitutionResponseDto(Institution institution, IStorageService storageService) {
        this(
            institution.getId(), institution.getName(), institution.getCode(), institution.isActive(),
            institution.getSolutions() != null ? institution.getSolutions().stream().map(sol -> new SolutionDto(sol, storageService)).toList() : null
        );
    }
}
