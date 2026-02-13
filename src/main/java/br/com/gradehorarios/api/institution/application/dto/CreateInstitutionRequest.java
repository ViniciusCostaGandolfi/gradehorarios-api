package br.com.gradehorarios.api.institution.application.dto;

public record CreateInstitutionRequest(
    String name,
    String code
) {}