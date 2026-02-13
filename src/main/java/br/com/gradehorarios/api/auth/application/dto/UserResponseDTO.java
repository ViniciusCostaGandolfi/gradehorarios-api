package br.com.gradehorarios.api.auth.application.dto;

import br.com.gradehorarios.api.auth.domain.model.RoleName;
import br.com.gradehorarios.api.auth.domain.model.User;

public record UserResponseDTO(Long id, String name, String email, RoleName role) {

    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}