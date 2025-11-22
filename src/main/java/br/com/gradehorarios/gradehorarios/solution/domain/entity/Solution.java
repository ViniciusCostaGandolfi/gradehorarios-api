package br.com.gradehorarios.gradehorarios.solution.domain.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;

import br.com.gradehorarios.gradehorarios.auth.domain.entity.Institution;
import br.com.gradehorarios.gradehorarios.auth.domain.entity.User;
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
@Table(name = "solutions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String inputPath;

    private String outputPath;

    private Long durationMillis;

    @Enumerated(EnumType.STRING)
    private SolverStatus solverStatus;

    private String errorMessage;

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
