package br.com.gradehorarios.gradehorarios.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gradehorarios.gradehorarios.domain.entity.Solution;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    
}
