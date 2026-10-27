package com.pragma.onclass_bootcamp_service.domain.model;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Bootcamp {

    private Long id;
    private String name;
    private String description;
    private LocalDate launchDate;
    private Integer durationInWeeks;
    private List<Capability> capabilities;

    public Bootcamp(Long id,  String name, String description, LocalDate launchDate, Integer durationInWeeks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.launchDate = launchDate;
        this.durationInWeeks = durationInWeeks;
    }
}
