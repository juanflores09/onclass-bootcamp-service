package com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MostPopularEnrollmentDTO {

    private Long bootcampId;
    private int totalEnrolled;
    private List<EnrolledPersonDTO> enrolledPersons;
}
