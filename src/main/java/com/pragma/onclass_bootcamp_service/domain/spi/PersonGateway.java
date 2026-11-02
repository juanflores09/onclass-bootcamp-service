package com.pragma.onclass_bootcamp_service.domain.spi;

import com.pragma.onclass_bootcamp_service.domain.model.MostPopularEnrollmentAggregate;
import reactor.core.publisher.Mono;

public interface PersonGateway {

    Mono<MostPopularEnrollmentAggregate> getMostPopularEnrollments(String messageId);

}
