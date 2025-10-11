package com.ViviEstu.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionesPropieRequestDTO {

    @NotNull(message = "El ID del propietario es obligatorio")
    private Long propietarioId;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
}
