package com.ViviEstu.model.dto.response;

import lombok.Data;

@Data
public class PropietariosResponseDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
    private String dni;
}
