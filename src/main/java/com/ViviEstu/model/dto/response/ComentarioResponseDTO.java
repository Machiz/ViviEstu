package com.ViviEstu.model.dto.response;

import lombok.Data;

@Data
public class ComentarioResponseDTO {
    private Long id;
    private String contenido;
    private String estudianteNombre;
    private String alojamientoTitulo;
}
