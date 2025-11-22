package br.com.gradehorarios.gradehorarios.bootstrap.security;

import org.springframework.stereotype.Component;


import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.gradehorarios.gradehorarios.auth.domain.entity.RoleName;
import br.com.gradehorarios.gradehorarios.bootstrap.dto.JwtUserDto;

@Component("securityGuard")
public class SecurityGuard {

    public boolean check(Long institutionId, String... allowedInstitutionRoles) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof JwtUserDto)) {
            return false;
        }
        JwtUserDto user = (JwtUserDto) principal;

        if (user.globalRole() == RoleName.ROLE_ADMIN) {
            return true;
        }

        List<String> allowedRolesList = Arrays.asList(allowedInstitutionRoles);

        return user.institutions().stream()
            .anyMatch(instInfo ->
                instInfo.institutionId().equals(institutionId) && 
                allowedRolesList.contains(instInfo.role())
            );
    }
}