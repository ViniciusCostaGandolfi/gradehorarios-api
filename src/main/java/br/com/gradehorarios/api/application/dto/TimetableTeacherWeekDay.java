package br.com.gradehorarios.api.application.dto;

import java.util.Optional;

public record TimetableTeacherWeekDay(
    int classIndex,
    Optional<ClassroomDto> mondayClassroom,
    Optional<ClassroomDto> tuesdayClassroom,
    Optional<ClassroomDto> wednesdayClassroom,
    Optional<ClassroomDto> thursdayClassroom,
    Optional<ClassroomDto> fridayClassroom,
    Optional<ClassroomDto> saturdayClassroom,
    Optional<ClassroomDto> sundayClassroom,
    int teacherId
) {
}