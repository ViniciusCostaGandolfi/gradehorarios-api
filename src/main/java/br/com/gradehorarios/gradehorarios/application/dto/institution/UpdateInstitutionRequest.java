package br.com.gradehorarios.gradehorarios.application.dto.institution;

public record UpdateInstitutionRequest(
    String name,
    String code,
    Boolean active
) {}
