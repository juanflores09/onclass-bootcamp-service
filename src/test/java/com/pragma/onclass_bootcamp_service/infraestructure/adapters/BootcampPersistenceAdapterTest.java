package com.pragma.onclass_bootcamp_service.infraestructure.adapters;

import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.model.Capability;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.BootcampPersistenceAdapter;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.entity.BootcampEntity;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.repository.SpringDataBootcampRepository;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper.BootcampMapper;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BootcampPersistenceAdapterTest {

    @Mock
    private SpringDataBootcampRepository repository;

    @Mock
    private BootcampMapper bootcampMapper;

    @InjectMocks
    private BootcampPersistenceAdapter adapter;

    private final LocalDate localDate = LocalDate.of(2026,6,1);

    @Test
    void save_Success() {
        // Given
        Capability capability = new Capability();
        capability.setId(1L);
        capability.setName("Backend");
        capability.setDescription("Desc");

        Bootcamp bootcamp = new Bootcamp(null, "Java", "Desc Java", localDate, 12, List.of(capability));
        BootcampEntity entity = new BootcampEntity();
        BootcampEntity savedEntity = new BootcampEntity();
        Bootcamp savedBootcamp = new Bootcamp(1L, "Java", "Desc Java", localDate, 12, List.of(capability));

        when(bootcampMapper.bootcampToBootcampEntity(bootcamp)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(savedEntity));
        when(bootcampMapper.bootcampEntityToBootcamp(savedEntity, bootcamp.getCapabilities())).thenReturn(savedBootcamp);

        // When
        Mono<Bootcamp> result = adapter.save(bootcamp);

        // Then
        StepVerifier.create(result)
                .expectNext(savedBootcamp)
                .verifyComplete();

        verify(bootcampMapper, times(1)).bootcampToBootcampEntity(bootcamp);
        verify(repository, times(1)).save(entity);
        verify(bootcampMapper, times(1)).bootcampEntityToBootcamp(savedEntity, bootcamp.getCapabilities());
    }

    @Test
    void findAllPagedAndSorted_Success() {
        // Given
        String sortBy = "name";
        String direction = "ASC";
        int limit = 10;
        long offset = 0L;

        BootcampEntity entity = new BootcampEntity();
        Bootcamp bootcamp = new Bootcamp(1L, "Java", "Desc", localDate, 12, null);

        when(repository.findAllPagedAndSorted(sortBy, direction, limit, offset)).thenReturn(Flux.just(entity));
        when(bootcampMapper.bootcampEntityToBootcamp(entity)).thenReturn(bootcamp);

        // When
        Flux<Bootcamp> result = adapter.findAllPagedAndSorted(sortBy, direction, limit, offset);

        // Then
        StepVerifier.create(result)
                .expectNext(bootcamp)
                .verifyComplete();

        verify(repository, times(1)).findAllPagedAndSorted(sortBy, direction, limit, offset);
        verify(bootcampMapper, times(1)).bootcampEntityToBootcamp(entity);
    }

    @Test
    void countTotal_Success() {
        // Given
        when(repository.count()).thenReturn(Mono.just(5L));

        // When
        Mono<Long> result = adapter.countTotal();

        // Then
        StepVerifier.create(result)
                .expectNext(5L)
                .verifyComplete();

        verify(repository, times(1)).count();
    }

    @Test
    void findById_Success() {
        // Given
        Long bootcampId = 1L;
        BootcampEntity entity = new BootcampEntity();
        Bootcamp bootcamp = new Bootcamp(bootcampId, "Java", "Desc", localDate, 12, null);

        when(repository.findById(bootcampId)).thenReturn(Mono.just(entity));
        when(bootcampMapper.bootcampEntityToBootcamp(entity)).thenReturn(bootcamp);

        // When
        Mono<Bootcamp> result = adapter.findById(bootcampId);

        // Then
        StepVerifier.create(result)
                .expectNext(bootcamp)
                .verifyComplete();

        verify(repository, times(1)).findById(bootcampId);
        verify(bootcampMapper, times(1)).bootcampEntityToBootcamp(entity);
    }

    @Test
    void findById_NotFound() {
        // Given
        Long bootcampId = 99L;
        when(repository.findById(bootcampId)).thenReturn(Mono.empty());

        // When
        Mono<Bootcamp> result = adapter.findById(bootcampId);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(repository, times(1)).findById(bootcampId);
        verify(bootcampMapper, never()).bootcampEntityToBootcamp(any());
    }

    @Test
    void findByIds_Success() {
        // Given
        List<Long> ids = List.of(1L, 2L);
        BootcampEntity entity1 = new BootcampEntity();
        BootcampEntity entity2 = new BootcampEntity();
        Bootcamp bootcamp1 = new Bootcamp(1L, "Java", "Desc", localDate, 12, null);
        Bootcamp bootcamp2 = new Bootcamp(2L, "Python", "Desc", localDate, 10, null);

        when(repository.findAllById(ids)).thenReturn(Flux.just(entity1, entity2));
        when(bootcampMapper.bootcampEntityToBootcamp(entity1)).thenReturn(bootcamp1);
        when(bootcampMapper.bootcampEntityToBootcamp(entity2)).thenReturn(bootcamp2);

        // When
        Flux<Bootcamp> result = adapter.findByIds(ids);

        // Then
        StepVerifier.create(result)
                .expectNext(bootcamp1)
                .expectNext(bootcamp2)
                .verifyComplete();

        verify(repository, times(1)).findAllById(ids);
    }

    @Test
    void existsByName_True() {
        // Given
        String name = "Java Bootcamp";
        when(repository.existsByName(name)).thenReturn(Mono.just(true));

        // When
        Mono<Boolean> result = adapter.existsByName(name);

        // Then
        StepVerifier.create(result)
                .expectNext(true)
                .verifyComplete();

        verify(repository, times(1)).existsByName(name);
    }

    @Test
    void deleteById_Success() {
        // Given
        Long bootcampId = 1L;
        when(repository.deleteById(bootcampId)).thenReturn(Mono.empty());

        // When
        Mono<Void> result = adapter.deleteById(bootcampId);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        verify(repository, times(1)).deleteById(bootcampId);
    }
}
