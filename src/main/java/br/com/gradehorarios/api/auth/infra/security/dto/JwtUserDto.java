package br.com.gradehorarios.api.auth.infra.security.dto;

import java.util.List;

import br.com.gradehorarios.api.auth.domain.model.RoleName;
import br.com.gradehorarios.api.auth.domain.model.User;

public record JwtUserDto(
    Long id,
    String email,
    RoleName role,
    List<InstitutionInfo> institutions
) {

    public record InstitutionInfo(
        Long institutionId,
        String institutionName,
        String role
    ) {}

    public JwtUserDto(User user) {
        this(
            user.getId(),
            user.getEmail(),
            user.getRole(),
            user.getInstitutionRoles() == null ? List.of() : user.getInstitutionRoles().stream()
                .map(ir -> new InstitutionInfo(
                    ir.getInstitution().getId(),
                    ir.getInstitution().getName(),
                    ir.getRole().name()
                ))
                .toList()
        );
    }
}