package com.pragma.onclass_bootcamp_service.domain.usecase;

import com.pragma.onclass_bootcamp_service.domain.api.CreateBootcampServicePort;
import com.pragma.onclass_bootcamp_service.domain.enums.TechnicalMessage;
import com.pragma.onclass_bootcamp_service.domain.exceptions.BusinessException;
import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateBootcampUseCase implements CreateBootcampServicePort {

    private final BootcampPersistencePort bootcampPersistencePort;
    private final CapabilityGateway capabilityValidatorGateway;

    @Override
    @Transactional
    public Mono<Bootcamp> createBootcamp(Bootcamp bootcamp, String messageId) {
        return bootcampPersistencePort.existsByName(bootcamp.getName())
                .filter(exists -> !exists)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BOOTCAMP_ALREADY_EXISTS)))
                .flatMap(ignored -> capabilityValidatorGateway.validateCapabilitiesExist(bootcamp.getCapabilities(), messageId))
                .filter(Boolean.TRUE::equals)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.CAPABILITY_NOT_FOUND)))
                .flatMap(ignored -> bootcampPersistencePort.save(bootcamp));
    }
}
