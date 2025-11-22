package br.com.gradehorarios.gradehorarios.solution.domain.validations;

import java.util.Map;

import tech.tablesaw.api.Table;

public interface ISolutionFileBaseValidator {
    void validate(Map<String, Table> dfDict);
}
