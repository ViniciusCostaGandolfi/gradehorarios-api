package br.com.gradehorarios.api.application.controller.v1;

import br.com.gradehorarios.api.application.dto.FullTeacherDto;
import br.com.gradehorarios.api.application.dto.UserDto;
import br.com.gradehorarios.api.application.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ApiVersion.VERSION + "/colleges/{collegeId}/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    public List<FullTeacherDto> getAllTeachers(
        @PathVariable 
        Integer collegeId
        ) {
        return teacherService.getAllByCollegeId(collegeId).stream()
                .map(FullTeacherDto::new)
                .toList();
    }

    @GetMapping("/{teacherId}")
    public FullTeacherDto getTeacherById(
        @PathVariable 
        Integer collegeId, 
        @PathVariable Integer teacherId
        ) {
        FullTeacherDto teacherDto = new FullTeacherDto(teacherService.getById(teacherId));
        
        return teacherDto;
    }

    @PutMapping
    public FullTeacherDto updateOrCreateTeacher(
        @PathVariable 
        Integer collegeId, 
        @RequestBody @Valid 
        FullTeacherDto teacherDto,
        @AuthenticationPrincipal
        UserDto userDto
        ) {

        return new FullTeacherDto(teacherService.updateOrCreate(teacherDto, collegeId, userDto.id()));
    }

    @DeleteMapping("/{teacherId}")
    public void deleteTeacher(
        @PathVariable 
        Integer collegeId, 
        @PathVariable Integer teacherId,
        @AuthenticationPrincipal
        UserDto userDto
        ) {
        FullTeacherDto teacherDto = new FullTeacherDto(teacherService.getById(teacherId));
        if (!teacherDto.collegeId().equals(collegeId)) {
            throw new IllegalArgumentException("Teacher does not belong to the specified college.");
        }
        teacherService.deleteById(teacherId);
    }
}
