package br.com.gradehorarios.gradehorarios.domain.validations.solution.file;

import java.util.List;
import java.util.Map;
import java.util.Set;
import tech.tablesaw.api.Table;

public class SolutionFileSheet2Validator implements ISolutionFileBaseValidator {
    @Override
    public void validate(Map<String, Table> dfDict) {
        Table df = dfDict.get(FileSheetNamesMock.DISCIPLINAS_X_TURMAS_2);
        Table dfCadastro = dfDict.get(FileSheetNamesMock.CADASTRAMENTO_1);

        if (!df.columnNames().contains("DISCIPLINAS")) {
            throw new IllegalArgumentException("Erro, a coluna DISCIPLINAS não esta presente no arquivo excel, baixe o modelo e revise.");
        }

        Set<String> turmasEsperadas = dfCadastro.stringColumn("TURMA").unique().removeMissing().asSet();
        Set<String> disciplinasEsperadas = dfCadastro.stringColumn("DISCIPLINA").unique().removeMissing().asSet();
        
        Set<String> disciplinasNaAba = df.stringColumn("DISCIPLINAS").asSet();
        for (String disciplina : disciplinasEsperadas) {
            if (!disciplinasNaAba.contains(disciplina)) {
                throw new IllegalArgumentException("Erro, a disciplina '" + disciplina + "' presente na aba '1 - Cadastramento' não foi encontrada na aba '2 - Disciplinas x Turmas'.");
            }
        }

        List<String> colunasNaAba = df.columnNames();
        for (String turma : turmasEsperadas) {
            if (!colunasNaAba.contains(turma)) {
                throw new IllegalArgumentException("Erro, a turma '" + turma + "' presente na aba '1 - Cadastramento' não foi encontrada na aba '2 - Disciplinas x Turmas'.");
            }
        }
    }
}