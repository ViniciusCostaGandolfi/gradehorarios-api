package br.com.gradehorarios.api.timetable.infra.dto;

public record TimetableRequestMessage(
    Long solutionId,
    String inputPath,
    Long institutionId,
    Long userId
) {}