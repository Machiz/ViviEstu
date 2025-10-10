package com.ViviEstu.service;

import com.ViviEstu.model.entity.*;
import com.ViviEstu.repository.*;
import com.ViviEstu.exception.BadRequestException;
import com.ViviEstu.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class PropietarioService {
    @Autowired
    private PropietarioRepository propietarioRepository;
    @Autowired
    private AlojamientoRepository alojamientoRepository;
    @Autowired
    private SolicitudesRepository solicitudesRepository;
    @Autowired
    private NotificacionPropieRepository notificacionPropieRepository;
    // ...other repositories as needed...

    // US-04: Publish a rental offer
    @Transactional
    public Alojamiento publicarAlojamiento(Long propietarioId, Alojamiento alojamiento, List<ImagenesAlojamiento> fotos, String dni, String documentoPropiedad) {
        // Validar documentación
        if (dni == null || dni.isEmpty() || documentoPropiedad == null || documentoPropiedad.isEmpty()) {
            throw new BadRequestException("Debe adjuntar DNI y documento de propiedad válidos.");
        }
        // Validar campos obligatorios
        if (alojamiento.getTitulo() == null || alojamiento.getTitulo().isEmpty() ||
            alojamiento.getPrecio() == null || alojamiento.getUbicacion() == null ||
            alojamiento.getTipoPropiedad() == null || alojamiento.getDescripcion() == null) {
            throw new BadRequestException("Faltan campos obligatorios en la publicación.");
        }
        // Validar descripción
        if (alojamiento.getDescripcion().length() < 50) {
            throw new BadRequestException("La descripción debe tener al menos 50 caracteres.");
        }
        // Validar fotos
        if (fotos == null || fotos.size() < 1) {
            throw new BadRequestException("Debe adjuntar al menos una foto.");
        }
        if (fotos.size() > 10) {
            throw new BadRequestException("No puede adjuntar más de 10 fotos.");
        }
        // Validar precio
        if (alojamiento.getPrecio() < 200 || alojamiento.getPrecio() > 5000) {
            throw new BadRequestException("El precio debe estar entre S/200 y S/5000.");
        }
        // Validar máximo de 20 publicaciones activas
        long activas = alojamientoRepository.countByPropietarioIdAndEstado(propietarioId, "ACTIVO");
        if (activas >= 20) {
            throw new BadRequestException("No puede tener más de 20 publicaciones activas.");
        }
        // Asignar propietario y guardar alojamiento
        Propietarios propietario = propietarioRepository.findById(propietarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado"));
        alojamiento.setPropietario(propietario);
        alojamiento.setEstado("ACTIVO");
        alojamiento.setFechaPublicacion(LocalDate.now());
        Alojamiento saved = alojamientoRepository.save(alojamiento);
        // Guardar fotos
        for (ImagenesAlojamiento foto : fotos) {
            foto.setAlojamiento(saved);
            // ...guardar foto en su repositorio...
        }
        // ...guardar documentación si aplica...
        return saved;
    }

    // US-10: Get received applications for landlord's properties
    public List<Solicitudes> obtenerSolicitudesRecibidas(Long propietarioId) {
        return solicitudesRepository.findByAlojamiento_Propietario_Id(propietarioId);
    }

    // US-11: Mark listing as rented and archive, reject pending applications
    @Transactional
    public void marcarComoAlquilado(Long alojamientoId) {
        Alojamiento alojamiento = alojamientoRepository.findById(alojamientoId)
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado"));
        alojamiento.setEstado("ALQUILADO");
        alojamientoRepository.save(alojamiento);
        // Rechazar solicitudes pendientes
        List<Solicitudes> pendientes = solicitudesRepository.findByAlojamiento_IdAndEstado(alojamientoId, "PENDIENTE");
        for (Solicitudes s : pendientes) {
            s.setEstado("RECHAZADO");
            solicitudesRepository.save(s);
        }
    }

    // US-18: Send notification to landlord
    public void notificarPropietario(Long propietarioId, String mensaje) {
        NotificacionPropie notificacion = new NotificacionPropie();
        notificacion.setPropietarioId(propietarioId);
        notificacion.setMensaje(mensaje);
        notificacion.setFecha(LocalDate.now());
        notificacion.setLeido(false);
        notificacionPropieRepository.save(notificacion);
    }

    // US-20: Generate report/statistics for listings active at least 7 days
    public ReporteAlojamientoDTO generarReporteAlojamiento(Long alojamientoId) {
        Alojamiento alojamiento = alojamientoRepository.findById(alojamientoId)
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado"));
        if (alojamiento.getFechaPublicacion() == null || ChronoUnit.DAYS.between(alojamiento.getFechaPublicacion(), LocalDate.now()) < 7) {
            throw new BadRequestException("El alojamiento debe estar activo al menos 7 días para generar estadísticas.");
        }
        // ...calcular estadísticas: visitas, solicitudes, comentarios, etc...
        ReporteAlojamientoDTO reporte = new ReporteAlojamientoDTO();
        // reporte.setVisitas(...);
        // reporte.setSolicitudes(...);
        // reporte.setComentarios(...);
        // ...otros campos...
        return reporte;
    }
}
