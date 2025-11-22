package br.com.gradehorarios.gradehorarios.auth.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gradehorarios.gradehorarios.auth.domain.entity.UserInstitutionRole;

public interface UserInstitutionRoleRepository extends JpaRepository<UserInstitutionRole, Long> {
    
}
