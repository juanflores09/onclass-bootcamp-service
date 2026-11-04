package com.pragma.onclass_bootcamp_service.domain.usecase;

import com.pragma.onclass_bootcamp_service.domain.api.FindBootcampsServicePort;
import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.model.BootcampCapabilityAggregate;
import com.pragma.onclass_bootcamp_service.domain.model.Capability;
import com.pragma.onclass_bootcamp_service.domain.model.PageResult;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGatewayPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ListBootcampUseCase implements FindBootcampsServicePort {

    private final BootcampPersistencePort bootcampPersistencePort;
    private final CapabilityGatewayPort capabilityGateway;

    @Override
    public Mono<PageResult<Bootcamp>> listBootcamps(int page, int size, String sortBy, String direction, String messageId) {
        long offset = (long) page * size;

        Flux<Bootcamp> bootcampsFlux =  bootcampPersistencePort.findAllPagedAndSorted(sortBy, direction, size, offset)
                .collectList()
                .flatMapMany(bootcamps -> {

                    if (bootcamps.isEmpty()) {
                        return Flux.empty();
                    }

                    List<Long> bootcampIds = bootcamps.stream()
                            .map(Bootcamp::getId)
                            .toList();

                    return capabilityGateway.getCapabilitiesByBootcampId(bootcampIds, messageId)
                            .collectMap(
                                    BootcampCapabilityAggregate::getBootcampId,
                                    BootcampCapabilityAggregate::getCapabilities
                            )
                            .flatMapMany(techMap ->
                                    Flux.fromIterable(bootcamps)
                                            .map(bootcamp -> {
                                                List<Capability> capabilityList = techMap.getOrDefault(bootcamp.getId(), Collections.emptyList());
                                                bootcamp.setCapabilities(capabilityList);
                                                return bootcamp;
                                            })

                            );
                });

        // Gets the total number of items in the database
        Mono<Long> totalCountMono = bootcampPersistencePort.countTotal();

        // Combine both results into a domain PageResult.
        return Mono.zip(bootcampsFlux.collectList(), totalCountMono)
                .map(tuple -> new PageResult<>(tuple.getT1(), tuple.getT2()));
    }

    @Override
    public Flux<Bootcamp> findBootcampsByIds(List<Long> ids, String messageId) {
        if (ids == null || ids.isEmpty()) {
            return Flux.empty();
        }
        return bootcampPersistencePort.findByIds(ids);
    }
}
