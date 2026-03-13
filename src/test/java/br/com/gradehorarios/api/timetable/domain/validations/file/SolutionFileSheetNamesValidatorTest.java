package br.com.gradehorarios.api.timetable.domain.validations.file;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tech.tablesaw.api.Table;

class SolutionFileSheetNamesValidatorTest {

    private SolutionFileSheetNamesValidator validator;

    @BeforeEach
    void setUp() {
        validator = new SolutionFileSheetNamesValidator();
    }

    @Test
    void validate_AllExpectedSheetsPresent_DoesNotThrowException() {
        Map<String, Table> dfDict = new HashMap<>();
        dfDict.put(FileSheetNamesMock.CADASTRAMENTO_1, Table.create());
        dfDict.put(FileSheetNamesMock.DISCIPLINAS_X_TURMAS_2, Table.create());
        dfDict.put(FileSheetNamesMock.PROFESSOR_X_DISCIPLINAS_3, Table.create());
        dfDict.put(FileSheetNamesMock.QUANTIDADE_AULAS_4, Table.create());
        dfDict.put(FileSheetNamesMock.FOLGA_5, Table.create());
        dfDict.put(FileSheetNamesMock.PREFERENCIAS_6, Table.create());
        dfDict.put(FileSheetNamesMock.RESTRICAO_IMPOSICAO_7, Table.create());
        dfDict.put(FileSheetNamesMock.RESTRICOES_PROIBICAO_8, Table.create());

        assertDoesNotThrow(() -> validator.validate(dfDict));
    }

    @Test
    void validate_MissingSheet_ThrowsIllegalArgumentException() {
        Map<String, Table> dfDict = new HashMap<>();
        dfDict.put(FileSheetNamesMock.CADASTRAMENTO_1, Table.create());
        dfDict.put(FileSheetNamesMock.DISCIPLINAS_X_TURMAS_2, Table.create());
        // Missing PROFESSOR_X_DISCIPLINAS_3
        dfDict.put(FileSheetNamesMock.QUANTIDADE_AULAS_4, Table.create());
        dfDict.put(FileSheetNamesMock.FOLGA_5, Table.create());
        dfDict.put(FileSheetNamesMock.PREFERENCIAS_6, Table.create());
        dfDict.put(FileSheetNamesMock.RESTRICAO_IMPOSICAO_7, Table.create());
        dfDict.put(FileSheetNamesMock.RESTRICOES_PROIBICAO_8, Table.create());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> validator.validate(dfDict));
        
        assertTrue(exception.getMessage().contains("A aba '" + FileSheetNamesMock.PROFESSOR_X_DISCIPLINAS_3 + "' não foi encontrada"));
    }

    @Test
    void validate_EmptyMap_ThrowsIllegalArgumentException() {
        Map<String, Table> dfDict = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> validator.validate(dfDict));
    }
}
