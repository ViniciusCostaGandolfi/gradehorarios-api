package br.com.gradehorarios.gradehorarios.domain.validations.solution.file;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import tech.tablesaw.api.Table;

public class SolutionFileSheetNamesValidator implements ISolutionFileBaseValidator {
    @Override
    public void validate(Map<String, Table> dfDict) {
        List<String> expectedSheets = Arrays.asList(
            FileSheetNamesMock.CADASTRAMENTO_1,
            FileSheetNamesMock.DISCIPLINAS_X_TURMAS_2,
            FileSheetNamesMock.PROFESSOR_X_DISCIPLINAS_3,
            FileSheetNamesMock.QUANTIDADE_AULAS_4,
            FileSheetNamesMock.FOLGA_5,
            FileSheetNamesMock.PREFERENCIAS_6,
            FileSheetNamesMock.RESTRICAO_IMPOSICAO_7,
            FileSheetNamesMock.RESTRICOES_PROIBICAO_8
        );

        for (String sheet : expectedSheets) {
            if (!dfDict.containsKey(sheet)) {
                throw new IllegalArgumentException("Erro: A aba '" + sheet + "' não foi encontrada no arquivo. "
                        + "Por favor, baixe o modelo e preencha corretamente.");
            }
        }
    }
}
