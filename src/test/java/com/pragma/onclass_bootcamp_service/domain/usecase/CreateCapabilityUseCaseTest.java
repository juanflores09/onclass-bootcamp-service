package com.pragma.onclass_bootcamp_service.domain.usecase;

import com.pragma.onclass_bootcamp_service.domain.enums.TechnicalMessage;
import com.pragma.onclass_bootcamp_service.domain.exceptions.BusinessException;
import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.model.Capability;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCapabilityUseCaseTest {
    @Mock
    private BootcampPersistencePort bootcampPersistencePort;

    @Mock
    private CapabilityGateway capabilityValidatorGateway;

    @InjectMocks
    private CreateBootcampUseCase createBootcampUseCase;

    private final String messageId = "msg-12345";

    private Capability capability;

    @BeforeEach
    void setup(){
        capability = new Capability();
        capability.setId(1L);
        capability.setName( "Backend");
        capability.setDescription("Backend capability");
    }

    @Test
    void shouldCreateBootcampSuccessfully() {
        // Arrange
        Bootcamp bootcampToCreate = new Bootcamp(null, "Java Bootcamp", "Intensive Java", LocalDate.of(2026, 6, 1), 16, List.of(capability));
        Bootcamp savedBootcamp = new Bootcamp(5L, "Java Bootcamp", "Intensive Java", LocalDate.of(2026, 6, 1), 16, List.of(capability));

        when(bootcampPersistencePort.existsByName("Java Bootcamp"))
                .thenReturn(Mono.just(false));
        when(bootcampPersistencePort.save(any(Bootcamp.class)))
                .thenReturn(Mono.just(savedBootcamp));
        when(capabilityValidatorGateway.saveCapabilityTechnologies(5L, List.of(1L), messageId))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(createBootcampUseCase.createBootcamp(bootcampToCreate, messageId))
                .expectNext(savedBootcamp)
                .verifyComplete();

        // Verify interactions
        verify(bootcampPersistencePort).existsByName("Java Bootcamp");
        verify(bootcampPersistencePort).save(any(Bootcamp.class));
        verify(capabilityValidatorGateway).saveCapabilityTechnologies(5L, List.of(1L), messageId);
        verify(bootcampPersistencePort, never()).deleteById(any());
    }

    @Test
    void shouldFailWhenBootcampAlreadyExists() {
        // Arrange
        Bootcamp bootcampToCreate = new Bootcamp(null, "Java Bootcamp", "Intensive Java", LocalDate.of(2026, 6, 1), 16, List.of());

        when(bootcampPersistencePort.existsByName("Java Bootcamp"))
                .thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(createBootcampUseCase.createBootcamp(bootcampToCreate, messageId))
                .expectErrorSatisfies(error -> {
                    assertThat(error)
                            .isInstanceOf(BusinessException.class)
                            .hasMessage(TechnicalMessage.BOOTCAMP_ALREADY_EXISTS.getMessage());
                })
                .verify();

        // Verify interactions
        verify(bootcampPersistencePort).existsByName("Java Bootcamp");
        verify(bootcampPersistencePort, never()).save(any());
        verify(capabilityValidatorGateway, never()).saveCapabilityTechnologies(any(), any(), any());
    }

    @Test
    void shouldRollbackAndFailWhenCapabilityGatewayFails() {
        // Arrange
        Bootcamp bootcampToCreate = new Bootcamp(null, "Java Bootcamp", "Intensive Java", LocalDate.of(2026, 6, 1), 16, List.of(capability));
        Bootcamp savedBootcamp = new Bootcamp(5L, "Java Bootcamp", "Intensive Java", LocalDate.of(2026, 6, 1), 16, List.of(capability));

        RuntimeException gatewayError = new RuntimeException("Capability service unavailable");

        when(bootcampPersistencePort.existsByName("Java Bootcamp"))
                .thenReturn(Mono.just(false));
        when(bootcampPersistencePort.save(any(Bootcamp.class)))
                .thenReturn(Mono.just(savedBootcamp));
        when(capabilityValidatorGateway.saveCapabilityTechnologies(5L, List.of(1L), messageId))
                .thenReturn(Mono.error(gatewayError));
        when(bootcampPersistencePort.deleteById(5L))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(createBootcampUseCase.createBootcamp(bootcampToCreate, messageId))
                .expectErrorSatisfies(error -> {
                    assertThat(error)
                            .isInstanceOf(RuntimeException.class)
                            .hasMessage("Capability service unavailable");
                })
                .verify();

        // Verify interactions
        verify(bootcampPersistencePort).existsByName("Java Bootcamp");
        verify(bootcampPersistencePort).save(any(Bootcamp.class));
        verify(capabilityValidatorGateway).saveCapabilityTechnologies(5L, List.of(1L), messageId);
        verify(bootcampPersistencePort).deleteById(5L);
    }

    @Test
    void shouldCreateBootcampSuccessfullyWhenCapabilitiesAreNull() {
        // Arrange
        Bootcamp bootcampToCreate = new Bootcamp(null, "Java Bootcamp", "Intensive Java", LocalDate.of(2026,6,1), 16, null);
        Bootcamp savedBootcamp = new Bootcamp(5L, "Java Bootcamp", "Intensive Java", LocalDate.of(2026,6,1), 16, null);

        when(bootcampPersistencePort.existsByName("Java Bootcamp"))
                .thenReturn(Mono.just(false));
        when(bootcampPersistencePort.save(any(Bootcamp.class)))
                .thenReturn(Mono.just(savedBootcamp));

        when(capabilityValidatorGateway.saveCapabilityTechnologies(5L, Collections.emptyList(), messageId))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(createBootcampUseCase.createBootcamp(bootcampToCreate, messageId))
                .expectNext(savedBootcamp)
                .verifyComplete();

        // Verify interactions
        verify(bootcampPersistencePort).existsByName("Java Bootcamp");
        verify(bootcampPersistencePort).save(any(Bootcamp.class));
        verify(capabilityValidatorGateway).saveCapabilityTechnologies(5L, Collections.emptyList(), messageId);
        verify(bootcampPersistencePort, never()).deleteById(any());
    }
}
