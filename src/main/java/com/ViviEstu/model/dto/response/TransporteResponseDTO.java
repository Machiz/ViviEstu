package com.ViviEstu.model.dto.response;

import lombok.Data;

@Data
public class TransporteResponseDTO {
    private Long id;
    private String nombre;
    private String tipo;
    private String horario;
    private Long distritoId;       // 🔹 agrega este campo
    private String distritoNombre;
}
