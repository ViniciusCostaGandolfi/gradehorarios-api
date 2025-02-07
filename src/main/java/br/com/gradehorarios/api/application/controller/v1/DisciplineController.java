package br.com.gradehorarios.api.application.controller.v1;

import br.com.gradehorarios.api.application.dto.DisciplineDto;
import br.com.gradehorarios.api.application.dto.UserDto;
import br.com.gradehorarios.api.application.service.DisciplineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ApiVersion.VERSION + "/colleges/{collegeId}/disciplines")
public class DisciplineController {

    @Autowired
    private DisciplineService disciplineService;

    @GetMapping
    public List<DisciplineDto> getAllDisciplinesByCollege(
        @PathVariable 
        Integer collegeId
    ) {
        return disciplineService.getAllByCollegeId(collegeId)
                                .stream()
                                .map(DisciplineDto::new)
                                .toList();
    }

    @GetMapping("/{disciplineId}")
    public DisciplineDto getDisciplineById(
        @PathVariable 
        Integer disciplineId,
        @PathVariable 
        Integer collegeId
        ) {
        return new DisciplineDto(disciplineService.getByIdAndCollegeId(disciplineId, collegeId));
    }

    @PutMapping
    public DisciplineDto updateOrCreateDiscipline(
        @RequestBody 
        @Valid 
        DisciplineDto disciplineDto,
        @PathVariable 
        Integer collegeId,
        @AuthenticationPrincipal
        UserDto userDto
    ) {

        return new DisciplineDto(disciplineService.updateOrCreate(disciplineDto, collegeId, userDto.id()));
    }

    @DeleteMapping("/{disciplineId}")
    public void deleteDiscipline(
        @PathVariable 
        Integer disciplineId,
        @PathVariable 
        Integer collegeId,
        @AuthenticationPrincipal
        UserDto userDto
    ) {
        disciplineService.deleteById(disciplineId, collegeId, userDto.id());
    }
}
