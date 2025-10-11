package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlojamientoRequestDTO {

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotNull(message = "El precio mensual es obligatorio")
    private BigDecimal precioMensual;

    @NotNull(message = "Debe indicar si está alquilado")
    private Boolean alquilado;

    @NotNull(message = "El ID del propietario es obligatorio")
    private Long propietarioId;

    @NotNull(message = "El ID de la zona es obligatorio")
    private Long distritoId;
}
