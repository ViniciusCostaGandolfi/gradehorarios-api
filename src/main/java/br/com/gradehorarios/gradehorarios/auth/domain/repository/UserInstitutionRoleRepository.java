package br.com.gradehorarios.gradehorarios.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gradehorarios.gradehorarios.auth.domain.model.UserInstitutionRole;

public interface UserInstitutionRoleRepository extends JpaRepository<UserInstitutionRole, Long> {
    
}
