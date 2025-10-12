package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SolicitudRequestDTO {

    @NotNull(message = "El número de meses de alquiler es obligatorio.")
    @Positive(message = "Los meses de alquiler deben ser un número positivo.")
    private Integer mesesAlquiler;

    @Positive(message = "La cantidad de inquilinos debe ser un número positivo.")
    private Integer cantInquilinos;

    private String mensaje;

    @NotNull(message = "La oferta no puede ser nula.")
    @Positive(message = "La oferta debe ser un valor positivo.")
    private Double oferta;

    @NotNull(message = "Debe especificar el ID del estudiante.")
    private Long estudiantesId;

    @NotNull(message = "Debe especificar el ID del alojamiento.")
    private Long alojamientoId;
}
