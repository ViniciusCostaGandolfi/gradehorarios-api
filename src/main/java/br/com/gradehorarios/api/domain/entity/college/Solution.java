package br.com.gradehorarios.api.domain.entity.college;

import java.time.Duration;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import br.com.gradehorarios.api.application.dto.SolutionDto;
import br.com.gradehorarios.api.application.dto.SolutionInputDto;
import br.com.gradehorarios.api.application.dto.SolutionOutputDto;
import io.hypersistence.utils.hibernate.type.json.JsonType;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "solutions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collegeId", nullable = false)
    private College college;

    @Enumerated(EnumType.STRING)
    private SolverStatus status;

    @CreationTimestamp
    private Instant createdAt;

    @Column
    private Duration timeToSolve;

    @Type(JsonType.class)
    @Column(name = "input", columnDefinition = "jsonb")
    private String input;

    @Type(JsonType.class)
    @Column(name = "output", columnDefinition = "jsonb")
    private String output;


    public SolutionDto toSolutionDto() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new ParameterNamesModule());
        try {
            SolutionInputDto inputDto = objectMapper.readValue(this.input, SolutionInputDto.class);
            SolutionOutputDto outputDto = objectMapper.readValue(this.output, SolutionOutputDto.class);

            return new SolutionDto(
                this.id,
                inputDto,
                outputDto,
                this.status,
                this.timeToSolve,
                this.createdAt
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter Solution para SolutionDto", e);
        }
    }
}
