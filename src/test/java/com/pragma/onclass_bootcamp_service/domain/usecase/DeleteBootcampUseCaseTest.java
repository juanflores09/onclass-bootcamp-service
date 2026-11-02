package com.pragma.onclass_bootcamp_service.domain.usecase;

import com.pragma.onclass_bootcamp_service.domain.enums.TechnicalMessage;
import com.pragma.onclass_bootcamp_service.domain.exceptions.BusinessException;
import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteBootcampUseCaseTest {

    @Mock
    private BootcampPersistencePort bootcampPersistencePort;

    @Mock
    private CapabilityGateway capabilityGateway;

    @InjectMocks
    private DeleteBootcampUseCase deleteBootcampUseCase;

    private final Long bootcampId = 1L;
    private final String messageId = "msg-12345";

    @Test
    void shouldDeleteBootcampSuccessfully() {
        // Arrange
        Bootcamp existingBootcamp = new Bootcamp(bootcampId, "Java Bootcamp", "Intensive Java", LocalDate.of(2026,6,1), 16, null);

        when(bootcampPersistencePort.findById(bootcampId))
                .thenReturn(Mono.just(existingBootcamp));
        when(capabilityGateway.deleteCapabilitiesByBootcampId(bootcampId, messageId))
                .thenReturn(Mono.empty());
        when(bootcampPersistencePort.deleteById(bootcampId))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(deleteBootcampUseCase.deleteBootcamp(bootcampId, messageId))
                .verifyComplete();

        // Verify interactions
        verify(bootcampPersistencePort).findById(bootcampId);
        verify(capabilityGateway).deleteCapabilitiesByBootcampId(bootcampId, messageId);
        verify(bootcampPersistencePort).deleteById(bootcampId);
    }

    @Test
    void shouldFailWhenBootcampNotFound() {
        // Arrange
        when(bootcampPersistencePort.findById(bootcampId))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(deleteBootcampUseCase.deleteBootcamp(bootcampId, messageId))
                .expectErrorSatisfies(error -> {
                    assertThat(error)
                            .isInstanceOf(BusinessException.class)
                            .hasMessage(TechnicalMessage.BOOTCAMP_NOT_FOUND.getMessage());
                })
                .verify();

        // Verify interactions
        verify(bootcampPersistencePort).findById(bootcampId);
        verify(capabilityGateway, never()).deleteCapabilitiesByBootcampId(any(), any());
        verify(bootcampPersistencePort, never()).deleteById(any());
    }
}
