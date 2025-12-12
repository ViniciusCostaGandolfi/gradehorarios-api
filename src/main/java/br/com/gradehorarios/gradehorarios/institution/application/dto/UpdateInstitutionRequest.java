package br.com.gradehorarios.gradehorarios.institution.application.dto;

public record UpdateInstitutionRequest(
    String name,
    String code,
    Boolean active
) {}
