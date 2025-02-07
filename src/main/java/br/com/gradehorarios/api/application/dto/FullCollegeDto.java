package br.com.gradehorarios.api.application.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

import br.com.gradehorarios.api.domain.entity.college.College;
import br.com.gradehorarios.api.domain.entity.college.Solution;

public record FullCollegeDto(
    Integer id,
    Integer code,
    String name,
    List<DisciplineDto> disciplines,
    List<FullTeacherDto> teachers,
    List<FullClassroomDto> classrooms,
    List<SolutionDto> solutions
) {

    public FullCollegeDto(College college) {
        this(
            college.getId(),
            college.getCode(),
            college.getName(),
            college.getDisciplines() != null 
                ? college.getDisciplines().stream()
                      .map(DisciplineDto::new)
                      .collect(Collectors.toList())
                : null,
            college.getTeachers() != null 
                ? college.getTeachers().stream()
                      .map(FullTeacherDto::new)
                      .collect(Collectors.toList())
                : null,
            college.getClassrooms() != null 
                ? college.getClassrooms().stream()
                      .map(FullClassroomDto::new)
                      .collect(Collectors.toList())
                : null,
            college.getSolutions() != null 
                ? college.getSolutions().stream()
                    .map(Solution::toSolutionDto)
                    .sorted(Comparator.comparing(SolutionDto::createdAt).reversed())
                    .collect(Collectors.toList())
                : null
        );
    }
}
