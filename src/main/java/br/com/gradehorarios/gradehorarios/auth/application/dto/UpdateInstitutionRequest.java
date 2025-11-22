package br.com.gradehorarios.gradehorarios.auth.application.dto;

public record UpdateInstitutionRequest(
    String name,
    String code,
    Boolean active
) {}
