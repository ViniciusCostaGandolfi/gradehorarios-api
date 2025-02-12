package br.com.gradehorarios.api.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gradehorarios.api.domain.entity.college.TeacherClassroomDiscipline;

public interface TeacherClassroomDisciplineRepository extends JpaRepository<TeacherClassroomDiscipline, Integer> {

    void deleteAllByDisciplineId(Integer disciplineId);

    List<TeacherClassroomDiscipline> findAllByTeacherId(Integer teacherId);
}
