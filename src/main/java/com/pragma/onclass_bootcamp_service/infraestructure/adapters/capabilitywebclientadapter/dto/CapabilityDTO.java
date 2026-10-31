package com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto;

import java.util.List;

public record CapabilityDTO(
        Long id,
        String name,
        String description,
        List<TechnologyDTO> technologies
){
}
