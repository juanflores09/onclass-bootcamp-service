package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper;

import com.pragma.onclass_bootcamp_service.domain.model.BootcampCapabilityAggregate;
import com.pragma.onclass_bootcamp_service.domain.model.Capability;
import com.pragma.onclass_bootcamp_service.domain.model.Technology;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.BootcampCapabilitiesResponseDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.CapabilityDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.TechnologyDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.CapabilityResponseDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.TechnologyResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapabilityMapperTest {

    @Mock
    private TechnologyMapper technologyMapper;

    @InjectMocks
    private CapabilityMapper capabilityMapper;

    @Test
    void capabilityDtoToCapability_Success() {
        // Given
        TechnologyDTO techDTO = mock(TechnologyDTO.class);
        Technology technology = new Technology(10L, "Java", "Backend language");
        CapabilityDTO dto = new CapabilityDTO(1L, "Backend", "Backend capability", List.of(techDTO));

        when(technologyMapper.technologyDTOToTechnology(techDTO)).thenReturn(technology);

        // When
        Capability capability = capabilityMapper.capabilityDtoToCapability(dto);

        // Then
        assertThat(capability).isNotNull();
        assertThat(capability.getId()).isEqualTo(1L);
        assertThat(capability.getName()).isEqualTo("Backend");
        assertThat(capability.getDescription()).isEqualTo("Backend capability");
        assertThat(capability.getTechnologies()).hasSize(1);
        assertThat(capability.getTechnologies().getFirst()).isEqualTo(technology);

        verify(technologyMapper, times(1)).technologyDTOToTechnology(techDTO);
    }

    @Test
    void capabilityToCapabilityResponseDTO_Success() {
        // Given
        Technology technology = new Technology(10L, "Java", "Backend language");
        TechnologyResponseDTO techResponseDTO = mock(TechnologyResponseDTO.class);
        Capability capability = new Capability(1L, "Backend", "Backend capability", List.of(technology));

        when(technologyMapper.technologyToTechnologyResponseDTO(technology)).thenReturn(techResponseDTO);

        // When
        CapabilityResponseDTO responseDTO = capabilityMapper.capabilityTocapabilityResponseDTO(capability);

        // Then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.id()).isEqualTo(1L);
        assertThat(responseDTO.name()).isEqualTo("Backend");
        assertThat(responseDTO.technologies()).hasSize(1);
        assertThat(responseDTO.technologies().getFirst()).isEqualTo(techResponseDTO);

        verify(technologyMapper, times(1)).technologyToTechnologyResponseDTO(technology);
    }

    @Test
    void dtoToBootcampCapabilityAggregate_Success() {
        // Given
        CapabilityDTO capDTO = new CapabilityDTO(1L, "Backend", "Desc", List.of());
        BootcampCapabilitiesResponseDTO responseDTO = new BootcampCapabilitiesResponseDTO(5L, List.of(capDTO));

        // When
        BootcampCapabilityAggregate aggregate = capabilityMapper.dtoToBootcampCapabilityAggregate(responseDTO);

        // Then
        assertThat(aggregate).isNotNull();
        assertThat(aggregate.getBootcampId()).isEqualTo(5L);
        assertThat(aggregate.getCapabilities()).hasSize(1);
        assertThat(aggregate.getCapabilities().getFirst().getId()).isEqualTo(1L);
        assertThat(aggregate.getCapabilities().getFirst().getName()).isEqualTo("Backend");
    }

}
