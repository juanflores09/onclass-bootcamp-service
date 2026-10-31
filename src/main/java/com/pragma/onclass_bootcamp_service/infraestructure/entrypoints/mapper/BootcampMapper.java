package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper;

import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.domain.model.Capability;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.entity.BootcampEntity;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.BootcampResponseDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.CreateBootcampRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BootcampMapper {

    private final CapabilityMapper capabilityMapper;
    public Bootcamp createBootcampRequestDTOToBootcamp(CreateBootcampRequestDTO dto){
        return new Bootcamp(
                dto.name(),
                dto.description(),
                dto.launchDate(),
                dto.durationInWeeks(),
                dto.capabilities().stream()
                        .map(Capability::new)
                        .toList()
        );
    }

    public BootcampResponseDTO bootcampToBootcampResponseDTO(Bootcamp bootcamp){
        return new BootcampResponseDTO(
                bootcamp.getId(),
                bootcamp.getName(),
                bootcamp.getDescription(),
                bootcamp.getLaunchDate(),
                bootcamp.getDurationInWeeks()
        );
    }

    public BootcampResponseDTO bootcampToBootcampResponseDTO(Bootcamp bootcamp, List<Capability> capabilities){
        return new BootcampResponseDTO(
                bootcamp.getId(),
                bootcamp.getName(),
                bootcamp.getDescription(),
                bootcamp.getLaunchDate(),
                bootcamp.getDurationInWeeks(),
                capabilities.stream()
                        .map(capabilityMapper::capabilityTocapabilityResponseDTO)
                        .toList()
        );
    }

    public BootcampEntity bootcampToBootcampEntity(Bootcamp bootcamp){
        BootcampEntity entity = new BootcampEntity();
        entity.setName(bootcamp.getName());
        entity.setDescription(bootcamp.getDescription());
        entity.setLaunchDate(bootcamp.getLaunchDate());
        entity.setDurationInWeeks(bootcamp.getDurationInWeeks());
        entity.setCapabilityCount(bootcamp.getCapabilities().size());
        return entity;
    }


    public Bootcamp bootcampEntityToBootcamp(BootcampEntity bootcampEntity){
        return new Bootcamp(
                bootcampEntity.getId(),
                bootcampEntity.getName(),
                bootcampEntity.getDescription(),
                bootcampEntity.getLaunchDate(),
                bootcampEntity.getDurationInWeeks()
        );
    }

    public Bootcamp bootcampEntityToBootcamp(BootcampEntity bootcampEntity, List<Capability> capabilities){
        return new Bootcamp(
                bootcampEntity.getId(),
                bootcampEntity.getName(),
                bootcampEntity.getDescription(),
                bootcampEntity.getLaunchDate(),
                bootcampEntity.getDurationInWeeks(),
                capabilities
        );
    }
}
