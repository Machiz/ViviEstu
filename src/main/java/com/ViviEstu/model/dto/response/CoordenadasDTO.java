package com.ViviEstu.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar un par de coordenadas geográficas (latitud y longitud).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoordenadasDTO {
    private double latitude;
    private double longitude;
}

