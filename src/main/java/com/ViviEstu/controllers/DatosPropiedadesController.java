package com.ViviEstu.controllers;

import com.ViviEstu.model.dto.request.DatosPropiedadesRequestDTO;
import com.ViviEstu.model.dto.response.DatosPropiedadesResponseDTO;
import com.ViviEstu.service.DatosPropiedadesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/datos-propiedades")
@AllArgsConstructor
public class DatosPropiedadesController {

    private final DatosPropiedadesService datosPropiedadesService;

    // Solo para el administrador (asumiendo que solo el admin gestiona estos datos)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DatosPropiedadesResponseDTO>> getAll() {
        return ResponseEntity.ok(datosPropiedadesService.getAllDatosPropiedades());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DatosPropiedadesResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(datosPropiedadesService.getDatosPropiedadesById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DatosPropiedadesResponseDTO> crearDatosPropiedades(
            @Validated @RequestBody DatosPropiedadesRequestDTO dto) {
        DatosPropiedadesResponseDTO response = datosPropiedadesService.crearDatosPropiedades(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DatosPropiedadesResponseDTO> updateDatosPropiedades(
            @PathVariable Integer id,
            @Validated @RequestBody DatosPropiedadesRequestDTO dto) {
        DatosPropiedadesResponseDTO response = datosPropiedadesService.updateDatosPropiedades(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        datosPropiedadesService.deleteDatosPropiedades(id);
        return ResponseEntity.noContent().build();
    }
}