package br.com.gradehorarios.api.application.dto;

public record SolutionDailyDto(
    DisciplineDto discipline,
    TeacherDto teacher,
    ClassroomDto classroom,
    Boolean monday,
    Boolean tuesday,
    Boolean wednesday,
    Boolean thursday,
    Boolean friday,
    Boolean saturday,
    Boolean sunday,
    Integer startClassIndex,
    Integer endClassIndex
) {
    
}
