package com.ViviEstu.controllers;

import com.ViviEstu.model.dto.request.FavoritosRequestDTO;
import com.ViviEstu.model.dto.response.FavoritosResponseDTO;
import com.ViviEstu.service.FavoritosService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoritos")
@AllArgsConstructor
public class FavoritosController {

    private final FavoritosService favoritosService;

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<FavoritosResponseDTO>> getFavoritosByEstudianteId(@PathVariable Long estudianteId) {
        List<FavoritosResponseDTO> favoritos = favoritosService.getFavoritosByEstudianteId(estudianteId);
        return ResponseEntity.ok(favoritos);
    }

    @PostMapping
    public ResponseEntity<FavoritosResponseDTO> addFavorito(@RequestBody FavoritosRequestDTO requestDTO) {
        FavoritosResponseDTO nuevoFavorito = favoritosService.addFavorito(requestDTO);
        return new ResponseEntity<>(nuevoFavorito, HttpStatus.CREATED);
    }

    @DeleteMapping("/estudiante/{estudianteId}/alojamiento/{alojamientoId}")
    public ResponseEntity<Void> removeFavoritoByEstudianteAndAlojamiento(
            @PathVariable Long estudianteId, @PathVariable Long alojamientoId) {
        favoritosService.removeFavorito(estudianteId, alojamientoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{favoritoId}")
    public ResponseEntity<Void> removeFavoritoById(@PathVariable Long favoritoId) {
        favoritosService.removeFavoritoById(favoritoId);
        return ResponseEntity.noContent().build();
    }
}
