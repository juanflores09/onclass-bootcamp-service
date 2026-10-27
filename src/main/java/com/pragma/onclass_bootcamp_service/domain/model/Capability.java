package com.pragma.onclass_bootcamp_service.domain.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Capability {

    private Long id;
    private String name;
    private String description;
    private List<Technology> technologies;

    public Capability(Long id) {
        this.id = id;
    }
}
