package br.com.gradehorarios.gradehorarios.application.dto.auth;

import br.com.gradehorarios.gradehorarios.domain.entity.RoleName;

public record UpdateUserRequest(String name, RoleName role) {}