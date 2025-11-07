package com.ViviEstu.model.dto.response;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class SolicitudResponseDTO {
    private Long id;
    private Integer mesesAlquiler;
    private Integer cantInquilinos;
    private String mensaje;
    private Double oferta;
    private String estado;
    private Long estudiantesId;
    private String nombreEstudiante;
    private Long alojamientoId;
    private String tituloAlojamiento;
}
