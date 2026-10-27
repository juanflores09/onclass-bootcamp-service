package com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.repository;

import com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.entity.BootcampEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SpringDataBootcampRepository extends ReactiveCrudRepository<BootcampEntity, Long> {

    Mono<Boolean> existsByName(String name);

    @Query("""
        SELECT b.id, b.name, b.description, b.launch_date, b.duration_in_weeks, COUNT(bc.capability_id) as cap_count
        FROM bootcamp b
        LEFT JOIN bootcamp_capability bc ON b.id = bc.bootcamp_id
        GROUP BY b.id, b.name, b.description, b.launch_date, b.duration_in_weeks
        ORDER BY 
            CASE WHEN :sortBy = 'capabilitiesCount' AND :direction = 'ASC' THEN COUNT(bc.capability_id) END ASC,
            CASE WHEN :sortBy = 'capabilitiesCount' AND :direction = 'DESC' THEN COUNT(bc.capability_id) END DESC,
            CASE WHEN :sortBy = 'name' AND :direction = 'ASC' THEN b.name END ASC,
            CASE WHEN :sortBy = 'name' AND :direction = 'DESC' THEN b.name END DESC
        LIMIT :limit OFFSET :offset
        """)
    Flux<BootcampEntity> findAllPagedAndSorted(
            @Param("sortBy") String sortBy,
            @Param("direction") String direction,
            @Param("limit") int limit,
            @Param("offset") long offset
    );
}
