package com.pragma.onclass_bootcamp_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BootcampCapabilityAggregate {

    Long bootcampId;
    List<Capability> capabilities;
}
