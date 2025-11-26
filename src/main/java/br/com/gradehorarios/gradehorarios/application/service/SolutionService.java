package br.com.gradehorarios.gradehorarios.application.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.gradehorarios.gradehorarios.domain.entity.Institution;
import br.com.gradehorarios.gradehorarios.domain.entity.Solution;
import br.com.gradehorarios.gradehorarios.domain.entity.SolverStatus;
import br.com.gradehorarios.gradehorarios.infra.repository.InstitutionRepository;
import br.com.gradehorarios.gradehorarios.infra.repository.SolutionRepository;
import br.com.gradehorarios.gradehorarios.shared.service.storage.StorageService;


@Service
@AllArgsConstructor
public class SolutionService {
    
    private final StorageService storageService;
    private final InstitutionRepository institutionRepository;
    private final SolutionRepository solutionRepository;

    public Solution createSolution(MultipartFile file, Long institutionId) {
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(() -> new RuntimeException("Instituição não encontrada"));
        String uniqueFileName = FileNameGenerator.generateFileName(file, institution.getName(), true);

        
        
        String inputPath = storageService.uploadFile(file, uniqueFileName);
        
        Solution solution = new Solution();
        solution.setInputPath(inputPath);
        solution.setInstitution(institution);
        solution.setSolverStatus(SolverStatus.PENDING);
        
        return solutionRepository.save(solution);
    }
    
}