package com.ViviEstu.controllers;

import com.ViviEstu.model.dto.request.AlojamientoRequestDTO;
import com.ViviEstu.model.dto.response.AlojamientoResponseDTO;
import com.ViviEstu.service.AlojamientoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alojamientos")
@AllArgsConstructor
public class AlojamientoController {

    private final AlojamientoService service;

    @GetMapping
    public ResponseEntity<List<AlojamientoResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllAlojamientos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlojamientoResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAlojamientoById(id));
    }

    @PostMapping
    public ResponseEntity<AlojamientoResponseDTO> create(@Validated @RequestBody AlojamientoRequestDTO dto) {
        return new ResponseEntity<>(service.createAlojamiento(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlojamientoResponseDTO> update(@PathVariable Long id,
                                                         @Validated @RequestBody AlojamientoRequestDTO dto) {
        return ResponseEntity.ok(service.updateAlojamiento(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteAlojamiento(id);
        return ResponseEntity.noContent().build();
    }
}
