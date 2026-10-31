package com.pragma.onclass_bootcamp_service.domain.exceptions;

import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto.ApiResponseDTO;
import lombok.Getter;

@Getter
public class ExternalServiceException extends RuntimeException{

    private final transient ApiResponseDTO errorResponse;

    public ExternalServiceException(ApiResponseDTO errorResponse) {
        super(errorResponse != null ? errorResponse.getMessage() : "External service error");
        this.errorResponse = errorResponse;
    }
}
