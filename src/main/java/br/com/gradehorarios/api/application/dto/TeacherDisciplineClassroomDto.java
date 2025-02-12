package br.com.gradehorarios.api.application.dto;

import br.com.gradehorarios.api.domain.entity.college.TeacherClassroomDiscipline;

public record TeacherDisciplineClassroomDto(
    Integer id,
    Integer teacherId,
    Integer classroomId,
    Integer disciplineId,
    Integer totalClasses
) {

    public TeacherDisciplineClassroomDto(TeacherClassroomDiscipline teacherDisciplineClassroom) {
        this(
            teacherDisciplineClassroom.getId(),
            teacherDisciplineClassroom.getTeacher() != null ? teacherDisciplineClassroom.getTeacher().getId() : null,
            teacherDisciplineClassroom.getClassroom() != null ? teacherDisciplineClassroom.getClassroom().getId() : null,
            teacherDisciplineClassroom.getDiscipline() != null ? teacherDisciplineClassroom.getDiscipline().getId() : null,
            teacherDisciplineClassroom.getTotalClasses()
        );
    }
}