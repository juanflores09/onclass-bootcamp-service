package com.pragma.onclass_bootcamp_service.domain.spi;

import com.pragma.onclass_bootcamp_service.domain.model.Capability;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilityGateway {
    Mono<Boolean> validateCapabilitiesExist(List<Long> ids, String messageId);
    Flux<Capability> getTechnologiesByIds(List<Long> technologyIds);
}
