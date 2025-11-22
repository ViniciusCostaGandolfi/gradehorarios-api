package br.com.gradehorarios.gradehorarios.solution.application.service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class FileNameGenerator {
    public static String generateFileName(
        MultipartFile file,
        String institutionName, 
        boolean hasInput
    ) {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String typePart = hasInput ? "Input" : "Output";

        String normalizedName = Normalizer.normalize(institutionName, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") 
                .replaceAll("[^\\p{Alpha}0-9\\s]", "") 
                .trim();
        
        String[] parts = normalizedName.split("\\s+");
        StringBuilder camelCaseName = new StringBuilder();
        for (String part : parts) {
            if (part.length() > 0) {
                camelCaseName.append(Character.toUpperCase(part.charAt(0)));
                camelCaseName.append(part.substring(1).toLowerCase());
            }
        }
        String cleanedInstitutionName = camelCaseName.toString();

        String uuidPart = UUID.randomUUID().toString().substring(0, 8);
        
        String extension = "";
        String originalName = file.getOriginalFilename();
        int lastDot = originalName.lastIndexOf(".");
        if (lastDot > 0) {
            extension = originalName.substring(lastDot);
        }

        return String.format("%s_%s_%s_%s%s",
            cleanedInstitutionName,
            datePart,
            typePart,
            uuidPart,
            extension
        );
    }

}
