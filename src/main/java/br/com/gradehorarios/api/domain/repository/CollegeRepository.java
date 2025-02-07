package br.com.gradehorarios.api.domain.repository;

import br.com.gradehorarios.api.domain.entity.college.College;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository extends JpaRepository<College, Integer> {
    List<College> findAllByUserId(Integer userId);

    Optional<College> findByIdAndUserId(Integer id, Integer collegeId);
}
