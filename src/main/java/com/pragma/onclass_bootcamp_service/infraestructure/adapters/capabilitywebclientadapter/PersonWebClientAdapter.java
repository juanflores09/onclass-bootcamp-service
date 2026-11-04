package com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter;

import com.pragma.onclass_bootcamp_service.domain.exceptions.ExternalServiceException;
import com.pragma.onclass_bootcamp_service.domain.model.MostPopularEnrollmentAggregate;
import com.pragma.onclass_bootcamp_service.domain.spi.PersonGatewayPort;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.MostPopularEnrollmentDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.ApiResponseDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper.BootcampMapper;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.util.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class PersonWebClientAdapter implements PersonGatewayPort {

    private final WebClient webClient;
    private final BootcampMapper bootcampMapper;

    public PersonWebClientAdapter(@Qualifier("personWebClient") WebClient webClient, BootcampMapper bootcampMapper) {
        this.webClient = webClient;
        this.bootcampMapper = bootcampMapper;
    }

    @Override
    public Mono<MostPopularEnrollmentAggregate> getMostPopularEnrollments(String messageId) {
        return webClient.get()
                .uri("/api/v1/persons/reports/most-popular-enrollments")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(Constants.X_MESSAGE_ID, messageId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                    response.bodyToMono(ApiResponseDTO.class)
                            .flatMap(errorResponse -> Mono.error(new ExternalServiceException(errorResponse)))
                )
                .bodyToMono(MostPopularEnrollmentDTO.class)
                .map(bootcampMapper::dtoToMostPopularEnrollmentAggregate);
    }
}
