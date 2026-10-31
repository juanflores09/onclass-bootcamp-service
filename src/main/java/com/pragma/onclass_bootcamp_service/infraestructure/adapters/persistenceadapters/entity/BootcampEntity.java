package com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.entity;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@Table("bootcamp")
public class BootcampEntity {

    @Id
    private Long id;
    private String name;
    private String description;
    private LocalDate launchDate;
    private Integer durationInWeeks;
    private Integer capabilityCount;
}
