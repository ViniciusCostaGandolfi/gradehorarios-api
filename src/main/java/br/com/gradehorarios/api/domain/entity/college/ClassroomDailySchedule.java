package br.com.gradehorarios.api.domain.entity.college;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "classroom_daily_schedules")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomDailySchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer mondayClasses;

    private Integer tuesdayClasses;

    private Integer wednesdayClasses;

    private Integer thursdayClasses;

    private Integer fridayClasses;

    private Integer saturdayClasses;

    private Integer sundayClasses;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroomId")
    private Classroom classroom;

}
