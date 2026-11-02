package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BootcampPopularityResponseDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDate launchDate;
    private Integer durationInWeeks;
    private int totalEnrolled;
    private List<CapabilityResponseDTO> capabilities;
    private List<EnrolledPersonResponseDTO> enrolledPersons;
}
