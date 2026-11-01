package com.pragma.onclass_bootcamp_service.domain.usecase;

import com.pragma.onclass_bootcamp_service.domain.api.FindBootcampsServicePort;
import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.model.Capability;
import com.pragma.onclass_bootcamp_service.domain.model.PageResult;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGateway;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.BootcampCapabilitiesResponseDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.BootcampIdsRequestDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.CapabilityDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper.CapabilityMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ListBootcampUseCase implements FindBootcampsServicePort {

    private final BootcampPersistencePort bootcampPersistencePort;
    private final CapabilityGateway capabilityGateway;
    private final CapabilityMapper capabilityMapper;

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

                    BootcampIdsRequestDTO capabilityIdsRequestDTO = new BootcampIdsRequestDTO(bootcampIds);

                    return capabilityGateway.getCapabilitiesByBootcampId(capabilityIdsRequestDTO, messageId)
                            .collectMap(
                                    BootcampCapabilitiesResponseDTO::bootcampId,
                                    BootcampCapabilitiesResponseDTO::capabilities
                            )
                            .flatMapMany(techMap ->
                                    Flux.fromIterable(bootcamps)
                                            .map(bootcamp -> {
                                                List<CapabilityDTO> capabilityDTOs = techMap.getOrDefault(bootcamp.getId(), Collections.emptyList());

                                                List<Capability> domainCapabilities = capabilityDTOs.stream()
                                                        .map(capabilityMapper::capabilityDtoToCapability)
                                                        .toList();

                                                bootcamp.setCapabilities(domainCapabilities);
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
