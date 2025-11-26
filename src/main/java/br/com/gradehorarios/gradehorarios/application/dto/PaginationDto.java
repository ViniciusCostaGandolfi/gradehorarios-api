package br.com.gradehorarios.gradehorarios.application.dto;

import org.springframework.data.domain.Page;
import java.util.List;

public record PaginationDto<T>(
    int currentPage,
    int totalPages,
    int pageSize,
    long totalCount,
    boolean hasPrevious,
    boolean hasNext,
    List<T> data,
    String previousPageLink,
    String nextPageLink
) {
    public static <T> PaginationDto<T> fromPage(Page<T> page, String baseUrl) {
        String prevPageLink = page.hasPrevious() ? baseUrl + "?page=" + (page.getNumber() - 1) + "&size=" + page.getSize() : null;
        String nextPageLink = page.hasNext() ? baseUrl + "?page=" + (page.getNumber() + 1) + "&size=" + page.getSize() : null;

        return new PaginationDto<>(
            page.getNumber(),
            page.getTotalPages(),
            page.getSize(),
            page.getTotalElements(),
            page.hasPrevious(),
            page.hasNext(),
            page.getContent(),
            prevPageLink,
            nextPageLink
        );
    }
}
