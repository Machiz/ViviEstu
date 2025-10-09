package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UniversidadRequestDTO {
    @NotBlank(message = "El nombre de la universidad es obligatorio")
    private String nombre;
    @NotNull(message = "El id del distrito es obligatorio")
    private Long distritoId;
}
