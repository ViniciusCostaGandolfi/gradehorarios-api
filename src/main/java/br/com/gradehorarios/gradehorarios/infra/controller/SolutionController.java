package br.com.gradehorarios.gradehorarios.infra.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.gradehorarios.gradehorarios.application.dto.solution.SolutionDto;
import br.com.gradehorarios.gradehorarios.application.service.solution.SolutionService;
import br.com.gradehorarios.gradehorarios.application.service.storage.StorageService;
import br.com.gradehorarios.gradehorarios.bootstrap.security.dto.JwtUserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/institutions/{institutionId}/solutions")
@RequiredArgsConstructor
public class SolutionController {

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private StorageService storageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SolutionDto> createSolution(
            @PathVariable Long institutionId,
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal JwtUserDto user
    ) throws Exception {
        var solution = solutionService.createSolution(file, user.id(), institutionId);
        
        return ResponseEntity.ok(new SolutionDto(solution));
    }

    @GetMapping("/{solutionId}")
    public SolutionDto getSolutionById(@PathVariable Long solutionId, @PathVariable Long institutionId) {
        return new SolutionDto(
            this.solutionService.getSolutionByInstitutionIdAndSolutionId(institutionId, solutionId),
            storageService
        ) ;
    }
    

    @PutMapping("/{solutionId}")
    public ResponseEntity<SolutionDto> retrySolution(
                @PathVariable Long institutionId,
                @PathVariable Long solutionId
            ) {
        var solution = solutionService.retrySolution(solutionId);
        
        return ResponseEntity.ok(new SolutionDto(solution));
    }

    @DeleteMapping("/{solutionId}")
    public ResponseEntity<Void> deleteSolution(
        @PathVariable Long institutionId,
        @PathVariable Long solutionId) {
        solutionService.deleteSolution(solutionId);
        return ResponseEntity.noContent().build();
    }

}