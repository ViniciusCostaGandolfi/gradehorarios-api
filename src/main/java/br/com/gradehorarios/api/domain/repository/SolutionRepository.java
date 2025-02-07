package br.com.gradehorarios.api.domain.repository;

import br.com.gradehorarios.api.domain.entity.college.Solution;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionRepository extends JpaRepository<Solution, Integer> {

    List<Solution> findAllByCollegeIdOrderByCreatedAtDesc(Integer collegeId);

    Optional<Solution> findByIdAndCollegeId(Integer id, Integer collegeId);
}
