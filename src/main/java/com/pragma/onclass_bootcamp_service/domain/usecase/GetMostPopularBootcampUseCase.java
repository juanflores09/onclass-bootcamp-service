package com.pragma.onclass_bootcamp_service.domain.usecase;

import com.pragma.onclass_bootcamp_service.domain.api.GetMostPopularBootcampServicePort;
import com.pragma.onclass_bootcamp_service.domain.model.BootcampPopularity;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGateway;
import com.pragma.onclass_bootcamp_service.domain.spi.PersonGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class GetMostPopularBootcampUseCase implements GetMostPopularBootcampServicePort {

    private final BootcampPersistencePort bootcampPersistencePort;
    private final PersonGateway personGateway;
    private final CapabilityGateway capabilityGateway;

     @Override
    public Mono<BootcampPopularity> execute(String messageId) {
        return personGateway.getMostPopularEnrollments(messageId)
                .flatMap(popularBootcamp ->
                        Mono.zip(
                                capabilityGateway.getCapabilitiesByBootcampId(List.of(popularBootcamp.getBootcampId()), messageId)
                                        .collectList(),
                                bootcampPersistencePort.findById(popularBootcamp.getBootcampId())
                        ).map(tuple -> {
                            var capabilities = tuple.getT1();
                            var bootcampDetail = tuple.getT2();

                            return new BootcampPopularity(
                                    bootcampDetail.getId(),
                                    bootcampDetail.getName(),
                                    bootcampDetail.getDescription(),
                                    bootcampDetail.getLaunchDate(),
                                    bootcampDetail.getDurationInWeeks(),
                                    popularBootcamp.getTotalEnrolled(),
                                    capabilities.stream()
                                    .flatMap(dto -> dto.getCapabilities().stream())
                                    .toList(),
                                    popularBootcamp.getEnrolledPersons()
                            );
                        })

                );
    }

}
