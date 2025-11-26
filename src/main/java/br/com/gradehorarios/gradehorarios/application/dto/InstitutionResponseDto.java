package br.com.gradehorarios.gradehorarios.application.dto;

import br.com.gradehorarios.gradehorarios.domain.entity.Institution;

public record InstitutionResponseDto(
    Long id,
    String name,
    String code,
    boolean active
) {
    public InstitutionResponseDto(Institution institution) {
        this(institution.getId(), institution.getName(), institution.getCode(), institution.isActive());
    }
}
