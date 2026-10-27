package com.pragma.onclass_bootcamp_service.domain.model;

import java.util.List;

public record PageResult <T>(
        List<T> content,
        long totalElements
){
}
