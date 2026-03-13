package br.com.gradehorarios.api.institution.infra.controller;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import br.com.gradehorarios.api.auth.infra.security.dto.JwtUserDto;
import br.com.gradehorarios.api.institution.application.dto.CreateInstitutionRequest;
import br.com.gradehorarios.api.institution.application.dto.InstitutionResponseDto;
import br.com.gradehorarios.api.institution.application.dto.UpdateInstitutionRequest;
import br.com.gradehorarios.api.institution.application.service.InstitutionService;
import br.com.gradehorarios.api.shared.domain.service.FileStorageService;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/institutions")
@Tag(name = "Institution API", description = "Endpoints for managing educational institutions")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService service;

    private final FileStorageService storageService;


    @PostMapping
    @Operation(summary = "Create institution", description = "Creates a new institution")
    public ResponseEntity<InstitutionResponseDto> create(
            @RequestBody CreateInstitutionRequest request,
            Authentication authentication
    ) {
        JwtUserDto user = (JwtUserDto) authentication.getPrincipal();

        var institution = service.create(request, user);
        
        return ResponseEntity.ok(new InstitutionResponseDto(institution));
    }

    @GetMapping
    @Operation(summary = "List institutions", description = "Returns a list of institutions associated with the current user")
    public ResponseEntity<List<InstitutionResponseDto>> listAll(Authentication authentication) {
        JwtUserDto user = (JwtUserDto) authentication.getPrincipal();
        
        return ResponseEntity.ok(service.findAllByUserId(user.id()).stream()
                .map(inst -> new InstitutionResponseDto(inst, storageService))
                .toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get institution by ID", description = "Returns institution details by ID")
    public ResponseEntity<InstitutionResponseDto> getById(@PathVariable Long id) {
        var inst = service.findById(id);
        return ResponseEntity.ok(new InstitutionResponseDto(inst, storageService));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update institution", description = "Updates an institution by ID")
    public ResponseEntity<InstitutionResponseDto> update(@PathVariable Long id, @RequestBody UpdateInstitutionRequest request) {
        var inst = service.update(id, request);
        return ResponseEntity.ok(new InstitutionResponseDto(inst));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete institution", description = "Deletes an institution by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}