package br.com.gradehorarios.api.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.gradehorarios.api.domain.entity.college.College;

public record SolutionInputDto(
    Integer collegeId,
    Integer code,
    String name,
    List<DisciplineDto> disciplines,
    List<FullTeacherDto> teachers,
    List<FullClassroomDto> classrooms
    
    ) {
        public SolutionInputDto(College college) {
            this(
                college.getId(),
                college.getCode(),
                college.getName(),
                college.getDisciplines() != null ? college.getDisciplines().stream()
                    .map(DisciplineDto::new)
                    .collect(Collectors.toList()) : null,
                college.getTeachers() != null ? college.getTeachers().stream()
                .map(FullTeacherDto::new)
                .collect(Collectors.toList()) : null,
                college.getClassrooms() != null ? college.getClassrooms().stream()
                .map(FullClassroomDto::new)
                .collect(Collectors.toList()) : null
                );
    }


}
