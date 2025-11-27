package br.com.gradehorarios.gradehorarios.infra.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.gradehorarios.gradehorarios.application.dto.SolutionDto;
import br.com.gradehorarios.gradehorarios.application.service.SolutionService;
import br.com.gradehorarios.gradehorarios.bootstrap.security.dto.JwtUserDto;

@RestController
@RequestMapping("/api/institutions/{institutionId}/solutions")
@RequiredArgsConstructor
public class SolutionController {

    @Autowired
    private SolutionService solutionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SolutionDto> createSolution(
            @PathVariable Long institutionId,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal JwtUserDto user
    ) throws Exception {
        var solution = solutionService.createSolution(file, user.id(), institutionId);
        
        return ResponseEntity.ok(new SolutionDto(solution));
    }
}