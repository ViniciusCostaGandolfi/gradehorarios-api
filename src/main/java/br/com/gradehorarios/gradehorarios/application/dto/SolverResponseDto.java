package br.com.gradehorarios.gradehorarios.application.dto;


import lombok.Data;
import java.util.List;

@Data
public class SolverResponseDto {
    private List<TimetableBaseDto> teachers;
    private List<TimetableBaseDto> classrooms;
}