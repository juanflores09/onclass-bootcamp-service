package com.pragma.onclass_bootcamp_service.domain.usecase;

import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.model.BootcampCapabilityAggregate;
import com.pragma.onclass_bootcamp_service.domain.model.Capability;
import com.pragma.onclass_bootcamp_service.domain.model.MostPopularEnrollmentAggregate;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGatewayPort;
import com.pragma.onclass_bootcamp_service.domain.spi.PersonGatewayPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetMostPopularBootcampUseCaseTest {

    @Mock
    private BootcampPersistencePort bootcampPersistencePort;

    @Mock
    private PersonGatewayPort personGateway;

    @Mock
    private CapabilityGatewayPort capabilityGateway;

    @InjectMocks
    private GetMostPopularBootcampUseCase getMostPopularBootcampUseCase;

    private final String messageId = "msg-12345";

    @Test
    void shouldReturnMostPopularBootcampSuccessfully() {
        // Arrange
        Long bootcampId = 1L;

        MostPopularEnrollmentAggregate popularInfo = mock(MostPopularEnrollmentAggregate.class);
        when(popularInfo.getBootcampId()).thenReturn(bootcampId);
        when(popularInfo.getTotalEnrolled()).thenReturn(15);
        when(popularInfo.getEnrolledPersons()).thenReturn(List.of());

        // Mock del detalle del bootcamp
        Bootcamp bootcampDetail = new Bootcamp(
                bootcampId,
                "Java Reactive",
                "Avanzado Spring WebFlux",
                LocalDate.of(2026,6,1),
                12,
                null
        );

        // Mock de las capacidades asociadas devueltas por el gateway
        Capability capability = new Capability();
        capability.setId(10L);
        capability.setName("Backend");
        capability.setDescription("Reactive systems");
        BootcampCapabilityAggregate capabilityResponseDTO = mock(BootcampCapabilityAggregate.class);
        when(capabilityResponseDTO.getCapabilities()).thenReturn(List.of(capability));

        when(personGateway.getMostPopularEnrollments(messageId))
                .thenReturn(Mono.just(popularInfo));
        when(capabilityGateway.getCapabilitiesByBootcampId(List.of(bootcampId), messageId))
                .thenReturn(Flux.just(capabilityResponseDTO));
        when(bootcampPersistencePort.findById(bootcampId))
                .thenReturn(Mono.just(bootcampDetail));

        // Act & Assert
        StepVerifier.create(getMostPopularBootcampUseCase.execute(messageId))
                .assertNext(popularity -> {
                    assertThat(popularity).isNotNull();
                    assertThat(popularity.getId()).isEqualTo(bootcampId);
                    assertThat(popularity.getName()).isEqualTo("Java Reactive");
                    assertThat(popularity.getTotalEnrolled()).isEqualTo(15L);
                    assertThat(popularity.getCapabilities()).hasSize(1);
                    assertThat(popularity.getCapabilities().getFirst().getName()).isEqualTo("Backend");
                })
                .verifyComplete();

        // Verify interactions
        verify(personGateway).getMostPopularEnrollments(messageId);
        verify(capabilityGateway).getCapabilitiesByBootcampId(List.of(bootcampId), messageId);
        verify(bootcampPersistencePort).findById(bootcampId);
    }

    @Test
    void shouldCompleteEmptyWhenNoPopularBootcampFound() {
        // Arrange
        when(personGateway.getMostPopularEnrollments(messageId))
                .thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(getMostPopularBootcampUseCase.execute(messageId))
                .verifyComplete();

        // Verify interactions
        verify(personGateway).getMostPopularEnrollments(messageId);
        verify(capabilityGateway, never()).getCapabilitiesByBootcampId(anyList(), any());
        verify(bootcampPersistencePort, never()).findById(any());
    }
}
