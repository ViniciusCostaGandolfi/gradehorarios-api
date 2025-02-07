package br.com.gradehorarios.api.application.controller.v1;

import br.com.gradehorarios.api.application.dto.FullClassroomDto;
import br.com.gradehorarios.api.application.dto.UserDto;
import br.com.gradehorarios.api.application.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(ApiVersion.VERSION + "/colleges/{collegeId}/classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @GetMapping
    public List<FullClassroomDto> getAllClassrooms(
        @PathVariable 
        Integer collegeId) {
        return classroomService.getAllByCollegeId(collegeId).stream()
                .map(FullClassroomDto::new)
                .toList();
    }

    @GetMapping("/{classroomId}")
    public FullClassroomDto getClassroomById(
        @PathVariable 
        Integer collegeId, @PathVariable Integer classroomId) {
        FullClassroomDto classroomDto = new FullClassroomDto(classroomService.getById(classroomId));
        if (!classroomDto.collegeId().equals(collegeId)) {
            throw new IllegalArgumentException("Classroom does not belong to the specified college.");
        }
        return classroomDto;
    }

    @PutMapping
    public FullClassroomDto updateOrCreateClassroom(
        @PathVariable 
        Integer collegeId, 
        @RequestBody @Valid 
        FullClassroomDto classroomDto,
        @AuthenticationPrincipal
        UserDto userDto
        ) {
        return new FullClassroomDto(classroomService.updateOrCreate(classroomDto, collegeId, userDto.id()));
    }

    @DeleteMapping("/{classroomId}")
    public void deleteClassroom(
        @PathVariable 
        Integer collegeId, 
        @PathVariable 
        Integer classroomId,
        @AuthenticationPrincipal
        UserDto userDto
        ) {
        FullClassroomDto classroomDto = new FullClassroomDto(classroomService.getById(classroomId));
        if (!classroomDto.collegeId().equals(collegeId)) {
            throw new IllegalArgumentException("Classroom does not belong to the specified college.");
        }
        classroomService.deleteById(classroomId);
    }
}
