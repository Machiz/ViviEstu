package com.ViviEstu.model.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistritoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Integer precioProm;
    private String tipo;
    private String urlImg;
    private Integer seguridad;
}

