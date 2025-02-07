package br.com.gradehorarios.api.application.dto;

import br.com.gradehorarios.api.domain.entity.college.Discipline;

public record DisciplineDto(
    Integer id,
    String name,
    String code,
    Integer collegeId
) {
    public DisciplineDto(Discipline discipline) {
        this(
            discipline.getId(),
            discipline.getName(),
            discipline.getCode(),
            discipline.getCollege().getId()
        );
    }
}

