package br.com.gradehorarios.api.domain.entity.college;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "colleges")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class College {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer code;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discipline> disciplines = new ArrayList<>();

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Teacher> teachers = new ArrayList<>();

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Classroom> classrooms = new ArrayList<>();

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Solution> solutions = new ArrayList<>();

    public void addDiscipline(Discipline discipline) {
        disciplines.add(discipline);
        discipline.setCollege(this);
    }

    public void removeDiscipline(Discipline discipline) {
        disciplines.remove(discipline);
        discipline.setCollege(null);
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
        teacher.setCollege(this);
    }

    public void removeTeacher(Teacher teacher) {
        teachers.remove(teacher);
        teacher.setCollege(null);
    }

    public void addClassroom(Classroom classroom) {
        classrooms.add(classroom);
        classroom.setCollege(this);
    }

    public void removeClassroom(Classroom classroom) {
        classrooms.remove(classroom);
        classroom.setCollege(null);
    }

    public void addSolution(Solution solution) {
        solutions.add(solution);
        solution.setCollege(this);
    }

    public void removeSolution(Solution solution) {
        solutions.remove(solution);
        solution.setCollege(null);
    }
}
