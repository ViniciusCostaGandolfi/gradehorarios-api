package br.com.gradehorarios.api.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.gradehorarios.api.domain.entity.college.Teacher;
import br.com.gradehorarios.api.domain.entity.college.TeacherDisciplineClassroom;

public interface TeacherDisciplineClassroomRepository extends JpaRepository<TeacherDisciplineClassroom, Integer> {

    void deleteAllByTeacher(Teacher teacher);
}
