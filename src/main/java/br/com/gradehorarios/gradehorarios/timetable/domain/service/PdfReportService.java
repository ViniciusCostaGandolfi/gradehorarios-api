package br.com.gradehorarios.gradehorarios.timetable.domain.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openpdf.pdf.ITextRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import br.com.gradehorarios.gradehorarios.timetable.infra.dto.SolverResponseDto;


@Service
public class PdfReportService {
    
    @Autowired
    private SpringTemplateEngine templateEngine;

    public byte[] generateTeacherReport(SolverResponseDto sollutionDto) {

        return generatePdfReport(sollutionDto.getTeachers(), "Professor");
    }

    public byte[] generateClassroomReport(SolverResponseDto solutionDto) {
        return generatePdfReport(solutionDto.getClassrooms(), "Sala");
    }

    private <T> byte[] generatePdfReport(List<T> items, String titlePrefix) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            int maxClasses = calculateMaxSlots(items);
            if (maxClasses == 0) maxClasses = 5;


            Context context = new Context();
            context.setVariable("items", items);
            context.setVariable("titlePrefix", titlePrefix);
            context.setVariable("maxClasses", maxClasses);
            String htmlContent = templateEngine.process("pdf/timetable_report", context);
            Document doc = Jsoup.parse(htmlContent, "UTF-8");
            doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
            String xhtml = doc.html();
            
            ITextRenderer renderer = new ITextRenderer();
            
            renderer.setDocumentFromString(xhtml);
            
            renderer.layout();
            
            renderer.createPDF(os);

            return os.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar PDF HTML", e);
        }
    }

    private <T> int calculateMaxSlots(List<T> items) {
        if (items == null || items.isEmpty()) {
            return 0;
        }

        int max = 0;
        String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday"};

        for (T item : items) {
            for (String day : days) {
                try {
                    Field field = item.getClass().getDeclaredField(day);
                    field.setAccessible(true);
                    List<?> dayList = (List<?>) field.get(item);
                    
                    if (dayList != null && dayList.size() > max) {
                        max = dayList.size();
                    }
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
                }
            }
        }
        return max;
    }
}
