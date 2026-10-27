package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;
import java.util.List;

public record BootcampResponseDTO(
        Long id,
        String name,
        String description,
        LocalDate launchDate,
        Integer durationInWeeks,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<CapabilityResponseDTO> capabilities
) {

    public BootcampResponseDTO(Long id,  String name, String description, LocalDate launchDate, Integer durationInWeeks) {
        this(id, name, description, launchDate, durationInWeeks, List.of());
    }
}
