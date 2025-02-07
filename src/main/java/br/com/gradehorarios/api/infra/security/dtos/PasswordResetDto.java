package br.com.gradehorarios.api.infra.security.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetDto(
    @NotBlank String token,
    @NotBlank 
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") 
    String newPassword
) {}
