package com.ViviEstu.controllers;


import com.ViviEstu.mapper.PropietariosMapper;
import com.ViviEstu.model.dto.request.PropietariosRequestDTO;
import com.ViviEstu.model.dto.response.EstudianteResponseDTO;
import com.ViviEstu.model.dto.response.PropietariosResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.ImagenesAlojamiento;
import com.ViviEstu.model.entity.Propietarios;
import com.ViviEstu.service.PropietarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/propietarios")
@AllArgsConstructor
public class PropietarioController {

    private final PropietarioService propietarioService;

    @GetMapping
    public ResponseEntity<List<PropietariosResponseDTO>> getAllPropietarios() {
        List<PropietariosResponseDTO> propietarios = propietarioService.findAllPropietarios();
        return new ResponseEntity<>(propietarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropietariosResponseDTO> getPropietarioById(@PathVariable Long id) {
        PropietariosResponseDTO propietario = propietarioService.findPropietarioById(id);
        return new ResponseEntity<>(propietario, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PropietariosResponseDTO> crearPropietario(@Valid @RequestBody PropietariosRequestDTO dto) {
        PropietariosResponseDTO creado = propietarioService.crearPropietario(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePropietario(@PathVariable long id) {
        propietarioService.deletePropietario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
