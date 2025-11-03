package com.ViviEstu.controllers;

import com.ViviEstu.model.dto.response.FavoritosResponseDTO;
import com.ViviEstu.service.FavoritosService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favoritos")
@AllArgsConstructor
public class FavoritosController {

    private final FavoritosService favoritosService;

    @GetMapping("/estudiante/{estudianteId}")
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
    public ResponseEntity<List<FavoritosResponseDTO>> getFavoritosByEstudianteId(@PathVariable Long estudianteId) {
        List<FavoritosResponseDTO> favoritos = favoritosService.getFavoritosByEstudianteId(estudianteId);
        return new ResponseEntity<>(favoritos, HttpStatus.OK);
    }

    @PostMapping("/estudiante/{estudianteId}/alojamiento/{alojamientoId}")
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
    public ResponseEntity<FavoritosResponseDTO> addFavorito(@PathVariable Long estudianteId, @PathVariable Long alojamientoId) {
        FavoritosResponseDTO nuevoFavorito = favoritosService.addFavorito(estudianteId, alojamientoId);
        return new ResponseEntity<>(nuevoFavorito, HttpStatus.CREATED);
    }

    @DeleteMapping("/estudiante/{estudianteId}/alojamiento/{alojamientoId}")
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
    public ResponseEntity<Void> removeFavoritoByEstudianteAndAlojamiento(
            @PathVariable Long estudianteId, @PathVariable Long alojamientoId) {
        favoritosService.removeFavorito(estudianteId, alojamientoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
