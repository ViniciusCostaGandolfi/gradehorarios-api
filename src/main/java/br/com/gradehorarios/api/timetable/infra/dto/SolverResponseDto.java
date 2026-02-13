package br.com.gradehorarios.api.timetable.infra.dto;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolverResponseDto {
    private List<TimetableBaseDto> teachers;
    private List<TimetableBaseDto> classrooms;
}