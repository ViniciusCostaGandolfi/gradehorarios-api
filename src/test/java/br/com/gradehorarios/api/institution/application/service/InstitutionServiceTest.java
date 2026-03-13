package br.com.gradehorarios.api.institution.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gradehorarios.api.auth.domain.model.RoleName;
import br.com.gradehorarios.api.auth.domain.model.User;
import br.com.gradehorarios.api.auth.domain.model.UserInstitutionRole;
import br.com.gradehorarios.api.auth.domain.repository.UserInstitutionRoleRepository;
import br.com.gradehorarios.api.auth.domain.repository.UserRepository;
import br.com.gradehorarios.api.auth.infra.security.dto.JwtUserDto;
import br.com.gradehorarios.api.institution.application.dto.CreateInstitutionRequest;
import br.com.gradehorarios.api.institution.application.dto.UpdateInstitutionRequest;
import br.com.gradehorarios.api.institution.domain.model.Institution;
import br.com.gradehorarios.api.institution.domain.repository.InstitutionRepository;

@ExtendWith(MockitoExtension.class)
class InstitutionServiceTest {

    @Mock
    private InstitutionRepository repository;

    @Mock
    private UserInstitutionRoleRepository userInstitutionRoleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private InstitutionService institutionService;

    private User sampleUser;
    private JwtUserDto sampleJwtUserDto;
    private Institution sampleInstitution;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setName("John Doe");

        sampleJwtUserDto = new JwtUserDto(1L, "john@example.com", RoleName.ROLE_USER, List.of());

        sampleInstitution = new Institution();
        sampleInstitution.setId(10L);
        sampleInstitution.setName("Test Institution");
        sampleInstitution.setCode("TI");
        sampleInstitution.setActive(true);
    }

    @Test
    void create_Success() {
        CreateInstitutionRequest request = new CreateInstitutionRequest("Test Institution", "TI");

        when(repository.save(any(Institution.class))).thenReturn(sampleInstitution);
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));

        Institution result = institutionService.create(request, sampleJwtUserDto);

        assertNotNull(result);
        assertEquals("Test Institution", result.getName());
        verify(repository).save(any(Institution.class));
        verify(userInstitutionRoleRepository).save(any(UserInstitutionRole.class));
    }

    @Test
    void create_UserNotFound_ThrowsException() {
        CreateInstitutionRequest request = new CreateInstitutionRequest("Test Institution", "TI");

        when(repository.save(any(Institution.class))).thenReturn(sampleInstitution);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> institutionService.create(request, sampleJwtUserDto));
    }

    @Test
    void findAll_AdminUser_ReturnsAllInstitutions() {
        JwtUserDto adminUser = new JwtUserDto(2L, "admin@example.com", RoleName.ROLE_ADMIN, List.of());
        when(repository.findAll()).thenReturn(List.of(sampleInstitution));

        List<Institution> result = institutionService.findAll(adminUser);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository).findAll();
        verify(repository, never()).findByUserId(anyLong());
    }

    @Test
    void findAll_NormalUser_ReturnsUserInstitutions() {
        when(repository.findByUserId(1L)).thenReturn(List.of(sampleInstitution));

        List<Institution> result = institutionService.findAll(sampleJwtUserDto);

        assertFalse(result.isEmpty());
        verify(repository).findByUserId(1L);
        verify(repository, never()).findAll();
    }

    @Test
    void findById_InstitutionExists_ReturnsInstitution() {
        when(repository.findById(10L)).thenReturn(Optional.of(sampleInstitution));

        Institution result = institutionService.findById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    void findById_InstitutionDoesNotExist_ThrowsException() {
        when(repository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> institutionService.findById(10L));
    }

    @Test
    void update_InstitutionExists_UpdatesAndReturnsInstitution() {
        UpdateInstitutionRequest request = new UpdateInstitutionRequest("Updated Name", "UTI", false);
        when(repository.findById(10L)).thenReturn(Optional.of(sampleInstitution));
        when(repository.save(any(Institution.class))).thenReturn(sampleInstitution);

        Institution result = institutionService.update(10L, request);

        assertNotNull(result);
        assertEquals("Updated Name", sampleInstitution.getName());
        assertEquals("UTI", sampleInstitution.getCode());
        assertFalse(sampleInstitution.isActive());
        verify(repository).save(sampleInstitution);
    }

    @Test
    void delete_InstitutionExists_DeactivatesInstitution() {
        when(repository.findById(10L)).thenReturn(Optional.of(sampleInstitution));
        when(repository.save(any(Institution.class))).thenReturn(sampleInstitution);

        institutionService.delete(10L);

        assertFalse(sampleInstitution.isActive());
        verify(repository).save(sampleInstitution);
    }
}
