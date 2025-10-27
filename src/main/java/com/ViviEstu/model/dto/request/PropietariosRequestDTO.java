package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PropietariosRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidos;

    @Email(message = "Correo no válido")
    @NotBlank(message = "El correo no puede estar vacío")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasenia;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotNull(message = "El DNI es obligatorio")
    private Integer dni;

}
