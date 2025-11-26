package br.com.gradehorarios.gradehorarios.application.dto;

public record UpdateInstitutionRequest(
    String name,
    String code,
    Boolean active
) {}
