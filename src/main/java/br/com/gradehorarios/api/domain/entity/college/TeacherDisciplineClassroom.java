package br.com.gradehorarios.api.domain.entity.college;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "teacher_disciplines")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDisciplineClassroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teacherId")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "classroomId")
    private Classroom classroom;

    
    @ManyToOne
    @JoinColumn(name = "disciplineId")
    private Discipline discipline;
    
    @Column
    private Integer totalClasses;
}