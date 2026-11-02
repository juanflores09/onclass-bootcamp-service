package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.handler;

import com.pragma.onclass_bootcamp_service.domain.api.CreateBootcampServicePort;
import com.pragma.onclass_bootcamp_service.domain.api.DeleteBootcampServicePort;
import com.pragma.onclass_bootcamp_service.domain.api.FindBootcampsServicePort;
import com.pragma.onclass_bootcamp_service.domain.api.GetMostPopularBootcampServicePort;
import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.model.BootcampPopularity;
import com.pragma.onclass_bootcamp_service.domain.model.PageResult;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.BootcampIdsRequestDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.util.Constants;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.BootcampPopularityResponseDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.BootcampResponseDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.CreateBootcampRequestDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper.BootcampMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BootcampHandlerTest {

    @Mock
    private CreateBootcampServicePort createBootcampServicePort;
    @Mock
    private FindBootcampsServicePort findBootcampsServicePort;
    @Mock
    private DeleteBootcampServicePort deleteBootcampServicePort;
    @Mock
    private GetMostPopularBootcampServicePort getMostPopularBootcampServicePort;
    @Mock
    private BootcampMapper bootcampMapper;
    @Mock
    private Validator validator;

    @Mock
    private ServerRequest serverRequest;

    @Mock
    private ServerRequest.Headers headers;

    @InjectMocks
    private BootcampHandler bootcampHandler;

    @BeforeEach
    void setUp() {
        lenient().when(serverRequest.headers()).thenReturn(headers);
        lenient().when(headers.firstHeader(Constants.X_MESSAGE_ID)).thenReturn("msg-12345");
    }

    @Test
    void listBootcamps_Success() {
        // Given
        PageResult<Bootcamp> pageResult = new PageResult<>(List.of(mock(Bootcamp.class)), 1L);
        BootcampResponseDTO responseDTO = mock(BootcampResponseDTO.class);

        when(serverRequest.queryParam("page")).thenReturn(java.util.Optional.empty());
        when(serverRequest.queryParam("size")).thenReturn(java.util.Optional.empty());
        when(serverRequest.queryParam("sortBy")).thenReturn(java.util.Optional.empty());
        when(serverRequest.queryParam("direction")).thenReturn(java.util.Optional.empty());

        when(findBootcampsServicePort.listBootcamps(0, 10, "name", "ASC", "msg-12345"))
                .thenReturn(Mono.just(pageResult));
        when(bootcampMapper.bootcampToBootcampResponseDTO(any(), any()))
                .thenReturn(responseDTO);

        // When
        Mono<ServerResponse> responseMono = bootcampHandler.listBootcamps(serverRequest);

        // Then
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        verify(findBootcampsServicePort, times(1)).listBootcamps(0, 10, "name", "ASC", "msg-12345");
    }

    @Test
    void createBootcamp_Success() {
        // Given
        CreateBootcampRequestDTO requestDTO = new CreateBootcampRequestDTO(
                "Java Bootcamp", "Intensive Java", LocalDate.of(2026,6,1), 12, List.of(1L)
        );
        Bootcamp bootcamp = mock(Bootcamp.class);
        Bootcamp savedBootcamp = mock(Bootcamp.class);
        BootcampResponseDTO responseDTO = mock(BootcampResponseDTO.class);

        when(serverRequest.bodyToMono(CreateBootcampRequestDTO.class)).thenReturn(Mono.just(requestDTO));
        when(validator.validate(requestDTO)).thenReturn(Collections.emptySet());
        when(bootcampMapper.createBootcampRequestDTOToBootcamp(requestDTO)).thenReturn(bootcamp);
        when(createBootcampServicePort.createBootcamp(bootcamp, "msg-12345")).thenReturn(Mono.just(savedBootcamp));
        when(bootcampMapper.bootcampToBootcampResponseDTO(savedBootcamp)).thenReturn(responseDTO);

        // When
        Mono<ServerResponse> responseMono = bootcampHandler.createBootcamp(serverRequest);

        // Then
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.CREATED))
                .verifyComplete();

        verify(createBootcampServicePort, times(1)).createBootcamp(bootcamp, "msg-12345");
    }

    @Test
    void createBootcamp_ValidationFailed_ReturnsBadRequest() {
        // Given
        CreateBootcampRequestDTO requestDTO = new CreateBootcampRequestDTO("", "", LocalDate.of(2026,6,1), 0, List.of());
        ConstraintViolation<CreateBootcampRequestDTO> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Name cannot be empty");

        when(serverRequest.bodyToMono(CreateBootcampRequestDTO.class)).thenReturn(Mono.just(requestDTO));
        when(validator.validate(requestDTO)).thenReturn(Set.of(violation));

        // When
        Mono<ServerResponse> responseMono = bootcampHandler.createBootcamp(serverRequest);

        // Then
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();

        verify(createBootcampServicePort, never()).createBootcamp(any(), any());
    }

    @Test
    void deleteBootcamp_Success() {
        // Given
        Long bootcampId = 1L;
        String messageId = "msg-12345";

        when(serverRequest.pathVariable("id")).thenReturn("1");
        when(deleteBootcampServicePort.deleteBootcamp(bootcampId, messageId)).thenReturn(Mono.empty());

        // When
        Mono<ServerResponse> responseMono = bootcampHandler.deleteBootcamp(serverRequest);

        // Then
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.NO_CONTENT))
                .verifyComplete();

        verify(deleteBootcampServicePort, times(1)).deleteBootcamp(bootcampId, messageId);
    }

    @Test
    void deleteBootcamp_InvalidIdFormat_ReturnsBadRequest() {
        // Given
        when(serverRequest.pathVariable("id")).thenReturn("invalid-id");

        // When
        Mono<ServerResponse> responseMono = bootcampHandler.deleteBootcamp(serverRequest);

        // Then
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();

        verifyNoInteractions(deleteBootcampServicePort);
    }

    @Test
    void getBootcampsDetailsBatch_Success() {
        // Given
        BootcampIdsRequestDTO requestDTO = new BootcampIdsRequestDTO(List.of(1L, 2L));
        Bootcamp bootcamp = mock(Bootcamp.class);
        BootcampResponseDTO responseDTO = mock(BootcampResponseDTO.class);

        when(serverRequest.bodyToMono(BootcampIdsRequestDTO.class)).thenReturn(Mono.just(requestDTO));
        when(findBootcampsServicePort.findBootcampsByIds(List.of(1L, 2L), "msg-12345"))
                .thenReturn(Flux.just(bootcamp));
        when(bootcampMapper.bootcampToBootcampResponseDTO(bootcamp)).thenReturn(responseDTO);

        // When
        Mono<ServerResponse> responseMono = bootcampHandler.getBootcampsDetailsBatch(serverRequest);

        // Then
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        verify(findBootcampsServicePort, times(1)).findBootcampsByIds(List.of(1L, 2L), "msg-12345");
    }

    @Test
    void getMostPopularBootcamp_Success() {
        // Given
        BootcampPopularity popularity = mock(BootcampPopularity.class);
        BootcampPopularityResponseDTO responseDTO = mock(BootcampPopularityResponseDTO.class);

        when(getMostPopularBootcampServicePort.execute("msg-12345")).thenReturn(Mono.just(popularity));
        when(bootcampMapper.bootcampPopularityToResponseDTO(popularity)).thenReturn(responseDTO);

        // When
        Mono<ServerResponse> responseMono = bootcampHandler.getMostPopularBootcamp(serverRequest);

        // Then
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
                .verifyComplete();

        verify(getMostPopularBootcampServicePort, times(1)).execute("msg-12345");
    }
}
