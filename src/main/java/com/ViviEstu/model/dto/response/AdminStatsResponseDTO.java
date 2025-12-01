package com.ViviEstu.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminStatsResponseDTO {
    private Long totalUsuarios;
    private Long totalPropietarios;
    private Long totalAlojamientos;
    private Long totalUniversidades;
}