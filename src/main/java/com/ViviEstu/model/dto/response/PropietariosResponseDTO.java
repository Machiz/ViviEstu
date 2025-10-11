package com.ViviEstu.model.dto.response;

import lombok.Data;

@Data
public class PropietariosResponseDTO {
    private Long id;
    private String nombre;
    private String correo;
    private String telefono;
    private String urlPerfil;
    private Integer dni;
}
