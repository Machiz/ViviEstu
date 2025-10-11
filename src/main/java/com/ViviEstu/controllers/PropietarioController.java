package com.ViviEstu.controllers;


import com.ViviEstu.mapper.PropietariosMapper;
import com.ViviEstu.model.dto.request.PropietariosRequestDTO;
import com.ViviEstu.model.dto.response.PropietariosResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.ImagenesAlojamiento;
import com.ViviEstu.model.entity.Propietarios;
import com.ViviEstu.service.PropietarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/propietarios")
@AllArgsConstructor
public class PropietarioController {

    private final PropietarioService propietarioService;
    private final PropietariosMapper propietarioMapper;

    @GetMapping
    public ResponseEntity<List<PropietariosResponseDTO>> getAllPropietarios() {
        return ResponseEntity.ok(propietarioService.findAllPropietarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropietariosResponseDTO> getPropietarioById(@PathVariable Long id) {
        return ResponseEntity.ok(propietarioService.findPropietarioById(id));
    }

    @PostMapping
    public ResponseEntity<PropietariosResponseDTO> crearPropietario(@Valid @RequestBody PropietariosRequestDTO dto) {
        Propietarios propietario = propietarioMapper.toEntity(dto);
        Propietarios saved = propietarioService.crearPropietario(propietario);
        return ResponseEntity.ok(propietarioMapper.toDTO(saved));
    }

    @PostMapping("/{id}/alojamientos")
    public ResponseEntity<Alojamiento> registrarAlojamiento(
            @PathVariable Long id,
            @RequestBody Alojamiento alojamiento,
            @RequestBody(required = false) List<ImagenesAlojamiento> imagenes) {
        return ResponseEntity.ok(propietarioService.registrarAlojamiento(id, alojamiento, imagenes));
    }
}
