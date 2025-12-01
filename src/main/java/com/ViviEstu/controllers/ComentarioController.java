package com.ViviEstu.controllers;


import com.ViviEstu.model.dto.request.ComentarioRequestDTO;
import com.ViviEstu.model.dto.response.ComentarioResponseDTO;
import com.ViviEstu.service.ComentarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentarios")
@AllArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ComentarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(comentarioService.listarTodos());
    }

    @PostMapping
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
    public ResponseEntity<ComentarioResponseDTO> crearComentario(@RequestBody ComentarioRequestDTO request) {
        ComentarioResponseDTO response = comentarioService.registrar(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/alojamiento/{alojamientoId}")
    @PreAuthorize("hasRole('PROPIETARIO') or hasRole('ESTUDIANTE') or hasRole('ADMIN')")
    public ResponseEntity<List<ComentarioResponseDTO>> listarPorAlojamiento(@PathVariable Long alojamientoId) {
        return ResponseEntity.ok(comentarioService.listarPorAlojamiento(alojamientoId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        comentarioService.eliminarComentario(id);
        return ResponseEntity.noContent().build();
    }
}
