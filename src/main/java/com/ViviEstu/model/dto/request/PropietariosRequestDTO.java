package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropietariosRequestDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;
    @NotBlank(message = "El DNI no puede estar vacío")
    @Size(max = 8, message = "El DNI no puede tener más de 8 caracteres")
    private String dni;
    @NotBlank(message = "El teléfono no puede estar vacío")
    @Size(max = 9, message = "El teléfono no puede tener más de 9 digitos")
    private String telefono;
    @NotBlank(message = "El email no puede estar vacío")
    @Size(max = 50, message = "El email no puede tener más de 50 caracteres")
    private String email;
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(max = 50, message = "La contraseña no puede tener más de 50 caracteres")
    private String contrasenia;

}
