package br.com.gradehorarios.api.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gradehorarios.api.auth.domain.model.UserInstitutionRole;

public interface UserInstitutionRoleRepository extends JpaRepository<UserInstitutionRole, Long> {
    
}
