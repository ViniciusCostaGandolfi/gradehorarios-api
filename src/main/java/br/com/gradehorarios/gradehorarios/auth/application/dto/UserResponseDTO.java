package br.com.gradehorarios.gradehorarios.auth.application.dto;

import br.com.gradehorarios.gradehorarios.auth.domain.model.RoleName;
import br.com.gradehorarios.gradehorarios.auth.domain.model.User;

public record UserResponseDTO(Long id, String name, String email, RoleName role) {

    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}