package br.com.gradehorarios.gradehorarios.auth.application.service;


import br.com.gradehorarios.gradehorarios.application.dto.CreateInstitutionRequest;
import br.com.gradehorarios.gradehorarios.application.dto.InstitutionResponseDto;
import br.com.gradehorarios.gradehorarios.application.dto.UpdateInstitutionRequest;
import br.com.gradehorarios.gradehorarios.application.service.InstitutionService;
import br.com.gradehorarios.gradehorarios.domain.entity.Institution;
import br.com.gradehorarios.gradehorarios.infra.repository.InstitutionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;




@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
public class InstitutionServiceTest {

    @Mock
    private InstitutionRepository repository;

    @InjectMocks
    private InstitutionService service;

    private Institution institution;
    private CreateInstitutionRequest createRequest;
    
    @BeforeEach
    void setUp() {
        institution = new Institution();
        institution.setId(1L);
        institution.setName("Escola Teste");
        institution.setCode("CODE_123");
        institution.setActive(true);

        createRequest = new CreateInstitutionRequest("Escola Teste", "CODE_123");
        new UpdateInstitutionRequest("Escola Atualizada", "NEW_CODE", true);
    }


    @Test
    @DisplayName("Deve criar instituição quando o código NÃO existe")
    void shouldCreateInstitution() {

        when(repository.save(any(Institution.class))).thenReturn(institution);

        InstitutionResponseDto response = service.create(createRequest);

        assertNotNull(response);
        assertEquals("Escola Teste", response.name());
        
        verify(repository, times(1)).save(any(Institution.class));
    }

}