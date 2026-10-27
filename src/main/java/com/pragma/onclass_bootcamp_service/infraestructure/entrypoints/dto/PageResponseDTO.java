package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto;

import java.util.List;

public record PageResponseDTO<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
) {
}
