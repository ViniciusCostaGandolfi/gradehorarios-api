package br.com.gradehorarios.api.timetable.infra.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimetableBaseDto {
    private String name;
    private List<String> monday = new ArrayList<>();
    private List<String> tuesday = new ArrayList<>();
    private List<String> wednesday = new ArrayList<>();
    private List<String> thursday = new ArrayList<>();
    private List<String> friday = new ArrayList<>();
    private List<String> saturday = new ArrayList<>();
    private List<String> sunday = new ArrayList<>();
}