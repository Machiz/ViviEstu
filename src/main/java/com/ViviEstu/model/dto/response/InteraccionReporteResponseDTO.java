package com.ViviEstu.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteraccionReporteResponseDTO {

    private Long alojamientoId;
    private String nombreAlojamiento;

    private Long totalInteracciones;
    private Long estudiantesUnicos;
    private LocalDateTime ultimaInteraccion;

    private String universidadPrincipal; // universidad con más estudiantes
    private String distritoPrincipal; // distrito más frecuente
    private Double promedioInteraccionesPorEstudiante;
}