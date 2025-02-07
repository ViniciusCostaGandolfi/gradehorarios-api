package br.com.gradehorarios.api.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.gradehorarios.api.application.dto.SolutionDto;
import br.com.gradehorarios.api.application.dto.SolutionInputDto;
import br.com.gradehorarios.api.domain.entity.college.College;
import br.com.gradehorarios.api.domain.entity.college.Solution;
import br.com.gradehorarios.api.domain.repository.CollegeRepository;
import br.com.gradehorarios.api.domain.repository.SolutionRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;


@Service
public class TimetableSolverService {



    @Value("${api.gradehorarios.ms.timetable.url}")
    private String timetableServiceUrl;

    @Autowired
    private CollegeRepository collegeRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private ObjectMapper objectMapper;


    public SolutionDto resolve(Integer collegeId) {
        College college = this.collegeRepository.findById(collegeId)
            .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada"));

        SolutionInputDto solutionInputDto = new SolutionInputDto(college);

        ResponseEntity<SolutionDto> response = this.restTemplate.postForEntity(
            this.timetableServiceUrl + "/timetable",
            solutionInputDto,
            SolutionDto.class
        );

        SolutionDto solutionDto = response.getBody();


        if (solutionDto != null) {
            Solution solution = new Solution();
            solution.setCollege(college);
            solution.setStatus(solutionDto.status());
            solution.setTimeToSolve(solutionDto.timeToSolve());
            try {
                solution.setInput(objectMapper.writeValueAsString(solutionDto.input()));
                solution.setOutput(objectMapper.writeValueAsString(solutionDto.output()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao converter JSON", e);
            }

            solution = solutionRepository.save(solution);

            solutionDto = solution.toSolutionDto();

        }

        return solutionDto;
    }

    @Transactional
    public void deleteById(Integer solutionId, Integer collegeId, Integer userId) {
        College college = collegeRepository.findByIdAndUserId(collegeId, userId)
            .orElseThrow(() -> new EntityNotFoundException("Recurso não encontrado"));
        Solution solution = solutionRepository.findByIdAndCollegeId(solutionId, college.getId())
                .orElseThrow(() -> new EntityNotFoundException("Grade não encontrada para a escola especificada."));
        solutionRepository.delete(solution);
    }
}
