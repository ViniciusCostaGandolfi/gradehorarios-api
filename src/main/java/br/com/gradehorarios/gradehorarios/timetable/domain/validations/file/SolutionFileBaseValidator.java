package br.com.gradehorarios.gradehorarios.timetable.domain.validations.file;

import java.util.Map;

import tech.tablesaw.api.Table;

public interface SolutionFileBaseValidator {
    void validate(Map<String, Table> dfDict);
}
