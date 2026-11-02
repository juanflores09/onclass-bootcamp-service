package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CreateBootcampRequestDTO (
        @NotBlank(message = "El nombre del bootcamp no puede estar vacío")
        @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
        String name,

        @NotBlank(message = "La descripción no puede estar vacía")
        @Size(max = 200, message = "La descripción no debe superar los 200 caracteres")
        String description,

        @NotNull(message = "La fecha de lanzamiento es obligatoria")
        @FutureOrPresent(message = "La fecha de lanzamiento debe ser presente o futura")
        LocalDate launchDate,

        @NotNull(message = "La duración es obligatoria")
        Integer durationInWeeks,

        @NotNull(message = "La lista de capacidades no puede ser nula")
        @Size(min = 1, max = 4, message = "El bootcamp debe tener como mínimo 1 capacidad y como máximo 4")
        List<Long> capabilities
){
}
