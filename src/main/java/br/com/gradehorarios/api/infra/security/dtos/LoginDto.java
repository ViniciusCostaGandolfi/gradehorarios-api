package br.com.gradehorarios.api.infra.security.dtos;

public record LoginDto (
    String email,
    String password
) {}
