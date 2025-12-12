package br.com.gradehorarios.gradehorarios.shared.infra.utils;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;


class StringUtilsTest {

    @Test
    void returnsEmptyForNullAndBlank() {
        assertEquals("", StringUtils.toCleanCamelCase(null));
        assertEquals("", StringUtils.toCleanCamelCase(""));
        assertEquals("", StringUtils.toCleanCamelCase("   "));
    }

    @Test
    void basicWordsBecomeCamelCase() {
        assertEquals("HelloWorld", StringUtils.toCleanCamelCase("hello world"));
        assertEquals("HelloWorld", StringUtils.toCleanCamelCase(" Hello   world "));
    }

    @Test
    void removesAccentsAndSpecialCharacters() {
        assertEquals("AcaoDeSaoPaulo", StringUtils.toCleanCamelCase("ação de São Paulo"));
        assertEquals("AcaoDeSaoPaulo", StringUtils.toCleanCamelCase("ação -- de São Paulo!!!"));
    }

    @Test
    void preservesNumbersAndHandlesMixedParts() {
        assertEquals("123abcDef", StringUtils.toCleanCamelCase("123abc def"));
        assertEquals("Helloworld123", StringUtils.toCleanCamelCase("hello-world_123"));
    }

    @Test
    void handlesMixedCaseAndMultipleSpaces() {
        assertEquals("MixedCase", StringUtils.toCleanCamelCase("  mIXeD    CaSe  "));
    }

    @Test
    void onlyPunctuationYieldsEmpty() {
        assertEquals("", StringUtils.toCleanCamelCase("!!!"));
        assertEquals("", StringUtils.toCleanCamelCase(" - _ "));
    }
}