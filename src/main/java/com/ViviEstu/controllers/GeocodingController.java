package com.ViviEstu.controllers;

import com.ViviEstu.model.dto.response.CoordenadasDTO;
import com.ViviEstu.service.GeocodingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/geocoding")
@AllArgsConstructor
public class GeocodingController {

    private final GeocodingService geocodingService;

    @GetMapping
    public ResponseEntity<CoordenadasDTO> getCoordinatesFromAddress(@RequestParam String address) {
        CoordenadasDTO coordenadas = geocodingService.getCoordinates(address);
        if (coordenadas != null) {
            return ResponseEntity.ok(coordenadas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
