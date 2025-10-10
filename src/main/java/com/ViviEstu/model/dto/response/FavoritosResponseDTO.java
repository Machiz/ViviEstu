package com.ViviEstu.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoritosResponseDTO {
    private Long id;
    private Long alojamientoId;
    private Long estudianteId;
}
