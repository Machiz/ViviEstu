package com.ViviEstu.model.dto.response;

import lombok.Data;

@Data
public class SolicitudResponseDTO {
    private Long id;
    private Integer mesesAlquiler;
    private Integer cantInquilinos;
    private String mensaje;
    private Double oferta;

    // Datos relacionados
    private Long estudiantesId;
    private String nombreEstudiante;
    private Long alojamientoId;
    private String tituloAlojamiento;
}
