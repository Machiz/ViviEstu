package com.ViviEstu.model.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class DatosPropiedadesRequestDTO {

    @NotBlank(message = "El número de partida es obligatorio.")
    @Size(min = 8, max = 8, message = "El número de partida debe tener 8 caracteres.")
    private String nroPartida;

    @NotBlank(message = "El DNI del propietario es obligatorio.")
    @Size(min = 8, max = 8, message = "El DNI del propietario debe tener 8 caracteres.")
    private String dniPropietario;
}