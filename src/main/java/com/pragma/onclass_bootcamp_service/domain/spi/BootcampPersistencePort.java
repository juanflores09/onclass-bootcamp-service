package com.pragma.onclass_bootcamp_service.domain.spi;

import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampPersistencePort {

    Mono<Bootcamp> findById(Long bootcampId);
    Flux<Bootcamp> findByIds(List<Long> bootcampIds);
    Mono<Boolean> existsByName(String name);
    Mono<Bootcamp> save(Bootcamp bootcamp);
    Flux<Bootcamp> findAllPagedAndSorted(String sortBy, String direction, int limit, long offset);
    Mono<Long> countTotal();
    Mono<Void> deleteById(Long id);
}
