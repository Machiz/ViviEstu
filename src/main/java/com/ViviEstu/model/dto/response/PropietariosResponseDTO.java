package com.ViviEstu.model.dto.response;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PropietariosResponseDTO {
    private String nombre;
    private String correo;
    private String telefono;
    private String contrasenia;
    private String dni;

}

