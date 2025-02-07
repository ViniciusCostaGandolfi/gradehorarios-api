package br.com.gradehorarios.api.application.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gradehorarios.api.application.dto.SolutionDto;
import br.com.gradehorarios.api.application.dto.UserDto;
import br.com.gradehorarios.api.application.service.TimetableSolverService;


@RestController
@RequestMapping(ApiVersion.VERSION + "/colleges/{collegeId}/solutions")
public class TimetableSolverController {

    @Autowired
    private TimetableSolverService timetableSolverService;

    @PostMapping
    private SolutionDto resolve(
        @PathVariable
        Integer collegeId
    ) {
        return timetableSolverService.resolve(collegeId);
    }

    @DeleteMapping("/{solutionId}")
    public void delete(
        @PathVariable 
        Integer solutionId,
        @PathVariable 
        Integer collegeId,
        @AuthenticationPrincipal
        UserDto userDto
    ) {
        timetableSolverService.deleteById(solutionId, collegeId, userDto.id());
    }

    
    
}
