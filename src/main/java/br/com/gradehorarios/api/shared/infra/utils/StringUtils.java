package br.com.gradehorarios.api.shared.infra.utils;

import java.text.Normalizer;
import java.util.Optional;
import java.util.UUID;

public class StringUtils {

    public static String toCleanCamelCase(String text) {
        if (text == null || text.isBlank()) return "";

        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("[^\\p{Alpha}0-9\\s]", "")
                .trim();
        
        String[] parts = normalized.split("\\s+");
        StringBuilder camelCase = new StringBuilder();
        
        for (String part : parts) {
            if (!part.isEmpty()) {
                camelCase.append(Character.toUpperCase(part.charAt(0)));
                camelCase.append(part.substring(1).toLowerCase());
            }
        }
        return camelCase.toString();
    }

    public static String extractExtension(String originalFilename) {
        if (originalFilename == null) return "";
        int lastDot = originalFilename.lastIndexOf(".");
        return (lastDot > 0) ? originalFilename.substring(lastDot) : "";
    }

    public static String generateRandomName(Optional<String> prefix, Optional<String> sufix) {
        String prefixString = prefix.orElse("");
        String sufixString = sufix.orElse("");
        
        return prefixString + UUID.randomUUID().toString() + sufixString;
    }

    public static String generateRandomName(String prefix, String sufix) {
        return prefix + UUID.randomUUID().toString() + sufix;
    }

    public static String generateRandomName() {
        return UUID.randomUUID().toString(); 
    }
    


}