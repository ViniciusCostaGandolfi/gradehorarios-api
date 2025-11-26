package br.com.gradehorarios.gradehorarios.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
    @NotBlank(message = "Nome é obrigatório")
    String name, 
    @Email(message = "Email inválido")
    String email, 
    @NotBlank(message = "Senha é obrigatória")
    String password
) {}