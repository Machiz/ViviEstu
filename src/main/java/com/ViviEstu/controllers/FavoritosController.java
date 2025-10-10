package com.ViviEstu.controllers;

import com.ViviEstu.model.dto.request.FavoritosRequestDTO;
import com.ViviEstu.model.dto.response.FavoritosResponseDTO;
import com.ViviEstu.service.FavoritosService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes/{estudianteId}/favoritos")
@AllArgsConstructor
public class FavoritosController {

    private FavoritosService favoritosService;

    @GetMapping
    public ResponseEntity<List<FavoritosResponseDTO>> getFavoritosByEstudianteId(@PathVariable Long estudianteId) {
        List<FavoritosResponseDTO> favoritos = favoritosService.getFavoritosByEstudianteId(estudianteId);
        return ResponseEntity.ok(favoritos);
    }

    @PostMapping
    public ResponseEntity<FavoritosResponseDTO> addFavorito(@PathVariable Long estudianteId, @RequestBody FavoritosRequestDTO requestDTO) {
        // Asegurarse de que el ID del estudiante en la URL coincide con el del DTO
        if (!estudianteId.equals(requestDTO.getEstudianteId())) {
            throw new IllegalArgumentException("El ID del estudiante en la URL no coincide con el del cuerpo de la solicitud.");
        }
        FavoritosResponseDTO nuevoFavorito = favoritosService.addFavorito(requestDTO);
        return new ResponseEntity<>(nuevoFavorito, HttpStatus.CREATED);
    }

    @DeleteMapping("/{alojamientoId}")
    public ResponseEntity<Void> removeFavorito(@PathVariable Long estudianteId, @PathVariable Long alojamientoId) {
        favoritosService.removeFavorito(estudianteId, alojamientoId);
        return ResponseEntity.noContent().build();
    }
}
