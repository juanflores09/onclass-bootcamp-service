package com.pragma.onclass_bootcamp_service.domain.usecase;

import com.pragma.onclass_bootcamp_service.domain.api.FindBootcampsServicePort;
import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.model.PageResult;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RequiredArgsConstructor
public class ListBootcampUseCase implements FindBootcampsServicePort {

    private final BootcampPersistencePort bootcampPersistencePort;
    private final CapabilityGateway capabilityGateway;

    @Override
    public Mono<PageResult<Bootcamp>> listBootcamps(int page, int size, String sortBy, String direction) {
        long offset = (long) page * size;

        Flux<Bootcamp> bootcampsFlux =  bootcampPersistencePort.findAllPagedAndSorted(sortBy, direction, size, offset)
                .flatMap(bootcamp ->
                    bootcampPersistencePort.findByCapabilityId(bootcamp.getId())
                            .collectList()
                            .flatMap(techIds -> {

                                if (techIds.isEmpty()) {
                                    bootcamp.setCapabilities(Collections.emptyList());
                                    return Mono.just(bootcamp);
                                }

                                // Call the microservice capability-service with the IDs to retrieve ID and Name
                                return capabilityGateway.getTechnologiesByIds(techIds)
                                        .collectList()
                                        .map(capabilityList -> {
                                            bootcamp.setCapabilities(capabilityList);
                                            return bootcamp;
                                        });
                            })
                );
        // Gets the total number of items in the database
        Mono<Long> totalCountMono = bootcampPersistencePort.countTotal();

        // Combine both results into a domain PageResult.
        return Mono.zip(bootcampsFlux.collectList(), totalCountMono)
                .map(tuple -> new PageResult<>(tuple.getT1(), tuple.getT2()));
    }
}
