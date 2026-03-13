package br.com.gradehorarios.api.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for registering a new user")
public record RegisterRequest(
    @Schema(description = "User's full name", example = "John Doe")
    @NotBlank(message = "Nome é obrigatório")
    String name, 

    @Schema(description = "User's email address", example = "john.doe@example.com")
    @Email(message = "Email inválido")
    String email, 

    @Schema(description = "User's password", example = "securePassword123")
    @NotBlank(message = "Senha é obrigatória")
    String password
) {}