package com.pragma.onclass_bootcamp_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class BootcampPopularity {

    private Long id;
    private String name;
    private String description;
    private LocalDate launchDate;
    private Integer durationInWeeks;
    private int totalEnrolled;
    private List<Capability> capabilities;
    private List<EnrolledPerson> enrolledPersons;
}
