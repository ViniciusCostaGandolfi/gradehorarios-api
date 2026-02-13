package br.com.gradehorarios.api.timetable.infra.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.gradehorarios.api.auth.infra.security.dto.JwtUserDto;
import br.com.gradehorarios.api.shared.infra.storage.S3FileStorageService;
import br.com.gradehorarios.api.timetable.application.dto.SolutionDto;
import br.com.gradehorarios.api.timetable.application.service.SolutionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/institutions/{institutionId}/solutions")
@RequiredArgsConstructor
public class SolutionController {

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private S3FileStorageService storageService;

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