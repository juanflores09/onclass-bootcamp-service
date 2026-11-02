package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper;

import com.pragma.onclass_bootcamp_service.domain.model.BootcampCapabilityAggregate;
import com.pragma.onclass_bootcamp_service.domain.model.Capability;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.BootcampCapabilitiesResponseDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.CapabilityDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.CapabilityResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CapabilityMapper {

    private final TechnologyMapper technologyMapper;

    public Capability capabilityDtoToCapability(CapabilityDTO dto){
        return new Capability(
                dto.id(),
                dto.name(),
                dto.description(),
                dto.technologies().stream()
                        .map(technologyMapper::technologyDTOToTechnology)
                        .toList());
    }

    public CapabilityResponseDTO capabilityTocapabilityResponseDTO(Capability capability){
        return new CapabilityResponseDTO(
                capability.getId(),
                capability.getName(),
                capability.getTechnologies().stream()
                        .map(technologyMapper::technologyToTechnologyResponseDTO)
                        .toList()
        );
    }

    public BootcampCapabilityAggregate dtoToBootcampCapabilityAggregate(BootcampCapabilitiesResponseDTO dto){
        return new BootcampCapabilityAggregate(
                dto.bootcampId(),
                dto.capabilities().stream()
                        .map(this::capabilityDtoToCapability)
                        .toList()
        );
    }
}
