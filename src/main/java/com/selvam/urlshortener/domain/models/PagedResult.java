package com.selvam.urlshortener.domain.models;

import org.springframework.data.domain.Page;

import java.util.List;

public record PagedResult<T>(
        List<T> data,
        int pageNumber,
        int totalPages,
        Long totalElements,
        boolean isFirst,
        boolean isLast,
        boolean hasPrevious,
        boolean hasNext
) {
    public static <T> PagedResult<T> from(Page<T> page){
        return new PagedResult<>(
                page.getContent(),
                page.getNumber()+1, //to show 1-based page numbering
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast(),
                page.hasPrevious(),
                page.hasNext()
        );
    }
}
