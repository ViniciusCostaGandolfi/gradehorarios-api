package br.com.gradehorarios.api.domain.entity.college;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teacher_availabilities")
public class TeacherAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(value = EnumType.STRING)
    private Preference monday;

    @Enumerated(value = EnumType.STRING)
    private Preference tuesday;

    @Enumerated(value = EnumType.STRING)
    private Preference wednesday;

    @Enumerated(value = EnumType.STRING)
    private Preference thursday;

    @Enumerated(value = EnumType.STRING)
    private Preference friday;

    @Enumerated(value = EnumType.STRING)
    private Preference saturday;

    @Enumerated(value = EnumType.STRING)
    private Preference sunday;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacherId")
    private Teacher teacher;

}