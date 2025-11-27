package br.com.gradehorarios.gradehorarios.application.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
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