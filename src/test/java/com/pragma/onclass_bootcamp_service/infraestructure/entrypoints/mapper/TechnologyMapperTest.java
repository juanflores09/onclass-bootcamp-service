package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.mapper;

import com.pragma.onclass_bootcamp_service.domain.model.Technology;
import com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto.TechnologyDTO;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.TechnologyResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TechnologyMapperTest {

    @InjectMocks
    private TechnologyMapper technologyMapper;

    @Test
    void technologyDTOToTechnology_Success() {
        // Given
        TechnologyDTO dto = new TechnologyDTO(1L, "Java", "Programming language");

        // When
        Technology technology = technologyMapper.technologyDTOToTechnology(dto);

        // Then
        assertThat(technology).isNotNull();
        assertThat(technology.getId()).isEqualTo(1L);
        assertThat(technology.getName()).isEqualTo("Java");
        assertThat(technology.getDescription()).isEqualTo("Programming language");
    }

    @Test
    void technologyToTechnologyResponseDTO_Success() {
        // Given
        Technology technology = new Technology(1L, "Java", "Programming language");

        // When
        TechnologyResponseDTO responseDTO = technologyMapper.technologyToTechnologyResponseDTO(technology);

        // Then
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.id()).isEqualTo(1L);
        assertThat(responseDTO.name()).isEqualTo("Java");
    }
}
