package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PropietariosRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;

    @Email(message = "El correo debe tener un formato válido")
    @NotBlank(message = "El correo no puede estar vacío")
    private String correo;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String contrasenia;

    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;

    @NotBlank(message = "La URL del perfil no puede estar vacía")
    private String urlPerfil;

    private Integer dni;
}
