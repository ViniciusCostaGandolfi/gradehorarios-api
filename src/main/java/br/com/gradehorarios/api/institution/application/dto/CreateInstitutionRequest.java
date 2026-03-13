package br.com.gradehorarios.api.institution.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for creating a new institution")
public record CreateInstitutionRequest(
    @Schema(description = "Institution name", example = "Universidade Estadual") String name,
    @Schema(description = "Institution short code", example = "UE") String code
) {}