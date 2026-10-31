package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper;

import com.pragma.onclass_bootcamp_service.domain.model.Technology;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.TechnologyDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.TechnologyResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class TechnologyMapper {

    public Technology technologyDTOToTechnology(TechnologyDTO dto){
        return new Technology(
                dto.id(),
                dto.name(),
                dto.description()
        );
    }

    public TechnologyResponseDTO technologyToTechnologyResponseDTO(Technology technology){
        return new TechnologyResponseDTO(
                technology.getId(),
                technology.getName()
        );
    }
}
