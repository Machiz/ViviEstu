package com.ViviEstu.model.dto.response;

import com.ViviEstu.model.entity.ImagenesAlojamiento;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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
    private String distrito;
    private String nroPartida;
    private Timestamp fecha;
    private List<ImagenResponseDTO> imagenes;
    private List<String> transportes;
    private List<String> universidades;
}
