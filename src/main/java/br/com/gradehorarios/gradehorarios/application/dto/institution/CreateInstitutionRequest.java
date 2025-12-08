package br.com.gradehorarios.gradehorarios.application.dto.institution;

public record CreateInstitutionRequest(
    String name,
    String code
) {}