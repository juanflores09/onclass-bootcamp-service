package com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto;

import java.util.List;

public record BootcampCapabilitiesResponseDTO(
        Long bootcampId,
        List<CapabilityDTO> capabilities
) {
}
