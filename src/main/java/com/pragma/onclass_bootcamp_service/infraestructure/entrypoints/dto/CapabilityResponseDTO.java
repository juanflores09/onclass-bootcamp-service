package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto;

import java.util.List;

public record CapabilityResponseDTO(
        Long id,
        String name,
        List<TechnologyResponseDTO> technologies
) {


}
