package br.com.gradehorarios.api.application.dto;

public record UserCreationDto(
    Integer id,
    String name,
    String email,
    String password,
    String phone
) {

}
