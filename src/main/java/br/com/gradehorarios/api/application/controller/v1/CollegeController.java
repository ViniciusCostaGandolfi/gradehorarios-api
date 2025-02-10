package br.com.gradehorarios.api.application.controller.v1;

import br.com.gradehorarios.api.application.dto.CollegeDto;
import br.com.gradehorarios.api.application.dto.FullCollegeDto;
import br.com.gradehorarios.api.application.dto.UserDto;
import br.com.gradehorarios.api.application.service.CollegeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping(ApiVersion.VERSION + "/colleges")
public class CollegeController {

    @Autowired
    private CollegeService collegeService;

    @GetMapping
    public List<CollegeDto> getAllColleges(
        @AuthenticationPrincipal
        UserDto userDto
    ) {
        return collegeService.getAllByUserId(userDto.id())
                            .stream()
                            .map(CollegeDto::new)
                            .toList();
    }

    @GetMapping("/full")
    public List<FullCollegeDto> getAllCollegesFull(
        @AuthenticationPrincipal
        UserDto userDto
    ) {
        return collegeService.getAllByUserId(userDto.id())
            .stream()
            .map(FullCollegeDto::new)
            .toList();
    }



    @GetMapping("/{collegeId}")
    public FullCollegeDto getCollegeById(@PathVariable Integer collegeId) {
        return new FullCollegeDto(collegeService.getById(collegeId));
    }

    @PutMapping
    public CollegeDto updateOrCreateCollege
    (
        @RequestBody 
        @Valid 
        CollegeDto collegeDto, 
        @AuthenticationPrincipal
        UserDto userDto
    ) {
        return new CollegeDto(collegeService.updateOrCreate(collegeDto, userDto));
    }

    @DeleteMapping("/{collegeId}")
    public void deleteCollege(
        @PathVariable 
        Integer collegeId,
        @AuthenticationPrincipal
        UserDto userDto) {
        collegeService.deleteById(collegeId, userDto);
    }
}
