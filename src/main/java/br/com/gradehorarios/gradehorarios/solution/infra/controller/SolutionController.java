package br.com.gradehorarios.gradehorarios.solution.infra.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.gradehorarios.gradehorarios.bootstrap.dto.JwtUserDto;
import br.com.gradehorarios.gradehorarios.solution.application.dto.SolutionDto;
import br.com.gradehorarios.gradehorarios.solution.application.service.SolutionService;


@RestController("/api/institutions/{institutionId}/solutions")
public class SolutionController {
    

    @Autowired
    private SolutionService solutionService;

    @PostMapping
    private ResponseEntity<SolutionDto> createSolution(
        @RequestParam(name = "file") MultipartFile file,
        @RequestParam(name = "institutionId") Long institutionId,
        @AuthenticationPrincipal JwtUserDto user
    ){

        var solution = this.solutionService.createSolution(file, user.id(), institutionId);

        return ResponseEntity.ok().body(new SolutionDto(solution));
    }

}
