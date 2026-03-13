package br.com.gradehorarios.api.auth.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request object for authenticating using an external OAuth provider")
public record OAuthLoginRequestDto(
    @Schema(description = "Name of the OAuth provider (e.g., google, facebook)", example = "google") String provider,
    @Schema(description = "OAuth token received from the provider") String token
) {}
