package com.pragma.onclass_bootcamp_service.domain.spi;

import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilityGateway {
    Mono<Boolean> validateCapabilitiesExist(List<Long> ids, String messageId);
}
