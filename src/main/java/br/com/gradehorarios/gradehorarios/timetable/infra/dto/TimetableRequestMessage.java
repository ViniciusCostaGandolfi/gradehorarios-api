package br.com.gradehorarios.gradehorarios.timetable.infra.dto;

public record TimetableRequestMessage(
    Long solutionId,
    String inputPath,
    Long institutionId,
    Long userId
) {}