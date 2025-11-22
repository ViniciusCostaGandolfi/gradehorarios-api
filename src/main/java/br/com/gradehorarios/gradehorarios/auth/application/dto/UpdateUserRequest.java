package br.com.gradehorarios.gradehorarios.auth.application.dto;

import br.com.gradehorarios.gradehorarios.auth.domain.entity.RoleName;

public record UpdateUserRequest(String name, RoleName role) {}