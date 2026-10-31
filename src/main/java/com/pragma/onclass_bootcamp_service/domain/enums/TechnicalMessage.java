package com.pragma.onclass_bootcamp_service.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TechnicalMessage {

    INTERNAL_ERROR("500", "Something went wrong, please try again"),
    INTERNAL_ERROR_IN_ADAPTERS("500", "Something went wrong in adapters, please try again"),
    BOOTCAMP_ALREADY_EXISTS("400", "Ya existe un bootcamp con ese nombre"),
    INVALID_PARAMETERS("400", "Parámetros inválidos"),
    CAPABILITY_NOT_FOUND("400", "La capacidad asociada no existe"),
    BOOTCAMP_NOT_FOUND("400", "El bootcamp no existe");

    private final String code;
    private final String message;

}
