package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrolledPersonResponseDTO {

    private String name;
    private String email;
}
