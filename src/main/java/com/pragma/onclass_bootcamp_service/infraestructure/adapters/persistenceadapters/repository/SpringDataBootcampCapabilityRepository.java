package com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.repository;

import com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.entity.BootcampCapabilityEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SpringDataBootcampCapabilityRepository extends ReactiveCrudRepository<BootcampCapabilityEntity, Long> {

    @Query("SELECT capability_id FROM bootcamp_capability WHERE bootcamp_id = :bootcampId")
    Flux<Long> findByBootcampId(@Param("bootcampId") Long bootcampId);

}
