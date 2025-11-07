package com.ViviEstu.controllers;

import com.ViviEstu.model.dto.request.SolicitudRequestDTO;
import com.ViviEstu.model.dto.response.SolicitudResponseDTO;
import com.ViviEstu.service.SolicitudService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SolicitudResponseDTO>> listar() {
        return ResponseEntity.ok(solicitudesService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PROPIETARIO') or hasRole('ESTUDIANTE') or hasRole('ADMIN')")
    public ResponseEntity<SolicitudResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudesService.obtenerPorId(id));
    }

    @PutMapping("/{id}/aceptar")
    @PreAuthorize("hasRole('PROPIETARIO') or hasRole('ADMIN')")
    public ResponseEntity<SolicitudResponseDTO> aceptarSolicitud(@PathVariable Long id) {
        SolicitudResponseDTO response = solicitudesService.actualizarEstado(id, "ACEPTADO");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/rechazar")
    @PreAuthorize("hasRole('PROPIETARIO') or hasRole('ADMIN')")
    public ResponseEntity<SolicitudResponseDTO> rechazarSolicitud(@PathVariable Long id) {
        SolicitudResponseDTO response = solicitudesService.actualizarEstado(id, "RECHAZADO");
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
    public ResponseEntity<SolicitudResponseDTO> registrar(@Valid @RequestBody SolicitudRequestDTO request) {
        return ResponseEntity.ok(solicitudesService.registrar(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
    public ResponseEntity<SolicitudResponseDTO> actualizar(@PathVariable Long id,
                                                           @Valid @RequestBody SolicitudRequestDTO request) {
        return ResponseEntity.ok(solicitudesService.actualizar(id, request));
    }

    @GetMapping("/estudiante/{id}")
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
    public ResponseEntity<List<SolicitudResponseDTO>> obtenerPorEstudianteId(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudesService.obtenerPorEstudianteId(id));
    }

    @GetMapping("/propietario/{id}")
    @PreAuthorize("hasRole('PROPIETARIO') or hasRole('ADMIN')")
    public ResponseEntity<List<SolicitudResponseDTO>> obtenerPorPropietarioId(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudesService.obtenerPorPropietarioId(id));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        solicitudesService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
