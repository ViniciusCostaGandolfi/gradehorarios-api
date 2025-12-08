package br.com.gradehorarios.gradehorarios.application.dto.auth;

import br.com.gradehorarios.gradehorarios.domain.entity.RoleName;
import br.com.gradehorarios.gradehorarios.domain.entity.User;

public record UserResponseDTO(Long id, String name, String email, RoleName role) {

    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }
}