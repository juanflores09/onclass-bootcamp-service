package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper;

import com.pragma.onclass_bootcamp_service.domain.model.*;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.EnrolledPersonDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.MostPopularEnrollmentDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.entity.BootcampEntity;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BootcampMapperTest {

    @Mock
    private CapabilityMapper capabilityMapper;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private BootcampMapper bootcampMapper;

    private final LocalDate localDate = LocalDate.of(2026,6,1);

    private final Capability capability = new Capability();

    @BeforeEach
    void setUp(){
        capability.setId(10L);
        capability.setName("Backend");
        capability.setDescription("Desc");
    }

    @Test
    void createBootcampRequestDTOToBootcamp_Success() {
        // Given
        CreateBootcampRequestDTO dto = new CreateBootcampRequestDTO(
                "Java Bootcamp", "Intensive Java", localDate, 12, List.of(1L, 2L)
        );

        // When
        Bootcamp bootcamp = bootcampMapper.createBootcampRequestDTOToBootcamp(dto);

        // Then
        assertThat(bootcamp).isNotNull();
        assertThat(bootcamp.getName()).isEqualTo("Java Bootcamp");
        assertThat(bootcamp.getDescription()).isEqualTo("Intensive Java");
        assertThat(bootcamp.getLaunchDate()).isEqualTo("2026-06-01");
        assertThat(bootcamp.getDurationInWeeks()).isEqualTo(12);
        assertThat(bootcamp.getCapabilities()).hasSize(2);
        assertThat(bootcamp.getCapabilities().get(0).getId()).isEqualTo(1L);
        assertThat(bootcamp.getCapabilities().get(1).getId()).isEqualTo(2L);
    }

    @Test
    void bootcampToBootcampResponseDTO_Simple_Success() {
        // Given
        Bootcamp bootcamp = new Bootcamp(1L, "Java", "Desc", localDate, 12, null);

        // When
        BootcampResponseDTO responseDTO = bootcampMapper.bootcampToBootcampResponseDTO(bootcamp);

        // Then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.id()).isEqualTo(1L);
        assertThat(responseDTO.name()).isEqualTo("Java");
        assertThat(responseDTO.description()).isEqualTo("Desc");
        assertThat(responseDTO.launchDate()).isEqualTo("2026-06-01");
        assertThat(responseDTO.durationInWeeks()).isEqualTo(12);
    }

    @Test
    void bootcampToBootcampResponseDTO_WithCapabilities_Success() {
        // Given
        Bootcamp bootcamp = new Bootcamp(1L, "Java", "Desc", localDate, 12, null);
        CapabilityResponseDTO capabilityResponseDTO = mock(CapabilityResponseDTO.class);

        when(capabilityMapper.capabilityTocapabilityResponseDTO(capability)).thenReturn(capabilityResponseDTO);

        // When
        BootcampResponseDTO responseDTO = bootcampMapper.bootcampToBootcampResponseDTO(bootcamp, List.of(capability));

        // Then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.id()).isEqualTo(1L);
        assertThat(responseDTO.capabilities()).hasSize(1);
        assertThat(responseDTO.capabilities().getFirst()).isEqualTo(capabilityResponseDTO);

        verify(capabilityMapper, times(1)).capabilityTocapabilityResponseDTO(capability);
    }

    @Test
    void bootcampToBootcampEntity_Success() {
        // Given
        Bootcamp bootcamp = new Bootcamp(1L, "Java", "Desc", localDate, 12, List.of(capability));

        // When
        BootcampEntity entity = bootcampMapper.bootcampToBootcampEntity(bootcamp);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("Java");
        assertThat(entity.getDescription()).isEqualTo("Desc");
        assertThat(entity.getLaunchDate()).isEqualTo("2026-06-01");
        assertThat(entity.getDurationInWeeks()).isEqualTo(12);
        assertThat(entity.getCapabilityCount()).isEqualTo(1);
    }

    @Test
    void bootcampEntityToBootcamp_Simple_Success() {
        // Given
        BootcampEntity entity = new BootcampEntity();
        entity.setId(1L);
        entity.setName("Java");
        entity.setDescription("Desc");
        entity.setLaunchDate(localDate);
        entity.setDurationInWeeks(12);

        // When
        Bootcamp bootcamp = bootcampMapper.bootcampEntityToBootcamp(entity);

        // Then
        assertThat(bootcamp).isNotNull();
        assertThat(bootcamp.getId()).isEqualTo(1L);
        assertThat(bootcamp.getName()).isEqualTo("Java");
        assertThat(bootcamp.getDescription()).isEqualTo("Desc");
        assertThat(bootcamp.getLaunchDate()).isEqualTo("2026-06-01");
        assertThat(bootcamp.getDurationInWeeks()).isEqualTo(12);
    }

    @Test
    void bootcampEntityToBootcamp_WithCapabilities_Success() {
        // Given
        BootcampEntity entity = new BootcampEntity();
        entity.setId(1L);
        entity.setName("Java");
        entity.setDescription("Desc");
        entity.setLaunchDate(localDate);
        entity.setDurationInWeeks(12);

        // When
        Bootcamp bootcamp = bootcampMapper.bootcampEntityToBootcamp(entity, List.of(capability));

        // Then
        assertThat(bootcamp).isNotNull();
        assertThat(bootcamp.getId()).isEqualTo(1L);
        assertThat(bootcamp.getCapabilities()).hasSize(1);
        assertThat(bootcamp.getCapabilities().getFirst().getName()).isEqualTo("Backend");
    }

    @Test
    void dtoToMostPopularEnrollmentAggregate_Success() {
        // Given
        EnrolledPersonDTO personDTO = mock(EnrolledPersonDTO.class);
        EnrolledPerson personDomain = mock(EnrolledPerson.class);
        MostPopularEnrollmentDTO dto = mock(MostPopularEnrollmentDTO.class);

        when(dto.getBootcampId()).thenReturn(1L);
        when(dto.getTotalEnrolled()).thenReturn(15);
        when(dto.getEnrolledPersons()).thenReturn(List.of(personDTO));
        when(personMapper.enrolledPersonDtoToenrolledPerson(personDTO)).thenReturn(personDomain);

        // When
        MostPopularEnrollmentAggregate aggregate = bootcampMapper.dtoToMostPopularEnrollmentAggregate(dto);

        // Then
        assertThat(aggregate).isNotNull();
        assertThat(aggregate.getBootcampId()).isEqualTo(1L);
        assertThat(aggregate.getTotalEnrolled()).isEqualTo(15L);
        assertThat(aggregate.getEnrolledPersons()).hasSize(1);
        assertThat(aggregate.getEnrolledPersons().getFirst()).isEqualTo(personDomain);

        verify(personMapper, times(1)).enrolledPersonDtoToenrolledPerson(personDTO);
    }

    @Test
    void bootcampPopularityToResponseDTO_Success() {
        // Given
        CapabilityResponseDTO capabilityResponseDTO = mock(CapabilityResponseDTO.class);

        EnrolledPerson person = mock(EnrolledPerson.class);
        EnrolledPersonResponseDTO personResponseDTO = mock(EnrolledPersonResponseDTO.class);

        BootcampPopularity popularity = new BootcampPopularity(
                1L, "Java", "Desc", localDate, 12, 20, List.of(capability), List.of(person)
        );

        when(capabilityMapper.capabilityTocapabilityResponseDTO(capability)).thenReturn(capabilityResponseDTO);
        when(personMapper.enrolledPersonDtoToResponseDTO(person)).thenReturn(personResponseDTO);

        // When
        BootcampPopularityResponseDTO responseDTO = bootcampMapper.bootcampPopularityToResponseDTO(popularity);

        // Then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getId()).isEqualTo(1L);
        assertThat(responseDTO.getName()).isEqualTo("Java");
        assertThat(responseDTO.getTotalEnrolled()).isEqualTo(20L);
        assertThat(responseDTO.getCapabilities()).hasSize(1);
        assertThat(responseDTO.getCapabilities().getFirst()).isEqualTo(capabilityResponseDTO);
        assertThat(responseDTO.getEnrolledPersons()).hasSize(1);
        assertThat(responseDTO.getEnrolledPersons().getFirst()).isEqualTo(personResponseDTO);

        verify(capabilityMapper, times(1)).capabilityTocapabilityResponseDTO(capability);
        verify(personMapper, times(1)).enrolledPersonDtoToResponseDTO(person);
    }
}
