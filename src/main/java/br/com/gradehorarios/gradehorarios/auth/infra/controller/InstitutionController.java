package br.com.gradehorarios.gradehorarios.auth.infra.controller;


import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.gradehorarios.gradehorarios.auth.application.dto.CreateInstitutionRequest;
import br.com.gradehorarios.gradehorarios.auth.application.dto.InstitutionResponseDTO;
import br.com.gradehorarios.gradehorarios.auth.application.dto.UpdateInstitutionRequest;
import br.com.gradehorarios.gradehorarios.auth.application.service.InstitutionService;
import br.com.gradehorarios.gradehorarios.bootstrap.dto.JwtUserDto;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    @Autowired
    private InstitutionService service;

    @PostMapping
    public ResponseEntity<InstitutionResponseDTO> create(@RequestBody CreateInstitutionRequest request) {
        return ResponseEntity.status(201).body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<InstitutionResponseDTO>> listAll(Authentication authentication) {
        JwtUserDto user = (JwtUserDto) authentication.getPrincipal();
        
        return ResponseEntity.ok(service.findAll(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitutionResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstitutionResponseDTO> update(@PathVariable Long id, @RequestBody UpdateInstitutionRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}