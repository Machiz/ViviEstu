package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TransporteRequestDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;
    @NotBlank(message = "El alojamientoId no puede estar vacío")
    @Size(max = 11, message = "El alojamientoId no puede tener más de 11 caracteres")
    private Integer alojamientoId;
}
