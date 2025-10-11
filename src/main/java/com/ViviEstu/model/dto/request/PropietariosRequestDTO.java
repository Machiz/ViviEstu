package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PropietariosRequestDTO {

    @NotBlank
    private String nombre;

    @Email
    private String correo;

    @NotBlank
    private String contrasenia;

    @NotBlank
    private String telefono;

    @NotBlank
    private String urlPerfil;

    private Integer dni;
}
