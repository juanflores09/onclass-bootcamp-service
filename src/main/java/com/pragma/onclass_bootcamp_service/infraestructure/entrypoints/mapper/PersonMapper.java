package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper;

import com.pragma.onclass_bootcamp_service.domain.model.EnrolledPerson;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.EnrolledPersonDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.EnrolledPersonResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    public EnrolledPerson enrolledPersonDtoToenrolledPerson(EnrolledPersonDTO dto){
        return new EnrolledPerson(
                dto.getName(),
                dto.getEmail()
        );
    }

    public EnrolledPersonResponseDTO enrolledPersonDtoToResponseDTO(EnrolledPerson dto){
        return new EnrolledPersonResponseDTO(
                dto.getName(),
                dto.getEmail()
        );
    }
}
