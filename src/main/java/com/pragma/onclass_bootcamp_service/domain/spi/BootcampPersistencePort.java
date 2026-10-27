package com.pragma.onclass_bootcamp_service.domain.spi;

import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcampPersistencePort {

    Mono<Boolean> existsByName(String name);
    Mono<Bootcamp> save(Bootcamp bootcamp);
    Flux<Bootcamp> findAllPagedAndSorted(String sortBy, String direction, int limit, long offset);
    Flux<Long> findByCapabilityId(Long capabilityId);
    Mono<Long> countTotal();
}
