package com.pragma.onclass_bootcamp_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class MostPopularEnrollmentAggregate {

    private Long bootcampId;
    private int totalEnrolled;
    private List<EnrolledPerson> enrolledPersons;
}
