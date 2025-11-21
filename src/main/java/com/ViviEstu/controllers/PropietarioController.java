package com.ViviEstu.controllers;


import com.ViviEstu.mapper.PropietariosMapper;
import com.ViviEstu.model.dto.request.EstudiantesRequestDTO;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/propietarios")
@AllArgsConstructor
public class PropietarioController {

    private final PropietarioService propietarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PropietariosResponseDTO>> getAllPropietarios() {
        List<PropietariosResponseDTO> propietarios = propietarioService.findAllPropietarios();
        return new ResponseEntity<>(propietarios, HttpStatus.OK);
    }

    @GetMapping("/me") // -> /propietarios/me
    @PreAuthorize("hasRole('PROPIETARIO')")
    public ResponseEntity<PropietariosResponseDTO> getMiPerfilPropietario(
            @AuthenticationPrincipal UserDetails userDetails) {

        String correo = userDetails.getUsername();

        PropietariosResponseDTO propietario = propietarioService.findPropietarioByCorreo(correo);
        return new ResponseEntity<>(propietario, HttpStatus.OK);
    }

    /**
     * Endpoint: PUT /propietarios/me (Actualizar mi perfil)
     */
    @PutMapping("/me") // -> /propietarios/me
    @PreAuthorize("hasRole('PROPIETARIO')")
    public ResponseEntity<PropietariosResponseDTO> updateMiPerfilPropietario(
            @AuthenticationPrincipal UserDetails userDetails,
            @Validated @RequestBody PropietariosRequestDTO requestDTO) {

        String correo = userDetails.getUsername();

        PropietariosResponseDTO updated = propietarioService.updatePropietarioByCorreo(correo, requestDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ESTUDIANTE') or hasRole('ADMIN') or hasRole('PROPIETARIO')")
    public ResponseEntity<PropietariosResponseDTO> getPropietarioById(@PathVariable Long id) {
        PropietariosResponseDTO propietario = propietarioService.findPropietarioById(id);
        return new ResponseEntity<>(propietario, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PropietariosResponseDTO> crearPropietario(@Valid @RequestBody PropietariosRequestDTO dto) {
        PropietariosResponseDTO creado = propietarioService.crearPropietario(dto);
        return new ResponseEntity<>(creado, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROPIETARIO') or hasRole('ADMIN')")
    public ResponseEntity<PropietariosResponseDTO> updatePropietario(
            @PathVariable long id,
            @Validated @RequestBody PropietariosRequestDTO requestDTO) {
        PropietariosResponseDTO updated = propietarioService.updatePropietario(id, requestDTO);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROPIETARIO')")
    public ResponseEntity<Void> deletePropietario(@PathVariable long id) {
        propietarioService.deletePropietario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
