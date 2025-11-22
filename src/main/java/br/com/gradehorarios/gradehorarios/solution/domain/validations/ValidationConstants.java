package br.com.gradehorarios.gradehorarios.solution.domain.validations;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ValidationConstants {
    
    public static final List<String> PERIODOS_INTERVALO_VALIDOS = IntStream.range(1, 30)
            .mapToObj(i -> "Entre a " + i + "ª e " + (i + 1) + "ª aula")
            .collect(Collectors.toList());

    public static final List<String> INTERVALOS_VALIDOS = Arrays.asList(
            "Intervalo", "Almoço", "Jantar", "Café", "Lanche", "Descanso", "Pausa", "Recreio"
    );
}
