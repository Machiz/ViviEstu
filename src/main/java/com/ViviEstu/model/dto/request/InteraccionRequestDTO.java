package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InteraccionRequestDTO {

    @NotNull(message = "El ID del alojamiento es obligatorio")
    private Long alojamientoId;

    @NotNull(message = "El ID del estudiante es obligatorio")
    private Long estudianteId;

}
