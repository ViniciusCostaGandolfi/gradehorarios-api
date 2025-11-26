package br.com.gradehorarios.gradehorarios.infra.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.gradehorarios.gradehorarios.application.dto.SolutionDto;
import br.com.gradehorarios.gradehorarios.application.service.SolutionService;


@RestController
@RequestMapping("/api/institutions/{institutionId}/solutions")

public class SolutionController {
    

    @Autowired
    private SolutionService solutionService;

    @PostMapping
    private ResponseEntity<SolutionDto> createSolution(
        @RequestParam(name = "file") MultipartFile file,
        @RequestParam(name = "institutionId") Long institutionId
    ){

        var solution = this.solutionService.createSolution(file, institutionId);

        return ResponseEntity.ok().body(new SolutionDto(solution));
    }

}
