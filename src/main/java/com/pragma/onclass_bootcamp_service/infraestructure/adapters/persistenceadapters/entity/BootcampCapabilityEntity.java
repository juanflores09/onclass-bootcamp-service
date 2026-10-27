package com.pragma.onclass_bootcamp_service.infraestructure.adapters.persistenceadapters.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Setter
@Getter
@Table("bootcamp_capability")
public class BootcampCapabilityEntity {

    @Id
    private Long id;
    private Long bootcampId;
    private Long capabilityId;
}
