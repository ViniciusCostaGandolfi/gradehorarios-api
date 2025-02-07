package br.com.gradehorarios.api.domain.repository;

import br.com.gradehorarios.api.domain.entity.college.Teacher;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    List<Teacher> findAllByCollegeId(Integer collegeId);
}
