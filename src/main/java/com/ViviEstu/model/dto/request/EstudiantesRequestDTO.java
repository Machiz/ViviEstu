package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstudiantesRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 20, message = "El nombre no debe exceder los 20 caracteres")
    private String nombre;

    @NotBlank(message = "Los apellidos no pueden estar vacío")
    @Size(max = 50, message = "Los apellidos no debe exceder los 50 caracteres")
    private String apellidos;

    @Email(message = "El correo debe tener un formato válido")
    private String correo;

    @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    private String contrasenia;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Pattern(regexp = "[0-9]+", message = "El teléfono debe contener solo dígitos")
    private String telefono;

    @NotBlank(message = "La carrera no puede estar vacía")
    private String carrera;

    @NotNull(message = "Debe seleccionar un ciclo")
    private Integer ciclo;

    @NotNull(message = "El DNI es obligatorio")
    private String dni;

    @NotNull(message = "Debe seleccionar un distrito")
    private Long distritoId;

    @NotNull(message = "Debe seleccionar una universidad")
    private Long universidadId;
}
