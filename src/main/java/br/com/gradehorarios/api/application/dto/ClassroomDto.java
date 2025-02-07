package br.com.gradehorarios.api.application.dto;

import br.com.gradehorarios.api.domain.entity.college.Classroom;

public record ClassroomDto(
    Integer id,
    String name,
    Integer collegeId
) {

    public ClassroomDto(Classroom classroom) {
        this(
            classroom.getId(),
            classroom.getName(),
            classroom.getCollege() != null ? classroom.getCollege().getId() : null
        );
    }
}
