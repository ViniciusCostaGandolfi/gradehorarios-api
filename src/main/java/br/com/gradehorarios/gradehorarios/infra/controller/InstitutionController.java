package br.com.gradehorarios.gradehorarios.infra.controller;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.gradehorarios.gradehorarios.application.dto.institution.CreateInstitutionRequest;
import br.com.gradehorarios.gradehorarios.application.dto.institution.InstitutionResponseDto;
import br.com.gradehorarios.gradehorarios.application.dto.institution.UpdateInstitutionRequest;
import br.com.gradehorarios.gradehorarios.application.service.InstitutionService;
import br.com.gradehorarios.gradehorarios.application.service.storage.IStorageService;
import br.com.gradehorarios.gradehorarios.bootstrap.security.dto.JwtUserDto;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    @Autowired
    private InstitutionService service;

    @Autowired
    private IStorageService storageService;


    @PostMapping
    public ResponseEntity<InstitutionResponseDto> create(
            @RequestBody CreateInstitutionRequest request,
            Authentication authentication
    ) {
        JwtUserDto user = (JwtUserDto) authentication.getPrincipal();

        var institution = service.create(request, user);
        
        return ResponseEntity.ok(new InstitutionResponseDto(institution));
    }

    @GetMapping
    public ResponseEntity<List<InstitutionResponseDto>> listAll(Authentication authentication) {
        JwtUserDto user = (JwtUserDto) authentication.getPrincipal();
        
        return ResponseEntity.ok(service.findAllByUserId(user.id()).stream()
                .map(inst -> new InstitutionResponseDto(inst, storageService))
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionResponseDto> getById(@PathVariable Long id) {
        var inst = service.findById(id);
        return ResponseEntity.ok(new InstitutionResponseDto(inst, storageService));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstitutionResponseDto> update(@PathVariable Long id, @RequestBody UpdateInstitutionRequest request) {
        var inst = service.update(id, request);
        return ResponseEntity.ok(new InstitutionResponseDto(inst));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}