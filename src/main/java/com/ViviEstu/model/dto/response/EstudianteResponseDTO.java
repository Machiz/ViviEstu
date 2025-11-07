package com.ViviEstu.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstudianteResponseDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
    private String carrera;
    private Integer ciclo;
    private String dni;
    private String distrito;
    private String universidad;
}
