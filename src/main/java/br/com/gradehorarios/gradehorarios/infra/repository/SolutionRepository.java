package br.com.gradehorarios.gradehorarios.infra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gradehorarios.gradehorarios.domain.entity.Solution;
import br.com.gradehorarios.gradehorarios.domain.entity.SolverStatus;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    Long countByUserIdAndSolverStatusIn(Long userId, List<SolverStatus> statuses);
}
