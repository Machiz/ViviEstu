package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioRequestDTO {

    @NotNull(message = "El ID del alojamiento es obligatorio")
    private Long alojamientoId;

    @NotNull(message = "El ID del estudiante es obligatorio")
    private Long estudianteId;

    @NotBlank(message = "El contenido del comentario no puede estar vacío")
    @jakarta.validation.constraints.Size(max = 500, message = "El comentario no puede exceder los 500 caracteres")
    private String contenido;
}
