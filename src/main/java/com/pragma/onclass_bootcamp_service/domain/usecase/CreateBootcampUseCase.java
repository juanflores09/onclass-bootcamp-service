package com.pragma.onclass_bootcamp_service.domain.usecase;

import com.pragma.onclass_bootcamp_service.domain.api.CreateBootcampServicePort;
import com.pragma.onclass_bootcamp_service.domain.enums.TechnicalMessage;
import com.pragma.onclass_bootcamp_service.domain.exceptions.BusinessException;
import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.model.Capability;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGatewayPort;
import com.pragma.onclass_bootcamp_service.domain.spi.ReportGatewayPort;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.BootcampReportRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CreateBootcampUseCase implements CreateBootcampServicePort {

    private final BootcampPersistencePort bootcampPersistencePort;
    private final CapabilityGatewayPort capabilityValidatorGateway;
    private final ReportGatewayPort reportGatewayPort;

    @Override
    @Transactional
    public Mono<Bootcamp> createBootcamp(Bootcamp bootcamp, String messageId) {

        List<Long> capabilityIds = (bootcamp.getCapabilities() == null) ? Collections.emptyList()
                : bootcamp.getCapabilities().stream()
                  .map(Capability::getId)
                  .toList();
        return bootcampPersistencePort.existsByName(bootcamp.getName())
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BOOTCAMP_ALREADY_EXISTS)))
                .flatMap(ignored -> bootcampPersistencePort.save(bootcamp))
                .flatMap(savedbootcamp ->
                        capabilityValidatorGateway.saveCapabilityTechnologies(savedbootcamp.getId(), capabilityIds, messageId)
                                .thenReturn(savedbootcamp)
                                .onErrorResume(ex ->
                                        bootcampPersistencePort.deleteById(savedbootcamp.getId())
                                                .then(Mono.error(ex))
                                )
                )
                .flatMap(savedBootcamp -> {

                    if (savedBootcamp.getId() == null) {
                        return Mono.just(savedBootcamp);
                    }
                    // Trigger the asynchronous notification to the reporting service
                    return capabilityValidatorGateway.getTotalTechnologiesByCapabilities(savedBootcamp.getId(), messageId)
                            .defaultIfEmpty(0)
                            .map(totalTechnologies -> {
                                BootcampReportRequestDTO reportDto =
                                        new BootcampReportRequestDTO(
                                                savedBootcamp.getId(),
                                                savedBootcamp.getName(),
                                                savedBootcamp.getDescription(),
                                                savedBootcamp.getLaunchDate(),
                                                savedBootcamp.getDurationInWeeks(),
                                                capabilityIds.size(),
                                                totalTechnologies
                                        );
                                reportGatewayPort.notifyBootcampCreated(reportDto);
                                return savedBootcamp;
                            });
                });
    }
}
