package com.ViviEstu.model.dto.request;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class DatosUniversitariosRequestDTO {

    @NotBlank(message = "El correo institucional es obligatorio.")
    @Email(message = "El formato del correo es inválido.")
    @Size(max = 50, message = "El correo no puede exceder los 50 caracteres.")
    private String correoInstitucional;
}