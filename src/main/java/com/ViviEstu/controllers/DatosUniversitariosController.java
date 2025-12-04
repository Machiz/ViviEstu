package com.ViviEstu.controllers;

import com.ViviEstu.model.dto.request.DatosUniversitariosRequestDTO;
import com.ViviEstu.model.dto.response.DatosUniversitariosResponseDTO;
import com.ViviEstu.service.DatosUniversitariosService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/datos-universitarios")
@AllArgsConstructor
public class DatosUniversitariosController {

    private final DatosUniversitariosService datosUniversitariosService;

    // Solo para el administrador (asumiendo que solo el admin gestiona estos datos)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DatosUniversitariosResponseDTO>> getAll() {
        return ResponseEntity.ok(datosUniversitariosService.getAllDatosUniversitarios());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DatosUniversitariosResponseDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(datosUniversitariosService.getDatosUniversitariosById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DatosUniversitariosResponseDTO> crearDatosUniversitarios(
            @Validated @RequestBody DatosUniversitariosRequestDTO dto) {
        DatosUniversitariosResponseDTO response = datosUniversitariosService.crearDatosUniversitarios(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DatosUniversitariosResponseDTO> updateDatosUniversitarios(
            @PathVariable Integer id,
            @Validated @RequestBody DatosUniversitariosRequestDTO dto) {
        DatosUniversitariosResponseDTO response = datosUniversitariosService.updateDatosUniversitarios(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        datosUniversitariosService.deleteDatosUniversitarios(id);
        return ResponseEntity.noContent().build();
    }
}