package br.com.gradehorarios.api.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.gradehorarios.api.domain.entity.college.Classroom;

public record FullClassroomDto(
    Integer id,
    String name,
    Integer collegeId,
    ClassroomDailyScheduleDto classroomDailySchedule,
    List<TeacherDisciplineClassroomDto> teacherDisciplineClassrooms
) {

    public FullClassroomDto(Classroom classroom) {
        this(
            classroom.getId(),
            classroom.getName(),
            classroom.getCollege() != null ? classroom.getCollege().getId() : null,
            classroom.getClassroomDailySchedule() != null
                ? new ClassroomDailyScheduleDto(classroom.getClassroomDailySchedule())
                : null,
            classroom.getTeacherDisciplineClassrooms() != null ? classroom.getTeacherDisciplineClassrooms().stream()
                .map(TeacherDisciplineClassroomDto::new)
                .collect(Collectors.toList()) : null
        );
    }
}
