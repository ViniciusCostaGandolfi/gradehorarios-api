package br.com.gradehorarios.api.domain.repository;

import br.com.gradehorarios.api.domain.entity.college.Discipline;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplineRepository extends JpaRepository<Discipline, Integer> {

    List<Discipline> findAllByCollegeId(Integer collegeId);

    Optional<Discipline> findByIdAndCollegeId(Integer id, Integer collegeId);
}
