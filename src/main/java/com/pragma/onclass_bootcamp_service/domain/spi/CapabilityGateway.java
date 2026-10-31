package com.pragma.onclass_bootcamp_service.domain.spi;

import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.BootcampCapabilitiesResponseDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.BootcampIdsRequestDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilityGateway {

    Flux<BootcampCapabilitiesResponseDTO> getCapabilitiesByBootcampId(BootcampIdsRequestDTO bootcampId, String messageId);

    Mono<Void> saveCapabilityTechnologies(Long capabilityId, List<Long> technologyIds, String messageId);

}
