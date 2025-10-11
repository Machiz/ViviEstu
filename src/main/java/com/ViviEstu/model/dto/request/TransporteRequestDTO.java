package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TransporteRequestDTO {

    @NotNull(message = "El ID de la zona es obligatorio")
    private Long zonaId;

    @NotNull(message = "El nombre del medio de transporte es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;
}
