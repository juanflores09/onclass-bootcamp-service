package com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters;

import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.entity.BootcampEntity;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.repository.SpringDataBootcampRepository;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper.BootcampMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class BootcampPersistenceAdapter implements BootcampPersistencePort {

    private final SpringDataBootcampRepository repository;
    private final BootcampMapper bootcampMapper;

    @Override
    public Mono<Bootcamp> save(Bootcamp bootcamp) {

        BootcampEntity entity = bootcampMapper.bootcampToBootcampEntity(bootcamp);
        return repository.save(entity)
                    .map(saved -> bootcampMapper.bootcampEntityToBootcamp(saved, bootcamp.getCapabilities()));
    }

    @Override
    public Flux<Bootcamp> findAllPagedAndSorted(String sortBy, String direction, int limit, long offset) {
        return repository.findAllPagedAndSorted(sortBy, direction, limit, offset)
                .map(bootcampMapper::bootcampEntityToBootcamp);
    }

    @Override
    public Mono<Long> countTotal() {
        return repository.count();
    }

    @Override
    public Mono<Bootcamp> findById(Long bootcampId) {
        return repository.findById(bootcampId)
                .map(bootcampMapper::bootcampEntityToBootcamp);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }
}
