package br.com.gradehorarios.api.domain.entity.college;

import jakarta.persistence.CascadeType;
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
@Table(name = "teachers_classrooms_disciplines")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherClassroomDiscipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teacherId", nullable = true)
    private Teacher teacher;

    @ManyToOne(cascade = CascadeType.REMOVE)    
    @JoinColumn(name = "classroomId")
    private Classroom classroom;

    
    @ManyToOne(cascade = CascadeType.REMOVE)    
    @JoinColumn(name = "disciplineId")
    private Discipline discipline;
    
    @Column
    private Integer totalClasses;
}