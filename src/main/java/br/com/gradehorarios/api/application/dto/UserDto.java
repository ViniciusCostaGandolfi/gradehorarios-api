package br.com.gradehorarios.api.application.dto;

import java.util.List;

import br.com.gradehorarios.api.domain.entity.college.User;

public record UserDto(
    Integer id,
    String name,
    String email,
    String phone,
    List<CollegeDto> colleges
) {

    public UserDto(User user) {
        this(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getColleges() != null
                ? user.getColleges().stream().map(CollegeDto::new).toList()
                : null
        );
    }
}
