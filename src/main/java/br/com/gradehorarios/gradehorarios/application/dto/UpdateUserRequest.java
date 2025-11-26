package br.com.gradehorarios.gradehorarios.application.dto;

import br.com.gradehorarios.gradehorarios.domain.entity.RoleName;

public record UpdateUserRequest(String name, RoleName role) {}