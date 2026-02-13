package br.com.gradehorarios.api.timetable.domain.model;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;

import br.com.gradehorarios.api.auth.domain.model.User;
import br.com.gradehorarios.api.institution.domain.model.Institution;
import jakarta.persistence.Column;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "solutions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"institution"})
@EqualsAndHashCode(of = "id")
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1024)
    private String inputPath;

    @Column(length = 1024)
    private String teacherOutputPath;

    @Column(length = 1024)
    private String classroomOutputPath;

    @Column(length = 1024)
    private String outputPath;

    private Long durationMillis;

    @Enumerated(EnumType.STRING)
    private SolverStatus solverStatus;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @Column(columnDefinition = "TEXT")
    private String warningMessage;

    private String modelName;

    @CreatedDate
    private Instant createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institutionId", nullable = false)
    private Institution institution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
}
