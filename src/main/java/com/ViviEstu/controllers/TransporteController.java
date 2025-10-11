package com.ViviEstu.controllers;


import com.ViviEstu.model.dto.request.TransporteRequestDTO;
import com.ViviEstu.model.dto.response.TransporteResponseDTO;
import com.ViviEstu.service.TransporteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transportes")
@AllArgsConstructor
public class TransporteController {

    private final TransporteService service;

    @PostMapping
    public ResponseEntity<TransporteResponseDTO> crear(@Valid @RequestBody TransporteRequestDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<TransporteResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransporteResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
