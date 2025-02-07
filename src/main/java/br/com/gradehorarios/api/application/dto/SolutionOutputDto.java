package br.com.gradehorarios.api.application.dto;

import java.util.List;


public record SolutionOutputDto(
    List<SolutionClassroomWeekDay> solutionClassrooms,
    List<SolutionTeacherWeekDay> solutionTeachers
) {
}
