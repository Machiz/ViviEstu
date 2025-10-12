package com.ViviEstu.controllers;


import com.ViviEstu.model.dto.request.ComentarioRequestDTO;
import com.ViviEstu.model.dto.response.ComentarioResponseDTO;
import com.ViviEstu.service.ComentarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
@AllArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    @PostMapping
    public ResponseEntity<ComentarioResponseDTO> crearComentario(@RequestBody ComentarioRequestDTO request) {
        ComentarioResponseDTO response = comentarioService.registrar(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/alojamiento/{alojamientoId}")
    public ResponseEntity<List<ComentarioResponseDTO>> listarPorAlojamiento(@PathVariable Long alojamientoId) {
        return ResponseEntity.ok(comentarioService.listarPorAlojamiento(alojamientoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        comentarioService.eliminarComentario(id);
        return ResponseEntity.noContent().build();
    }
}
