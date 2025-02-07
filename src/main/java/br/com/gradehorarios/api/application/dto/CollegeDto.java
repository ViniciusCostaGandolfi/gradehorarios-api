package br.com.gradehorarios.api.application.dto;

import br.com.gradehorarios.api.domain.entity.college.College;

public record CollegeDto(
    Integer id,
    Integer code,
    String name) {

    public CollegeDto(College college) {
        this(
            college.getId(),
            college.getCode(),
            college.getName()
            );
    }
}
