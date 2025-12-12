package br.com.gradehorarios.gradehorarios.institution.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.gradehorarios.gradehorarios.institution.domain.model.Institution;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    @Query("SELECT uir.institution FROM UserInstitutionRole uir WHERE uir.user.id = :userId")
    List<Institution> findByUserId(Long userId);
    
}
