package br.com.gradehorarios.gradehorarios.timetable.domain.service;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import br.com.gradehorarios.gradehorarios.timetable.infra.dto.SolverResponseDto;
import br.com.gradehorarios.gradehorarios.timetable.infra.dto.TimetableBaseDto;


@ExtendWith(MockitoExtension.class)
public class PdfReportServiceTest {

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private PdfReportService service;

    @Captor
    private ArgumentCaptor<Context> contextCaptor;

    private SolverResponseDto solverResponseDto;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        var baseClasses = List.of("Aula 1", "Aula 2", "Aula 3", "Aula 4", "Aula 5");

        var teachers = IntStream.range(0, 10)
            .mapToObj(i -> new TimetableBaseDto(
                "Teacher " + i,
                baseClasses, baseClasses, baseClasses, baseClasses, baseClasses, baseClasses, baseClasses
            ))
            .toList();

        var classrooms = IntStream.range(0, 10)
            .mapToObj(i -> new TimetableBaseDto(
                "Classroom " + i,
                baseClasses, baseClasses, baseClasses, baseClasses, baseClasses, baseClasses, baseClasses
            ))
            .toList();
            
        this.solverResponseDto = new SolverResponseDto(
            teachers,
            classrooms
        );

    }



    @Test
    void testGenerateClassroomReport() {
        String mockHtml = "<html><body><h1>Relatorio Classroom</h1><p>Teste</p></body></html>";

        when(templateEngine.process(eq("pdf/timetable_report"), any(Context.class)))
            .thenReturn(mockHtml);

        byte[] pdfBytes = service.generateClassroomReport(solverResponseDto);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);


        verify(templateEngine).process(eq("pdf/timetable_report"), contextCaptor.capture());

        var context = contextCaptor.getValue();

        assertEquals("Sala", context.getVariable("titlePrefix"));

        List<?> items = (List<?>) context.getVariable("items");
        assertEquals(10, items.size());
        
        assertEquals(5, context.getVariable("maxClasses"));
    }


    @Test
    void testGenerateTeacherReport() {
        String mockHtml = "<html><body><h1>Relatorio Teacher</h1><p>Teste</p></body></html>";

        when(templateEngine.process(eq("pdf/timetable_report"), any(Context.class)))
            .thenReturn(mockHtml);

        byte[] pdfBytes = service.generateClassroomReport(solverResponseDto);

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);


        verify(templateEngine).process(eq("pdf/timetable_report"), contextCaptor.capture());

        var context = contextCaptor.getValue();

        assertEquals("Sala", context.getVariable("titlePrefix"));

        List<?> items = (List<?>) context.getVariable("items");
        assertEquals(10, items.size());
        
        assertEquals(5, context.getVariable("maxClasses"));
    }


}
