package com.pragma.onclass_bootcamp_service.domain.usecase;

import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.model.BootcampCapabilityAggregate;
import com.pragma.onclass_bootcamp_service.domain.model.Capability;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.domain.spi.CapabilityGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListBootcampUseCaseTest {

    @Mock
    private BootcampPersistencePort bootcampPersistencePort;

    @Mock
    private CapabilityGateway capabilityGateway;

    @InjectMocks
    private ListBootcampUseCase listBootcampUseCase;

    private final String messageId = "msg-12345";
    private final LocalDate localDate = LocalDate.of(2026,6,1);

    @Test
    void shouldListBootcampsSuccessfully() {
        // Arrange
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String direction = "ASC";
        long offset = 0L;

        Bootcamp bootcamp = new Bootcamp(1L, "Java Bootcamp", "Intensive Java", localDate, 16, null);
        Capability capability = new Capability();
        capability.setId(10L);
        capability.setName("Backend");
        capability.setDescription("Backend capability");
        BootcampCapabilityAggregate aggregate = mock(BootcampCapabilityAggregate.class);
        when(aggregate.getBootcampId()).thenReturn(1L);
        when(aggregate.getCapabilities()).thenReturn(List.of(capability));

        when(bootcampPersistencePort.findAllPagedAndSorted(sortBy, direction, size, offset))
                .thenReturn(Flux.just(bootcamp));
        when(bootcampPersistencePort.countTotal())
                .thenReturn(Mono.just(1L));
        when(capabilityGateway.getCapabilitiesByBootcampId(List.of(1L), messageId))
                .thenReturn(Flux.just(aggregate));

        // Act & Assert
        StepVerifier.create(listBootcampUseCase.listBootcamps(page, size, sortBy, direction, messageId))
                .assertNext(pageResult -> {
                    assertThat(pageResult).isNotNull();
                    assertThat(pageResult.totalElements()).isEqualTo(1L);
                    assertThat(pageResult.content()).hasSize(1);
                    assertThat(pageResult.content().getFirst().getName()).isEqualTo("Java Bootcamp");
                    assertThat(pageResult.content().getFirst().getCapabilities()).hasSize(1);
                    assertThat(pageResult.content().getFirst().getCapabilities().getFirst().getName()).isEqualTo("Backend");
                })
                .verifyComplete();

        // Verify interactions
        verify(bootcampPersistencePort).findAllPagedAndSorted(sortBy, direction, size, offset);
        verify(bootcampPersistencePort).countTotal();
        verify(capabilityGateway).getCapabilitiesByBootcampId(List.of(1L), messageId);
    }

    @Test
    void shouldReturnEmptyPageWhenNoBootcampsFound() {
        // Arrange
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String direction = "ASC";
        long offset = 0L;

        when(bootcampPersistencePort.findAllPagedAndSorted(sortBy, direction, size, offset))
                .thenReturn(Flux.empty());
        when(bootcampPersistencePort.countTotal())
                .thenReturn(Mono.just(0L));

        // Act & Assert
        StepVerifier.create(listBootcampUseCase.listBootcamps(page, size, sortBy, direction, messageId))
                .assertNext(pageResult -> {
                    assertThat(pageResult).isNotNull();
                    assertThat(pageResult.totalElements()).isZero();
                    assertThat(pageResult.content()).isEmpty();
                })
                .verifyComplete();

        // Verify interactions
        verify(bootcampPersistencePort).findAllPagedAndSorted(sortBy, direction, size, offset);
        verify(bootcampPersistencePort).countTotal();
        verify(capabilityGateway, never()).getCapabilitiesByBootcampId(anyList(), anyString());
    }

    @Test
    void shouldFindBootcampsByIdsSuccessfully() {
        // Arrange
        List<Long> ids = List.of(1L, 2L);
        Bootcamp bootcamp1 = new Bootcamp(1L, "Java", "Desc 1", localDate, 12, null);
        Bootcamp bootcamp2 = new Bootcamp(2L, "Python", "Desc 2", localDate, 10, null);

        when(bootcampPersistencePort.findByIds(ids))
                .thenReturn(Flux.just(bootcamp1, bootcamp2));

        // Act & Assert
        StepVerifier.create(listBootcampUseCase.findBootcampsByIds(ids, messageId))
                .expectNext(bootcamp1)
                .expectNext(bootcamp2)
                .verifyComplete();

        // Verify interactions
        verify(bootcampPersistencePort).findByIds(ids);
    }

    @Test
    void shouldReturnEmptyWhenIdsAreNullOrEmpty() {
        // Act & Assert
        StepVerifier.create(listBootcampUseCase.findBootcampsByIds(null, messageId))
                .verifyComplete();

        StepVerifier.create(listBootcampUseCase.findBootcampsByIds(Collections.emptyList(), messageId))
                .verifyComplete();

        // Verify no persistence interaction
        verify(bootcampPersistencePort, never()).findByIds(anyList());
    }
}
