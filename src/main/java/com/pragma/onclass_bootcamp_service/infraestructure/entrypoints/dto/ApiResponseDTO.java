package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.util.ErrorDTO;
import lombok.*;

import java.util.List;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDTO {

    private String code;
    private String message;
    private String identifier;
    private String date;
    private CreateBootcampRequestDTO data;
    private List<ErrorDTO> errors;

}
