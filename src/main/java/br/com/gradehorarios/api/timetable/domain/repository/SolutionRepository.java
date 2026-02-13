package br.com.gradehorarios.api.timetable.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.gradehorarios.api.timetable.domain.model.Solution;
import br.com.gradehorarios.api.timetable.domain.model.SolverStatus;

@Repository
public interface SolutionRepository extends JpaRepository<Solution, Long> {
    Long countByUserIdAndSolverStatusIn(Long userId, List<SolverStatus> statuses);

    Optional<Solution> findByIdAndInstitutionId(Long solutionId, Long institutionId);
}
