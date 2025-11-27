package br.com.gradehorarios.gradehorarios.application.dto;

public record TimetableRequestMessage(
    Long solutionId,
    String inputPath,
    Long institutionId,
    Long userId
) {}