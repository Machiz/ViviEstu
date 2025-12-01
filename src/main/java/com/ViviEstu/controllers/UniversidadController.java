package com.ViviEstu.controllers;


import com.ViviEstu.model.dto.request.UniversidadRequestDTO;
import com.ViviEstu.model.dto.response.UniversidadResponseDTO;
import com.ViviEstu.service.UniversidadService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/universidades")
@AllArgsConstructor

public class UniversidadController {
    private final UniversidadService universidadService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UniversidadResponseDTO> crear(@Valid @RequestBody UniversidadRequestDTO request) {
        UniversidadResponseDTO creada = universidadService.crear(request);
        return ResponseEntity.status(201).body(creada);
    }

    @GetMapping
    public ResponseEntity<List<UniversidadResponseDTO>> listar() {
        return ResponseEntity.ok(universidadService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN') or hasRole('PROPIETARIO')")
    public ResponseEntity<UniversidadResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(universidadService.obtenerPorId(id));
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN') or hasRole('PROPIETARIO')")
    public ResponseEntity<List<UniversidadResponseDTO>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(universidadService.buscarPorNombre(nombre));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UniversidadResponseDTO> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody UniversidadRequestDTO request) {
        return ResponseEntity.ok(universidadService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        universidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
