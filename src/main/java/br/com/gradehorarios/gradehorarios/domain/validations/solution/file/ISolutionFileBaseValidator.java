package br.com.gradehorarios.gradehorarios.domain.validations.solution.file;

import java.util.Map;

import tech.tablesaw.api.Table;

public interface ISolutionFileBaseValidator {
    void validate(Map<String, Table> dfDict);
}
