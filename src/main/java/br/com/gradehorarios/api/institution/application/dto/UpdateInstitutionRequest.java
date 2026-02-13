package br.com.gradehorarios.api.institution.application.dto;

public record UpdateInstitutionRequest(
    String name,
    String code,
    Boolean active
) {}
