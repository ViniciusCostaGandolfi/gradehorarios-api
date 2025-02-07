package br.com.gradehorarios.api.application.dto;
import java.util.Optional;

public record TimetableClassroomWeekDay(
    int classIndex,
    Optional<TeacherDto> mondayTeacher,
    Optional<TeacherDto> tuesdayTeacher,
    Optional<TeacherDto> wednesdayTeacher,
    Optional<TeacherDto> thursdayTeacher,
    Optional<TeacherDto> fridayTeacher,
    Optional<TeacherDto> saturdayTeacher,
    Optional<TeacherDto> sundayTeacher,
    int classroomId
) {}

