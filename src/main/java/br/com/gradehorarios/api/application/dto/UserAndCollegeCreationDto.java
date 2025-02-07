package br.com.gradehorarios.api.application.dto;

public record UserAndCollegeCreationDto(
    UserCreationDto user,
    CollegeDto college
) {
    
}
