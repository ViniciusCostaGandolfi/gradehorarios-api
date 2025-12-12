package br.com.gradehorarios.gradehorarios.timetable.application.service;

import br.com.gradehorarios.gradehorarios.auth.domain.model.User;
import br.com.gradehorarios.gradehorarios.auth.domain.repository.UserRepository;
import br.com.gradehorarios.gradehorarios.institution.domain.model.Institution;
import br.com.gradehorarios.gradehorarios.institution.domain.repository.InstitutionRepository;
import br.com.gradehorarios.gradehorarios.shared.domain.service.FileStorageService;
import br.com.gradehorarios.gradehorarios.shared.infra.utils.StringUtils;
import br.com.gradehorarios.gradehorarios.timetable.domain.model.Solution;
import br.com.gradehorarios.gradehorarios.timetable.domain.model.SolverStatus;
import br.com.gradehorarios.gradehorarios.timetable.domain.policy.SolutionCreationPolicy;
import br.com.gradehorarios.gradehorarios.timetable.domain.repository.SolutionRepository;
import br.com.gradehorarios.gradehorarios.timetable.infra.dto.SolverResponseDto;
import br.com.gradehorarios.gradehorarios.timetable.infra.dto.TimetableRequestMessage;
import br.com.gradehorarios.gradehorarios.timetable.infra.service.AmqpScheduleProducerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.time.Instant;

@Service
public class SolutionService {

    
    @Autowired
    private FileStorageService storageService;
    
    @Autowired
    private AmqpScheduleProducerService messagingService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private InstitutionRepository institutionRepository;
    
    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private SolutionCreationPolicy solutionCreationPolicy;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${ms-solver.url}")
    private String msSolverUrl;

    @Transactional
    public Solution createSolution(MultipartFile file, Long userId, Long institutionId) throws IOException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instituição não encontrada"));

        this.solutionCreationPolicy.checkQuota(user.getId());

        String extension = StringUtils.extractExtension(file.getOriginalFilename());
        String uniqueFileName = StringUtils.generateRandomName();
        String objectName = String.format(
            "instituicao_%d/solucao/%s/%s/%s",
            institutionId,
            uniqueFileName,
            "_input",
            extension);

        String inputPath = storageService.uploadFile(file, objectName);

        Solution solution = new Solution();
        solution.setInstitution(institution);
        solution.setUser(user);
        solution.setInputPath(inputPath);
        solution.setModelName(file.getOriginalFilename());
        solution.setSolverStatus(SolverStatus.PENDING);
        solution.setCreatedAt(Instant.now());

        solution = solutionRepository.save(solution);

        TimetableRequestMessage message = new TimetableRequestMessage(
            solution.getId(),
            this.storageService.getPublicUrl(inputPath),
            institutionId,
            userId
        );

        messagingService.sendScheduleRequest(message);

        return solution;
    }

    @Transactional
    public void deleteSolution(Long solutionId) {
        Solution solution = this.solutionRepository.findById(solutionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solução não encontrada"));
        
        this.storageService.deleteFile(solution.getInputPath());
        if (solution.getOutputPath() != null) {
            this.storageService.deleteFile(solution.getOutputPath());
        }
        this.solutionRepository.delete(solution);
    }

    public Solution retrySolution(Long solutionId) {
        Solution solution = this.solutionRepository.findById(solutionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solução não encontrada"));
        
        this.storageService.deleteFile(solution.getOutputPath());
        solution.setSolverStatus(SolverStatus.PENDING);
        solution.setDurationMillis(null);
        solution.setErrorMessage(null);
        solution.setWarningMessage(null);
        solution.setOutputPath(null);

        solution = solutionRepository.save(solution);

        TimetableRequestMessage message = new TimetableRequestMessage(
            solution.getId(),
            this.storageService.getPublicUrl(solution.getInputPath()),
            solution.getInstitution().getId(),
            solution.getUser().getId()
        );

        messagingService.sendScheduleRequest(message);

        return solution;
    }

    public Solution getSolutionByInstitutionIdAndSolutionId(Long institutionId, Long solutionId) {

        var solution = this.solutionRepository.findByIdAndInstitutionId(solutionId, institutionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solução não encontrada para a instituição"));
        return solution;
    }

    public SolverResponseDto runSolverExternally(Solution solution) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            TimetableRequestMessage body = new TimetableRequestMessage(
                solution.getId(),
                this.storageService.getPublicUrl(solution.getInputPath()),
                solution.getInstitution().getId(),
                solution.getUser().getId()
            );

            HttpEntity<TimetableRequestMessage> requestEntity = new HttpEntity<>(body, headers);
            String url = msSolverUrl + "/api/v1/solutions";
            return restTemplate.postForObject(url, requestEntity, SolverResponseDto.class);

        } catch (HttpClientErrorException e) {
            String errorDetail = extractErrorDetail(e.getResponseBodyAsString());
            throw new ResponseStatusException(e.getStatusCode(), errorDetail);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro comunicação solver: " + e.getMessage());
        }
    }

    private String extractErrorDetail(String responseBody) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            if (root.has("detail")) return root.get("detail").asText();
        } catch (Exception e) {}
        return responseBody;
    }

}