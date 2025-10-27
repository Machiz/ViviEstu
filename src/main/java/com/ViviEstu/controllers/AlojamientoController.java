package com.ViviEstu.controllers;

import com.ViviEstu.model.dto.request.AlojamientoRequestDTO;
import com.ViviEstu.model.dto.response.AlojamientoResponseDTO;
import com.ViviEstu.service.AlojamientoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/alojamientos")
@AllArgsConstructor
public class AlojamientoController {

    private final AlojamientoService alojamientoService;

    @GetMapping
    public ResponseEntity<List<AlojamientoResponseDTO>> getAll() {
        return ResponseEntity.ok(alojamientoService.getAllAlojamientos());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AlojamientoResponseDTO> crearAlojamiento(
            @Validated @ModelAttribute AlojamientoRequestDTO dto) throws IOException {

        AlojamientoResponseDTO response = alojamientoService.crearAlojamiento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlojamientoResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(alojamientoService.getAlojamientoById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        alojamientoService.deleteAlojamiento(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlojamientoResponseDTO> updateAlojamiento(
            @PathVariable Long id,
            @RequestBody AlojamientoRequestDTO dto) {
        AlojamientoResponseDTO response = alojamientoService.updateAlojamiento(id, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/{id}/imagenes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AlojamientoResponseDTO> agregarImagenes(
            @PathVariable Long id,
            @RequestPart("imagenes") List<MultipartFile> nuevasImagenes) throws IOException {
        AlojamientoResponseDTO response = alojamientoService.agregarImagenes(id, nuevasImagenes);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{alojamientoId}/imagenes/{imagenId}")
    public ResponseEntity<String> eliminarImagen(
            @PathVariable Long alojamientoId,
            @PathVariable Long imagenId) throws IOException {

        alojamientoService.eliminarImagen(alojamientoId, imagenId);
        return ResponseEntity.ok("Imagen eliminada correctamente");
    }

}
