package br.com.gradehorarios.api.application.dto;

import java.util.List;

public record SolutionTeacherDisciplines(
        List<SolutionDailyDto> solutionDisciplineDailyDto 
) {
    
}
