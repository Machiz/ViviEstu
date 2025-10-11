package com.ViviEstu.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionesPropieResponseDTO {
    private Long id;
    private Long propietarioId;
    private String mensaje;
    private LocalDate fecha;
}
