package com.ViviEstu.controllers;

import com.ViviEstu.model.dto.request.DistritoRequestDTO;
import com.ViviEstu.model.dto.response.DistritoResponseDTO;
import com.ViviEstu.service.DistritoService;
import com.ViviEstu.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/distritos")
@AllArgsConstructor
public class DistritoController {
    private DistritoService distritoService;

    @GetMapping
    public ResponseEntity<List<DistritoResponseDTO>> getAllDistritos() {
        List<DistritoResponseDTO> distritos = distritoService.listAll();
        return ResponseEntity.ok(distritos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DistritoResponseDTO> getDistritoById(@PathVariable Long id) {
        DistritoResponseDTO distrito = distritoService.getDistritoById(id);
        return ResponseEntity.ok(distrito);
    }

    @PostMapping
    public ResponseEntity<DistritoResponseDTO> createDistrito(@Validated @RequestBody DistritoRequestDTO requestDTO) {
        DistritoResponseDTO created = distritoService.createDistrito(requestDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DistritoResponseDTO> updateDistrito(@PathVariable Long id, @RequestBody DistritoRequestDTO requestDTO) {
        DistritoResponseDTO updated = distritoService.updateDistrito(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistrito(@PathVariable Long id) {
        distritoService.deleteDistrito(id);
        return ResponseEntity.noContent().build();
    }
}
