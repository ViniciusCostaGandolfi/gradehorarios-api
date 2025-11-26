package br.com.gradehorarios.gradehorarios.application.dto;

public record CreateInstitutionRequest(
    String name,
    String code
) {}