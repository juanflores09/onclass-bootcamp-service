package com.pragma.onclass_bootcamp_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bootcamp {

    private Long id;
    private String name;
    private String description;
    private LocalDate launchDate;
    private Integer durationInWeeks;
    private List<Long> capabilities;

}
