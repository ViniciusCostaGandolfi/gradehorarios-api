package br.com.gradehorarios.api.application.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.gradehorarios.api.application.dto.SolutionDto;
import br.com.gradehorarios.api.application.dto.SolutionInputDto;
import br.com.gradehorarios.api.application.dto.SolutionOutputDto;
import br.com.gradehorarios.api.domain.entity.college.Solution;

@Component
public class SolutionMapper {

    @Autowired
    private ObjectMapper objectMapper;


    public SolutionDto convertSolutionEntityToDto(Solution solution) {
        try {
            SolutionInputDto inputDto = objectMapper.readValue(solution.getInput(), SolutionInputDto.class);
            SolutionOutputDto outputDto = objectMapper.readValue(solution.getOutput(), SolutionOutputDto.class);
        
            return new SolutionDto(
                solution.getId(),
                inputDto,
                outputDto,
                solution.getStatus(),
                solution.getTimeToSolve(),
                solution.getCreatedAt()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter JSON para DTO", e);
        }
    }
}
