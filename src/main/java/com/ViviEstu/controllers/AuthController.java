package com.ViviEstu.controllers;

import com.ViviEstu.model.dto.request.LoginRequestDTO;
import com.ViviEstu.model.dto.request.RegisterEstudianteRequestDTO;
import com.ViviEstu.model.dto.request.RegisterPropietarioRequestDTO;
import com.ViviEstu.model.dto.response.AuthResponseDTO;
import com.ViviEstu.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/estudiante")
    public ResponseEntity<String> registerEstudiante(@Validated @RequestBody RegisterEstudianteRequestDTO dto) {
        authService.registerEstudiante(dto);
        return ResponseEntity.ok("Estudiante registrado con éxito");
    }

    @PostMapping("/register/propietario")
    public ResponseEntity<String> registerPropietario(@Validated @RequestBody RegisterPropietarioRequestDTO dto) {
        authService.registerPropietario(dto);
        return ResponseEntity.ok("Propietario registrado con éxito");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}