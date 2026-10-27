package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper;

import com.pragma.onclass_bootcamp_service.domain.model.Bootcamp;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.CreateBootcampRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BootcampMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "launchDate", target = "launchDate")
    @Mapping(source = "durationInWeeks", target = "durationInWeeks")
    @Mapping(source = "capabilities", target = "capabilities")
    Bootcamp bootcampDtoToBootcamp(CreateBootcampRequestDTO dto);
}
