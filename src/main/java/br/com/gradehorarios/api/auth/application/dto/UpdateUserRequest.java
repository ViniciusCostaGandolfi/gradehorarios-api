package br.com.gradehorarios.api.auth.application.dto;

import br.com.gradehorarios.api.auth.domain.model.RoleName;

public record UpdateUserRequest(String name, RoleName role) {}