package br.com.gradehorarios.api.application.dto;

import java.util.List;

public record SolutionClassroomWeekDay(
    ClassroomDto classroom,
    List<TimetableClassroomWeekDay> grade
) {}
