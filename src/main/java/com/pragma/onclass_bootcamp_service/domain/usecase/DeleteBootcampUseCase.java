package com.pragma.onclass_bootcamp_service.domain.usecase;

import com.pragma.onclass_bootcamp_service.domain.api.DeleteBootcampServicePort;
import com.pragma.onclass_bootcamp_service.domain.enums.TechnicalMessage;
import com.pragma.onclass_bootcamp_service.domain.exceptions.BusinessException;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGatewayPort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DeleteBootcampUseCase implements DeleteBootcampServicePort {

    private final BootcampPersistencePort bootcampPersistencePort;
    private final CapabilityGatewayPort capabilityGateway;

    @Override
    public Mono<Void> deleteBootcamp(Long bootcampId, String messageId) {

        return bootcampPersistencePort.findById(bootcampId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BOOTCAMP_NOT_FOUND)))
                .flatMap(bootcamp ->
                        capabilityGateway.deleteCapabilitiesByBootcampId(bootcampId, messageId)
                                .then(bootcampPersistencePort.deleteById(bootcampId))
                );
    }
}
