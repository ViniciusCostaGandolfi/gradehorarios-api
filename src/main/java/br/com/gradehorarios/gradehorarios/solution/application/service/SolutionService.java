package br.com.gradehorarios.gradehorarios.solution.application.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.gradehorarios.gradehorarios.solution.domain.entity.Solution;
import br.com.gradehorarios.gradehorarios.solution.domain.entity.SolverStatus;
import br.com.gradehorarios.gradehorarios.solution.infra.repository.SolutionRepository;
import br.com.gradehorarios.gradehorarios.auth.domain.entity.Institution;
import br.com.gradehorarios.gradehorarios.auth.domain.entity.User;
import br.com.gradehorarios.gradehorarios.auth.infra.repository.InstitutionRepository;
import br.com.gradehorarios.gradehorarios.auth.infra.repository.UserRepository;
import br.com.gradehorarios.gradehorarios.shared.service.storage.StorageService;


@Service
@AllArgsConstructor
public class SolutionService {
    
    private final StorageService storageService;
    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final SolutionRepository solutionRepository;

    public Solution createSolution(MultipartFile file, Long userId, Long institutionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(() -> new RuntimeException("Instituição não encontrada"));
        String uniqueFileName = FileNameGenerator.generateFileName(file, institution.getName(), true);
        
        String inputPath = storageService.uploadFile(file, uniqueFileName);
        
        Solution solution = new Solution();
        solution.setInputPath(inputPath);
        solution.setUser(user);
        solution.setInstitution(institution);
        solution.setSolverStatus(SolverStatus.PENDING);
        
        return solutionRepository.save(solution);
    }
    
}