package com.ViviEstu.controllers;


import com.ViviEstu.model.dto.request.InteraccionRequestDTO;
import com.ViviEstu.model.dto.response.InteraccionReporteResponseDTO;
import com.ViviEstu.model.dto.response.InteraccionResponseDTO;
import com.ViviEstu.service.InteraccionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// http://localhost:8080/api/v1/interacciones
@RestController
@RequestMapping("/interacciones")
@AllArgsConstructor
public class InteraccionController {

    private final InteraccionService interaccionService;

    @GetMapping
    @PreAuthorize("hasRole('PROPIETARIO') or hasRole('ADMIN')")
    public ResponseEntity<List<InteraccionResponseDTO>> getAllInteracciones() {
        List<InteraccionResponseDTO> interacciones = interaccionService.getAllInteracciones();
        return new ResponseEntity<>(interacciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PROPIETARIO') or hasRole('ADMIN')")
    public ResponseEntity<InteraccionResponseDTO> getInteraccionById(@PathVariable Integer id) {
        InteraccionResponseDTO interaccion = interaccionService.getInteraccionById(id);
        return new ResponseEntity<>(interaccion, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
    public ResponseEntity<InteraccionResponseDTO> createInteraccion(
            @Validated @RequestBody InteraccionRequestDTO requestDTO) {
        InteraccionResponseDTO created = interaccionService.createInteraccion(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/contar/{alojamientoId}")
    @PreAuthorize("hasRole('PROPIETARIO') or hasRole('ADMIN')")
    public ResponseEntity<Long> contarPorAlojamiento(@PathVariable Long alojamientoId) {
        long total = interaccionService.contarPorAlojamiento(alojamientoId);
        return ResponseEntity.ok(total);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InteraccionResponseDTO> updateInteraccion(
            @PathVariable Integer id,
            @Validated @RequestBody InteraccionRequestDTO requestDTO) {
        InteraccionResponseDTO updated = interaccionService.updateInteraccion(id, requestDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteInteraccion(@PathVariable Integer id) {
        interaccionService.deleteInteraccion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/reporte/{alojamientoId}")
    @PreAuthorize("hasRole('PROPIETARIO') or hasRole('ADMIN')")
    public ResponseEntity<InteraccionReporteResponseDTO> generarReportePorAlojamiento(@PathVariable Long alojamientoId) {
        return ResponseEntity.ok(interaccionService.generarReportePorAlojamiento(alojamientoId));
    }
}
