package com.pragma.onclass_bootcamp_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class EnrolledPerson {

    private String name;
    private String email;
}
