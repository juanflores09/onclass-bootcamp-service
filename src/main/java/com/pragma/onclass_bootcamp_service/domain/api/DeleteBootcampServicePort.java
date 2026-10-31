package com.pragma.onclass_bootcamp_service.domain.api;

import reactor.core.publisher.Mono;

public interface DeleteBootcampServicePort {

    Mono<Void> deleteBootcamp(Long bootcampId, String messageId);
}
