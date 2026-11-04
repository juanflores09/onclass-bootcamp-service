package com.pragma.onclass_bootcamp_service.domain.spi;

import com.pragma.onclass_bootcamp_service.domain.model.BootcampCapabilityAggregate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilityGatewayPort {

    Flux<BootcampCapabilityAggregate> getCapabilitiesByBootcampId(List<Long> bootcampIds, String messageId);

    Mono<Void> saveCapabilityTechnologies(Long capabilityId, List<Long> technologyIds, String messageId);

    Mono<Void> deleteCapabilitiesByBootcampId(Long bootcampId, String messageId);

    Mono<Integer> getTotalTechnologiesByCapabilities(Long bootcampId, String messageId);

}
