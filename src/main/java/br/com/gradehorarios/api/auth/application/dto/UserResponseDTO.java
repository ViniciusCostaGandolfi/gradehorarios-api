package br.com.gradehorarios.api.auth.application.dto;

import br.com.gradehorarios.api.auth.domain.model.RoleName;
import br.com.gradehorarios.api.auth.domain.model.User;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object containing user details")
public record UserResponseDTO(
    @Schema(description = "User's unique ID", example = "1") Long id, 
    @Schema(description = "User's full name", example = "John Doe") String name, 
    @Schema(description = "User's email address", example = "john.doe@example.com") String email, 
    @Schema(description = "User's role in the system", example = "ROLE_USER") RoleName role) {

    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}