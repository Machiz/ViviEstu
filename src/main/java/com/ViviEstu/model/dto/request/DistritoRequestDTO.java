package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistritoRequestDTO {
    @NotBlank(message = "El nombre del distrito no puede estar vacío")
    private String nombre;

    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @NotNull(message = "El precio promedio no puede ser nulo")
    @Positive(message = "El precio promedio debe ser un número positivo")
    private Integer precioProm;

    @NotBlank(message = "El tipo no puede estar vacío")
    private String tipo;

    @NotBlank(message = "La URL de la imagen no puede estar vacía")
    private String urlImg;

    @NotNull(message = "El nivel de seguridad no puede ser nulo")
    @Min(value = 1, message = "El nivel de seguridad debe ser como mínimo 1")
    @Max(value = 5, message = "El nivel de seguridad debe ser como máximo 5")
    private Integer seguridad;
}
