package com.ViviEstu.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoritosRequestDTO {
    private Long alojamientoId;
    private Long estudianteId;
}
