package br.com.gradehorarios.api.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for user login")
public record LoginRequest(
    
    @Schema(description = "User's email address", example = "user@example.com")
    @Email(message = "Email inválido")
    String email, 

    @Schema(description = "User's password", example = "secret123")
    @NotBlank(message = "Senha é obrigatória")
    String password) {}