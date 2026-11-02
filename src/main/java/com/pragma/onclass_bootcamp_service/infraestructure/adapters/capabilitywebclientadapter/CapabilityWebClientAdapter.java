package com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter;

import com.pragma.onclass_bootcamp_service.domain.exceptions.ExternalServiceException;
import com.pragma.onclass_bootcamp_service.domain.model.BootcampCapabilityAggregate;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGateway;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.BootcampCapabilitiesRequestDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.BootcampCapabilitiesResponseDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.BootcampIdsRequestDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.util.Constants;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.ApiResponseDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper.CapabilityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class CapabilityWebClientAdapter implements CapabilityGateway {


    private final WebClient webClient;
    private final CapabilityMapper capabilityMapper;

    public CapabilityWebClientAdapter(@Qualifier("capabilityWebClient") WebClient webClient, CapabilityMapper capabilityMapper) {
        this.webClient = webClient;
        this.capabilityMapper = capabilityMapper;
    }

    @Override
    public Flux<BootcampCapabilityAggregate> getCapabilitiesByBootcampId(List<Long> bootcampsIds, String messageId) {

        return webClient.post()
                .uri("/api/v1/capabilities/find-by-bootcamps")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(Constants.X_MESSAGE_ID, messageId != null ? messageId : "")
                .bodyValue(new BootcampIdsRequestDTO(bootcampsIds))
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(ApiResponseDTO.class)
                                .flatMap(errorResponse -> Mono.error(new ExternalServiceException(errorResponse)))
                )
                .bodyToFlux(BootcampCapabilitiesResponseDTO.class)
                .map(capabilityMapper::dtoToBootcampCapabilityAggregate);
    }

    @Override
    public Mono<Void> saveCapabilityTechnologies(Long capabilityId, List<Long> technologyIds, String messageId) {
        BootcampCapabilitiesRequestDTO requestBody = new BootcampCapabilitiesRequestDTO(capabilityId, technologyIds);

        return webClient.post()
                .uri("/api/v1/capabilities/bootcamp-capabilities")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(Constants.X_MESSAGE_ID, messageId)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(ApiResponseDTO.class)
                                .flatMap(errorResponse -> Mono.error(new ExternalServiceException(errorResponse)))
                )
                .bodyToMono(Void.class);
    }

    @Override
    public Mono<Void> deleteCapabilitiesByBootcampId(Long bootcampId, String messageId) {
        return webClient.delete()
                .uri("/api/v1/capabilities/bootcamp/{bootcampId}", bootcampId)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(Constants.X_MESSAGE_ID, messageId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(ApiResponseDTO.class)
                                .flatMap(errorResponse -> Mono.error(new ExternalServiceException(errorResponse)))
                )
                .bodyToMono(Void.class);
    }
}

