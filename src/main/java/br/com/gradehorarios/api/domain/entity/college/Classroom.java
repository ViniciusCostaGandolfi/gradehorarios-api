package br.com.gradehorarios.api.domain.entity.college;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "classroom")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;


    @OneToOne(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private ClassroomDailySchedule classroomDailySchedule;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeacherClassroomDiscipline> teacherDisciplineClassrooms = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "collegeId")
    private College college;

    public void addTeacherDisciplineClassroom(TeacherClassroomDiscipline disciplineClassroom) {
        disciplineClassroom.setClassroom(this);
        this.teacherDisciplineClassrooms.add(disciplineClassroom);
    }

    public void removeTeacherDisciplineClassroom(TeacherClassroomDiscipline disciplineClassroom) {
        disciplineClassroom.setClassroom(null);
        this.teacherDisciplineClassrooms.remove(disciplineClassroom);
    }

}
