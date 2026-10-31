package com.pragma.onclass_bootcamp_service.domain.api;

import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.model.PageResult;
import reactor.core.publisher.Mono;

public interface FindBootcampsServicePort {

    Mono<PageResult<Bootcamp>> listBootcamps(int page, int size, String sortBy, String direction, String messageId);
}
