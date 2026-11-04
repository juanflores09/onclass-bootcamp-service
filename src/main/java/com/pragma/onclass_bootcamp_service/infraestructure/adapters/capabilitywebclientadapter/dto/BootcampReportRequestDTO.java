package com.pragma.onclass_bootcamp_service.infraestructure.adapters.capabilitywebclientadapter.dto;

import java.time.LocalDate;

public record BootcampReportRequestDTO (
        Long bootcampId,
        String name,
        String description,
        LocalDate launchDate,
        Integer durationInWeeks,
        Integer capabilityCount,
        Integer technologyCount
){
}
