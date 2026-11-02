package com.pragma.onclass_bootcamp_service.domain.api;

import com.pragma.onclass_bootcamp_service.domain.model.BootcampPopularity;
import reactor.core.publisher.Mono;

public interface GetMostPopularBootcampServicePort {

    Mono<BootcampPopularity> execute(String messageId);
}
