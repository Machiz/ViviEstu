package com.ViviEstu.controllers;

import com.ViviEstu.model.dto.request.SolicitudRequestDTO;
import com.ViviEstu.model.dto.response.SolicitudResponseDTO;
import com.ViviEstu.service.SolicitudService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")

public class SolicitudController {
    private final SolicitudService solicitudesService;

    public SolicitudController(SolicitudService solicitudesService) {
        this.solicitudesService = solicitudesService;
    }

    @GetMapping
    public ResponseEntity<List<SolicitudResponseDTO>> listar() {
        return ResponseEntity.ok(solicitudesService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudesService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<SolicitudResponseDTO> registrar(@Valid @RequestBody SolicitudRequestDTO request) {
        return ResponseEntity.ok(solicitudesService.registrar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudResponseDTO> actualizar(@PathVariable Long id,
                                                           @Valid @RequestBody SolicitudRequestDTO request) {
        return ResponseEntity.ok(solicitudesService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        solicitudesService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
