package com.ViviEstu.controllers;


import com.ViviEstu.model.dto.request.EstudiantesRequestDTO;
import com.ViviEstu.model.dto.response.EstudianteResponseDTO;
import com.ViviEstu.service.EstudiantesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// http://localhost:8080/api/v1/estudiantes
@RestController
@RequestMapping("/estudiantes")
@AllArgsConstructor
public class EstudiantesController {

    private final EstudiantesService estudiantesService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EstudianteResponseDTO>> getAllEstudiantes() {
        List<EstudianteResponseDTO> estudiantes = estudiantesService.getAllEstudiantes();
        return new ResponseEntity<>(estudiantes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteResponseDTO> getEstudianteById(@PathVariable long id) {
        EstudianteResponseDTO estudiante = estudiantesService.getEstudianteById(id);
        return new ResponseEntity<>(estudiante, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteResponseDTO> createEstudiante(@Validated @RequestBody EstudiantesRequestDTO requestDTO) {
        EstudianteResponseDTO created = estudiantesService.createEstudiante(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
    public ResponseEntity<EstudianteResponseDTO> updateEstudiante(
            @PathVariable long id,
            @Validated @RequestBody EstudiantesRequestDTO requestDTO) {
        EstudianteResponseDTO updated = estudiantesService.updateEstudiante(id, requestDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEstudiante(@PathVariable long id) {
        estudiantesService.deleteEstudiante(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
