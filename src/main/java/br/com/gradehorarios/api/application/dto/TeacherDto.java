package br.com.gradehorarios.api.application.dto;

import br.com.gradehorarios.api.domain.entity.college.Preference;
import br.com.gradehorarios.api.domain.entity.college.Teacher;


public record TeacherDto(
    Integer id,
    String name,
    Preference preferDoubleClass,
    Preference preferFirstClass,
    Preference preferLastClass,
    Integer collegeId
) {

    public TeacherDto(Teacher teacher) {
        this(
            teacher.getId(),
            teacher.getName(),
            teacher.getPreferDoubleClass(),
            teacher.getPreferFirstClass(),
            teacher.getPreferLastClass(),
            teacher.getCollege() != null ? teacher.getCollege().getId() : null
        );
    }
}
