package com.pragma.onclass_bootcamp_service.domain.usecase;

import com.pragma.onclass_bootcamp_service.domain.api.CreateBootcampServicePort;
import com.pragma.onclass_bootcamp_service.domain.enums.TechnicalMessage;
import com.pragma.onclass_bootcamp_service.domain.exceptions.BusinessException;
import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.model.Capability;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class CreateBootcampUseCase implements CreateBootcampServicePort {

    private final BootcampPersistencePort bootcampPersistencePort;
    private final CapabilityGateway capabilityValidatorGateway;

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
                .flatMap(savedCapability ->
                        capabilityValidatorGateway.saveCapabilityTechnologies(savedCapability.getId(), capabilityIds, messageId)
                                .thenReturn(savedCapability)
                                .onErrorResume(ex ->
                                        bootcampPersistencePort.deleteById(savedCapability.getId())
                                                .then(Mono.error(ex))
                                )
                );
    }
}
