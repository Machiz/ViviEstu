package com.ViviEstu.controllers;


import com.ViviEstu.model.dto.request.InteraccionRequestDTO;
import com.ViviEstu.model.dto.response.InteraccionResponseDTO;
import com.ViviEstu.service.InteraccionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<InteraccionResponseDTO>> getAllInteracciones() {
        List<InteraccionResponseDTO> interacciones = interaccionService.getAllInteracciones();
        return new ResponseEntity<>(interacciones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InteraccionResponseDTO> getInteraccionById(@PathVariable Integer id) {
        InteraccionResponseDTO interaccion = interaccionService.getInteraccionById(id);
        return new ResponseEntity<>(interaccion, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InteraccionResponseDTO> createInteraccion(
            @Validated @RequestBody InteraccionRequestDTO requestDTO) {
        InteraccionResponseDTO created = interaccionService.createInteraccion(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InteraccionResponseDTO> updateInteraccion(
            @PathVariable Integer id,
            @Validated @RequestBody InteraccionRequestDTO requestDTO) {
        InteraccionResponseDTO updated = interaccionService.updateInteraccion(id, requestDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInteraccion(@PathVariable Integer id) {
        interaccionService.deleteInteraccion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
