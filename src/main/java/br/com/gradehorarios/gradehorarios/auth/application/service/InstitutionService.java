package br.com.gradehorarios.gradehorarios.auth.application.service;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gradehorarios.gradehorarios.auth.application.dto.CreateInstitutionRequest;
import br.com.gradehorarios.gradehorarios.auth.application.dto.InstitutionResponseDTO;
import br.com.gradehorarios.gradehorarios.auth.application.dto.UpdateInstitutionRequest;
import br.com.gradehorarios.gradehorarios.auth.domain.entity.Institution;
import br.com.gradehorarios.gradehorarios.auth.domain.entity.RoleName;
import br.com.gradehorarios.gradehorarios.auth.infra.repository.InstitutionRepository;
import br.com.gradehorarios.gradehorarios.bootstrap.dto.JwtUserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final InstitutionRepository repository;

    @Transactional
    public InstitutionResponseDTO create(CreateInstitutionRequest request) {

        Institution institution = new Institution();
        institution.setName(request.name());
        institution.setCode(request.code());
        institution.setActive(true);

        return mapToDto(repository.save(institution));
    }

    public List<InstitutionResponseDTO> findAll(JwtUserDto user) {
        if (user.globalRole() == RoleName.ROLE_ADMIN) {
            return repository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        }
        
        return findAllByUserId(user.id());
    }

    public List<InstitutionResponseDTO> findAllByUserId(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public InstitutionResponseDTO findById(Long id) {
        Institution institution = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institution not found"));
        return mapToDto(institution);
    }

    @Transactional
    public InstitutionResponseDTO update(Long id, UpdateInstitutionRequest request) {
        Institution institution = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institution not found"));

        if (request.name() != null) institution.setName(request.name());
        
        if (request.code() != null && !request.code().equals(institution.getCode())) {
            institution.setCode(request.code());
        }

        if (request.active() != null) institution.setActive(request.active());

        return mapToDto(repository.save(institution));
    }

    @Transactional
    public void delete(Long id) {
        Institution institution = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institution not found"));
        institution.setActive(false);
        repository.save(institution);
    }

    private InstitutionResponseDTO mapToDto(Institution entity) {
        return new InstitutionResponseDTO(
            entity.getId(),
            entity.getName(),
            entity.getCode(),
            entity.isActive()
        );
    }
}