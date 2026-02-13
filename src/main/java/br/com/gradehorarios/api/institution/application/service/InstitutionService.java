package br.com.gradehorarios.api.institution.application.service;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gradehorarios.api.auth.domain.model.RoleName;
import br.com.gradehorarios.api.auth.domain.model.UserInstitutionRole;
import br.com.gradehorarios.api.auth.domain.model.UserInstitutionRoleName;
import br.com.gradehorarios.api.auth.domain.repository.UserInstitutionRoleRepository;
import br.com.gradehorarios.api.auth.domain.repository.UserRepository;
import br.com.gradehorarios.api.auth.infra.security.dto.JwtUserDto;
import br.com.gradehorarios.api.institution.application.dto.CreateInstitutionRequest;
import br.com.gradehorarios.api.institution.application.dto.UpdateInstitutionRequest;
import br.com.gradehorarios.api.institution.domain.model.Institution;
import br.com.gradehorarios.api.institution.domain.repository.InstitutionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final InstitutionRepository repository;

    private final UserInstitutionRoleRepository userInstitutionRoleRepository;

    private final UserRepository userRepository;
    

    @Transactional
    public Institution create(CreateInstitutionRequest request, JwtUserDto userDto) {

        var institution = new Institution();
        institution.setName(request.name());
        institution.setCode(request.code());
        institution.setActive(true);
        institution = repository.save(institution);

        var user = userRepository.findById(userDto.id())
                .orElseThrow(() -> new RuntimeException("User not found"));


        var institutionRole = new UserInstitutionRole();
        institutionRole.setInstitution(institution);
        institutionRole.setUser(user);
        institutionRole.setRole(UserInstitutionRoleName.OWNER);
        userInstitutionRoleRepository.save(institutionRole);


        return institution;
    }

    public List<Institution> findAll(JwtUserDto user) {
        if (user.role() == RoleName.ROLE_ADMIN) {
            return repository.findAll();
        }
        
        return findAllByUserId(user.id());
    }

    public List<Institution> findAllByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    public Institution findById(Long id) {
        Institution institution = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institution not found"));
        return institution;
    }

    @Transactional
    public Institution update(Long id, UpdateInstitutionRequest request) {
        Institution institution = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institution not found"));

        if (request.name() != null) institution.setName(request.name());
        
        if (request.code() != null && !request.code().equals(institution.getCode())) {
            institution.setCode(request.code());
        }

        if (request.active() != null) institution.setActive(request.active());

        return repository.save(institution);
    }

    @Transactional
    public void delete(Long id) {
        Institution institution = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institution not found"));
        institution.setActive(false);
        repository.save(institution);
    }

}