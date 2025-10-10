package com.ViviEstu.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlojamientoResponseDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String direccion;
    private BigDecimal precioMensual;
    private Boolean alquilado;
    private String propietario;
    private String zona;
    private Timestamp fecha;
}
