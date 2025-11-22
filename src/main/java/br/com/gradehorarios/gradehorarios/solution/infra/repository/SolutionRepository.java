package br.com.gradehorarios.gradehorarios.solution.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gradehorarios.gradehorarios.solution.domain.entity.Solution;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    
}
