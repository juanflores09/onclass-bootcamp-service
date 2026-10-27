package com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.repository;

import com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.entity.BootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SpringDataBootcampRepository extends ReactiveCrudRepository<BootcampEntity, Long> {

    Mono<Boolean> existsByName(String name);
}
