package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterEstudianteRequestDTO {

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo debe ser valido")
        String correo;

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 0, message = "La contraseña debe tener al menos 6 caracteres")
        String contrasenia;

        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 20, message = "El nombre no debe exceder los 20 caracteres")
        String nombre;

        @NotBlank(message = "Los apellidos no pueden estar vacío")
        @Size(max = 50, message = "Los apellidos no debe exceder los 50 caracteres")
        String apellidos;

        @NotBlank(message = "El teléfono no puede estar vacío")
        @Pattern(regexp = "[0-9]+", message = "El teléfono debe contener solo dígitos")
        String telefono;

        @NotBlank(message = "La carrera no puede estar vacía")
        String carrera;

        @NotNull(message = "Debe seleccionar un ciclo")
        Integer ciclo;

        @NotNull(message = "El DNI es obligatorio")
        String dni;

        @NotNull(message = "Debe seleccionar un distrito")
        Long distritoId;

        @NotNull(message = "Debe seleccionar una universidad")
        Long universidadId;

        }