package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper;

import com.pragma.onclass_bootcamp_service.domain.model.EnrolledPerson;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.EnrolledPersonDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.EnrolledPersonResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonMapperTest {

    @InjectMocks
    private PersonMapper personMapper;

    @Test
    void enrolledPersonDtoToEnrolledPerson_Success() {
        // Given
        EnrolledPersonDTO dto = mock(EnrolledPersonDTO.class);
        when(dto.getName()).thenReturn("Juan Pérez");
        when(dto.getEmail()).thenReturn("juan.perez@example.com");

        // When
        EnrolledPerson person = personMapper.enrolledPersonDtoToenrolledPerson(dto);

        // Then
        assertThat(person).isNotNull();
        assertThat(person.getName()).isEqualTo("Juan Pérez");
        assertThat(person.getEmail()).isEqualTo("juan.perez@example.com");
    }

    @Test
    void enrolledPersonDtoToResponseDTO_Success() {
        // Given
        EnrolledPerson person = mock(EnrolledPerson.class);
        when(person.getName()).thenReturn("María Gómez");
        when(person.getEmail()).thenReturn("maria.gomez@example.com");

        // When
        EnrolledPersonResponseDTO responseDTO = personMapper.enrolledPersonDtoToResponseDTO(person);

        // Then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getName()).isEqualTo("María Gómez");
        assertThat(responseDTO.getEmail()).isEqualTo("maria.gomez@example.com");
    }
}
