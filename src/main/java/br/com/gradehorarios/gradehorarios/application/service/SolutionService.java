package br.com.gradehorarios.gradehorarios.application.service;

import br.com.gradehorarios.gradehorarios.application.dto.SolverResponseDto;
import br.com.gradehorarios.gradehorarios.application.dto.TimetableRequestMessage;
import br.com.gradehorarios.gradehorarios.domain.entity.Institution;
import br.com.gradehorarios.gradehorarios.domain.entity.Solution;
import br.com.gradehorarios.gradehorarios.domain.entity.SolverStatus;
import br.com.gradehorarios.gradehorarios.domain.entity.User;
import br.com.gradehorarios.gradehorarios.infra.repository.InstitutionRepository;
import br.com.gradehorarios.gradehorarios.infra.repository.SolutionRepository;
import br.com.gradehorarios.gradehorarios.infra.repository.UserRepository;
import br.com.gradehorarios.gradehorarios.shared.service.storage.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

@Service
public class SolutionService {

    @Autowired
    private StorageService storageService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private SolutionRepository solutionRepository;
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

        String uuid = UUID.randomUUID().toString();
        String uniqueFileName = String.format("%s_input.xlsx", uuid);
        String objectName = String.format("instituicao_%d/solucao/%s", institutionId, uniqueFileName);

        String inputPath = storageService.uploadFile(file, objectName);

        Solution solution = new Solution();
        solution.setInstitution(institution);
        solution.setUser(user);
        solution.setInputPath(inputPath);
        solution.setModelName(file.getOriginalFilename());
        solution.setSolverStatus(SolverStatus.PENDING);
        solution.setCreatedAt(Instant.now());

        solution = solutionRepository.save(solution);

        try {
            SolverResponseDto responseDto = this.runSolverExternally(solution);

            byte[] jsonBytes = objectMapper.writeValueAsBytes(responseDto);
            
            String outputFileName = String.format("%s_output.json", uuid);
            String outputObjectName = String.format("instituicao_%d/solucao/%s", institutionId, outputFileName);
            
            MultipartFile jsonMultipartFile = new JsonMultipartFile(jsonBytes, outputFileName);

            String outputPath = storageService.uploadFile(jsonMultipartFile, outputObjectName);

            solution.setOutputPath(outputPath);
            solution.setSolverStatus(SolverStatus.OPTIMAL);
            
            return solutionRepository.save(solution);

        } catch (Exception e) {
            e.printStackTrace();
            solution.setSolverStatus(SolverStatus.ERROR);
            solutionRepository.save(solution);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar solução: " + e.getMessage());
        }
    }

    public void validateFileExternally(MultipartFile file) throws IOException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileResource);
            
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            String url = msSolverUrl + "/api/v1/validate";
            restTemplate.postForEntity(url, requestEntity, Void.class);

        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString());
        }
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


    private static class JsonMultipartFile implements MultipartFile {
        private final byte[] content;
        private final String name;

        public JsonMultipartFile(byte[] content, String name) {
            this.content = content;
            this.name = name;
        }

        @Override @NonNull public String getName() { return "file"; }
        @Override public String getOriginalFilename() { return name; }
        @Override public String getContentType() { return "application/json"; }
        @Override public boolean isEmpty() { return content == null || content.length == 0; }
        @Override public long getSize() { return content.length; }
        @Override @NonNull public byte[] getBytes() throws IOException { return content; }
        @Override @NonNull public InputStream getInputStream() throws IOException { return new ByteArrayInputStream(content); }
        @Override public void transferTo(@NonNull File dest) throws IOException, IllegalStateException { 
            try (FileOutputStream fos = new FileOutputStream(dest)) { fos.write(content); }
        }
    }
}