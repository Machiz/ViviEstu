package com.ViviEstu.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InteraccionResponseDTO {

    private Integer id;
    private Long alojamientoId;
    private Long estudianteId;
    private LocalDateTime fecha;
}
