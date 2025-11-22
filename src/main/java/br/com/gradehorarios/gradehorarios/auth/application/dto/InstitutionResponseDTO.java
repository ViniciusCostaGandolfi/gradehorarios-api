package br.com.gradehorarios.gradehorarios.auth.application.dto;

public record InstitutionResponseDTO(
    Long id,
    String name,
    String code,
    boolean active
) {}
