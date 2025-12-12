package br.com.gradehorarios.gradehorarios.institution.application.dto;

public record CreateInstitutionRequest(
    String name,
    String code
) {}