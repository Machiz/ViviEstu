package com.ViviEstu.controllers;

import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.ImagenesAlojamiento;
import com.ViviEstu.model.entity.Solicitudes;
import com.ViviEstu.model.entity.ReporteAlojamientoDTO;
import com.ViviEstu.service.PropietarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/propietarios")
public class PropietarioController {
    @Autowired
    private PropietarioService propietarioService;

    // US-04: Publicar alojamiento
    @PostMapping("/{propietarioId}/alojamientos")
    public ResponseEntity<Alojamiento> publicarAlojamiento(
            @PathVariable Long propietarioId,
            @RequestPart Alojamiento alojamiento,
            @RequestPart List<MultipartFile> fotos,
            @RequestParam String dni,
            @RequestParam String documentoPropiedad) {
        // Convertir MultipartFile a ImagenesAlojamiento según tu lógica
        List<ImagenesAlojamiento> imagenes = /* conversión aquí */ null;
        Alojamiento publicado = propietarioService.publicarAlojamiento(propietarioId, alojamiento, imagenes, dni, documentoPropiedad);
        return ResponseEntity.ok(publicado);
    }

    // US-10: Obtener solicitudes recibidas
    @GetMapping("/{propietarioId}/solicitudes")
    public ResponseEntity<List<Solicitudes>> obtenerSolicitudesRecibidas(@PathVariable Long propietarioId) {
        List<Solicitudes> solicitudes = propietarioService.obtenerSolicitudesRecibidas(propietarioId);
        return ResponseEntity.ok(solicitudes);
    }

    // US-11: Marcar alojamiento como alquilado
    @PutMapping("/alojamientos/{alojamientoId}/alquilado")
    public ResponseEntity<Void> marcarComoAlquilado(@PathVariable Long alojamientoId) {
        propietarioService.marcarComoAlquilado(alojamientoId);
        return ResponseEntity.ok().build();
    }

    // US-18: Notificar propietario (opcional, para pruebas)
    @PostMapping("/{propietarioId}/notificaciones")
    public ResponseEntity<Void> notificarPropietario(@PathVariable Long propietarioId, @RequestParam String mensaje) {
        propietarioService.notificarPropietario(propietarioId, mensaje);
        return ResponseEntity.ok().build();
    }

    // US-20: Generar reporte de alojamiento
    @GetMapping("/alojamientos/{alojamientoId}/reporte")
    public ResponseEntity<ReporteAlojamientoDTO> generarReporteAlojamiento(@PathVariable Long alojamientoId) {
        ReporteAlojamientoDTO reporte = propietarioService.generarReporteAlojamiento(alojamientoId);
        return ResponseEntity.ok(reporte);
    }
}
