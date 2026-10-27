package com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters;

import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.spi.BootcampPersistencePort;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.entity.BootcampCapabilityEntity;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.entity.BootcampEntity;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.repository.SpringDataBootcampCapabilityRepository;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.repository.SpringDataBootcampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BootcampPersistenceAdapter implements BootcampPersistencePort {

    private final SpringDataBootcampRepository repository;
    private final SpringDataBootcampCapabilityRepository bootcampCapabilityRepository;

    @Override
    public Mono<Bootcamp> save(Bootcamp bootcamp) {

        BootcampEntity entity = new BootcampEntity();
        entity.setName(bootcamp.getName());
        entity.setDescription(bootcamp.getDescription());
        entity.setLaunchDate(bootcamp.getLaunchDate());
        entity.setDurationInWeeks(bootcamp.getDurationInWeeks());
        return repository.save(entity)
                .flatMap(saved ->{
                    List<Long> capabilities = bootcamp.getCapabilities();
                    if (capabilities == null || capabilities.isEmpty()) {
                        return Mono.just(saved);
                    }

                    // Insert the relationships in the intermediate table for each capability.
                     return Flux.fromIterable(capabilities)
                                    .flatMap(capabilityId -> {

                                        BootcampCapabilityEntity bootcampCapability = new BootcampCapabilityEntity();
                                        bootcampCapability.setBootcampId(saved.getId());
                                        bootcampCapability.setCapabilityId(capabilityId);
                                        return bootcampCapabilityRepository.save(bootcampCapability);

                                    }, 5)
                                    .then(Mono.just(saved));
                    })
                    .map(saved -> new Bootcamp(
                            saved.getId(),
                            saved.getName(),
                            saved.getDescription(),
                            saved.getLaunchDate(),
                            saved.getDurationInWeeks(),
                            bootcamp.getCapabilities()
                    ));
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return repository.existsByName(name);
    }
}
