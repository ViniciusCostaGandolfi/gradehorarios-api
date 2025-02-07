package br.com.gradehorarios.api.domain.repository;

import br.com.gradehorarios.api.domain.entity.college.Classroom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom, Integer> {

    List<Classroom> findAllByCollegeId(Integer collegeId);
}
