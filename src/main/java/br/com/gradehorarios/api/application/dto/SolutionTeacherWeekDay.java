package br.com.gradehorarios.api.application.dto;

import java.util.List;

public record SolutionTeacherWeekDay(
    TeacherDto teacher,
    List<TimetableTeacherWeekDay> grade
) {
}
