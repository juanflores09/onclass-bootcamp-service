package com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter;

import com.pragma.onclass_bootcamp_service.domain.enums.TechnicalMessage;
import com.pragma.onclass_bootcamp_service.domain.exceptions.BusinessException;
import com.pragma.onclass_bootcamp_service.domain.exceptions.TechnicalException;
import com.pragma.onclass_bootcamp_service.domain.model.Capability;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGateway;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.CapabilityIdsRequestDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CapabilityWebClientAdapter implements CapabilityGateway {


    private final WebClient webClient;

    @Override
    public Mono<Boolean> validateCapabilitiesExist(List<Long> ids, String messageId) {
        CapabilityIdsRequestDTO requestBody = new CapabilityIdsRequestDTO(ids);

        return webClient.post()
                .uri("/api/v1/capabilities/validate")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(Constants.X_MESSAGE_ID, messageId)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> buildErrorResponse(response, TechnicalMessage.INVALID_PARAMETERS))
                .onStatus(HttpStatusCode::is5xxServerError, response -> buildErrorResponse(response, TechnicalMessage.INTERNAL_ERROR_IN_ADAPTERS))
                .bodyToMono(Boolean.class)
                .doOnNext(response -> log.info("Received API response for messageId {}: {}", messageId, response))
                .defaultIfEmpty(false);
    }

    @Override
    public Flux<Capability> getTechnologiesByIds(List<Long> ids) {
        CapabilityIdsRequestDTO requestBody = new CapabilityIdsRequestDTO(ids);

        return webClient.post()
                .uri("/api/v1/capabilities/find")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(Constants.X_MESSAGE_ID, "")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> buildErrorResponse(response, TechnicalMessage.INVALID_PARAMETERS))
                .onStatus(HttpStatusCode::is5xxServerError, response -> buildErrorResponse(response, TechnicalMessage.INTERNAL_ERROR_IN_ADAPTERS))
                .bodyToFlux(Capability.class);
    }

    private Mono<Throwable> buildErrorResponse(ClientResponse response, TechnicalMessage technicalMessage) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty(Constants.NO_ADITIONAL_ERROR_DETAILS)
                .flatMap(errorBody -> {
                    log.error(Constants.STRING_ERROR_BODY_DATA, errorBody);
                    return Mono.error(
                            response.statusCode().is5xxServerError() ?
                                    new TechnicalException(technicalMessage):
                                    new BusinessException(technicalMessage));
                });
    }
}

