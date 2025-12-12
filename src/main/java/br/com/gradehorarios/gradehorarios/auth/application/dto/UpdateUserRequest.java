package br.com.gradehorarios.gradehorarios.auth.application.dto;

import br.com.gradehorarios.gradehorarios.auth.domain.model.RoleName;

public record UpdateUserRequest(String name, RoleName role) {}