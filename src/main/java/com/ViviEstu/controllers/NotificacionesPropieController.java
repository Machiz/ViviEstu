package com.ViviEstu.controllers;


import com.ViviEstu.model.dto.request.NotificacionesPropieRequestDTO;
import com.ViviEstu.model.dto.response.NotificacionesPropieResponseDTO;
import com.ViviEstu.service.NotificacionesPropieService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones-propietario")
@AllArgsConstructor
public class NotificacionesPropieController {

    private final NotificacionesPropieService service;

    @GetMapping
    public ResponseEntity<List<NotificacionesPropieResponseDTO>> getAllNotificaciones() {
        return ResponseEntity.ok(service.getAllNotificaciones());
    }

    @GetMapping("/propietario/{id}")
    public ResponseEntity<List<NotificacionesPropieResponseDTO>> getByPropietario(@PathVariable Long id) {
        return ResponseEntity.ok(service.getNotificacionesByPropietarioId(id));
    }

    @PostMapping
    public ResponseEntity<NotificacionesPropieResponseDTO> createNotificacion(
            @Validated @RequestBody NotificacionesPropieRequestDTO dto) {
        return new ResponseEntity<>(service.createNotificacion(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificacion(@PathVariable Long id) {
        service.deleteNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}
