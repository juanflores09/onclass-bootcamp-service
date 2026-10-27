package com.pragma.onclass_bootcamp_service.domain.spi;

import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface BootcampPersistencePort {

    Mono<Boolean> existsByName(String name);
    Mono<Bootcamp> save(Bootcamp bootcamp);
}
