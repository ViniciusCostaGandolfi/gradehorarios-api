package br.com.gradehorarios.api.timetable.infra.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import br.com.gradehorarios.api.auth.infra.security.dto.JwtUserDto;
import br.com.gradehorarios.api.shared.infra.storage.S3FileStorageService;
import br.com.gradehorarios.api.timetable.application.dto.SolutionDto;
import br.com.gradehorarios.api.timetable.application.service.SolutionService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/institutions/{institutionId}/solutions")
@RequiredArgsConstructor
@Tag(name = "Solution API", description = "Endpoints for timetable generation solutions within an institution")
public class SolutionController {

    private final SolutionService solutionService;

    private final S3FileStorageService storageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create solution", description = "Uploads a spreadsheet file and creates a new timetable solution generation request")
    public ResponseEntity<SolutionDto> createSolution(
            @PathVariable Long institutionId,
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal JwtUserDto user
    ) throws Exception {
        var solution = solutionService.createSolution(file, user.id(), institutionId);
        
        return ResponseEntity.ok(new SolutionDto(solution));
    }

    @GetMapping("/{solutionId}")
    @Operation(summary = "Get solution", description = "Returns details and status of a specific solution")
    public SolutionDto getSolutionById(@PathVariable Long solutionId, @PathVariable Long institutionId) {
        return new SolutionDto(
            this.solutionService.getSolutionByInstitutionIdAndSolutionId(institutionId, solutionId),
            storageService
        ) ;
    }
    

    @PutMapping("/{solutionId}")
    @Operation(summary = "Retry solution", description = "Retries a previously failed or stopped solution generation")
    public ResponseEntity<SolutionDto> retrySolution(
                @PathVariable Long institutionId,
                @PathVariable Long solutionId
            ) {
        var solution = solutionService.retrySolution(solutionId);
        
        return ResponseEntity.ok(new SolutionDto(solution));
    }

    @DeleteMapping("/{solutionId}")
    @Operation(summary = "Delete solution", description = "Deletes a solution by ID")
    public ResponseEntity<Void> deleteSolution(
        @PathVariable Long institutionId,
        @PathVariable Long solutionId) {
        solutionService.deleteSolution(solutionId);
        return ResponseEntity.noContent().build();
    }

}