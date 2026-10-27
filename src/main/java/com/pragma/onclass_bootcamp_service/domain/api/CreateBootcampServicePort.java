package com.pragma.onclass_bootcamp_service.domain.api;

import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface CreateBootcampServicePort {

    Mono<Bootcamp> createBootcamp(Bootcamp bootcamp, String messageId);

}
