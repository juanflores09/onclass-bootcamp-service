package com.pragma.onclass_bootcamp_service.domain.api;

import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.model.PageResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface FindBootcampsServicePort {

    Mono<PageResult<Bootcamp>> listBootcamps(int page, int size, String sortBy, String direction, String messageId);
    Flux<Bootcamp> findBootcampsByIds(List<Long> ids, String messageId);
}
