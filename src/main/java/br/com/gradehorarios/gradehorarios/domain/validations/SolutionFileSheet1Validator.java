package br.com.gradehorarios.gradehorarios.domain.validations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

public class SolutionFileSheet1Validator implements ISolutionFileBaseValidator {
    @Override
    public void validate(Map<String, Table> dfDict) {
        Table df = dfDict.get(FileSheetNamesMock.CADASTRAMENTO_1);
        List<String> colunasEsperadas = Arrays.asList(
                "PROFESSOR", "TURMA", "TURNO", "DISCIPLINA", "PERIODO_INTERVALO", "NOME_INTERVALO"
        );

        for (String coluna : colunasEsperadas) {
            if (!df.columnNames().contains(coluna)) {
                throw new IllegalArgumentException("Erro, a coluna " + coluna + " não esta presente no arquivo excel, baixe o modelo e revise.");
            }
        }

        validarColunaPreenchida(df, "PROFESSOR");
        validarColunaPreenchida(df, "TURMA");
        validarColunaPreenchida(df, "DISCIPLINA");
        validarColunaPreenchida(df, "TURNO");

        validarValoresPermitidos(df, "PERIODO_INTERVALO", ValidationConstants.PERIODOS_INTERVALO_VALIDOS);
        validarValoresPermitidos(df, "NOME_INTERVALO", ValidationConstants.INTERVALOS_VALIDOS);
    }

    private void validarColunaPreenchida(Table df, String colName) {
        if (df.column(colName).removeMissing().size() < 1) {
            throw new IllegalArgumentException("Erro, a coluna '" + colName + "' deve conter ao menos um valor preenchido.");
        }
    }

    private void validarValoresPermitidos(Table df, String colName, List<String> permitidos) {
        StringColumn coluna = df.stringColumn(colName);
        Set<String> valoresUnicos = coluna.unique().removeMissing().asSet();
        
        for (String valor : valoresUnicos) {
            if (!permitidos.contains(valor)) {
                throw new IllegalArgumentException(
                    String.format("Erro: O valor '%s' na coluna '%s' não é válido. Valores permitidos são: %s",
                            valor, colName, String.join(", ", permitidos))
                );
            }
        }
    }
}