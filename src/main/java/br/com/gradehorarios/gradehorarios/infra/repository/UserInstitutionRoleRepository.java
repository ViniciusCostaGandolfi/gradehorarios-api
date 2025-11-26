package br.com.gradehorarios.gradehorarios.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gradehorarios.gradehorarios.domain.entity.UserInstitutionRole;

public interface UserInstitutionRoleRepository extends JpaRepository<UserInstitutionRole, Long> {
    
}
