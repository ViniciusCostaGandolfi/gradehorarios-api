package br.com.gradehorarios.gradehorarios.auth.application.dto;

public record CreateInstitutionRequest(
    String name,
    String code
) {}