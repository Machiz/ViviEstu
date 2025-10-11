package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SolicitudRequestDTO {
    @NotNull
    @Positive
    private Integer mesesAlquiler;


    @Positive
    private Integer cantInquilinos;

    private String mensaje;

    @NotNull
    @Positive
    private Double oferta;

    @NotNull
    private Long estudiantesId;

    @NotNull
    private Long alojamientoId;
}
