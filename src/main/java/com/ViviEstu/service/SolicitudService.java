package com.ViviEstu.service;

import com.ViviEstu.mapper.SolicitudMapper;
import com.ViviEstu.model.dto.request.SolicitudRequestDTO;
import com.ViviEstu.model.dto.response.SolicitudResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.Estudiantes;
import com.ViviEstu.model.entity.Solicitudes;
import com.ViviEstu.repository.AlojamientoRepository;
import com.ViviEstu.repository.EstudiantesRepository;
import com.ViviEstu.repository.SolicitudRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SolicitudService {
    private final SolicitudRepository solicitudesRepository;
    private final EstudiantesRepository estudiantesRepository;
    private final AlojamientoRepository alojamientoRepository;
    private final SolicitudMapper mapper;

    public SolicitudService(SolicitudRepository solicitudesRepository,
                              EstudiantesRepository estudiantesRepository,
                              AlojamientoRepository alojamientoRepository,
                              SolicitudMapper mapper) {
        this.solicitudesRepository = solicitudesRepository;
        this.estudiantesRepository = estudiantesRepository;
        this.alojamientoRepository = alojamientoRepository;
        this.mapper = mapper;
    }

    // 📄 Listar todas las solicitudes
    public List<SolicitudResponseDTO> listar() {
        return solicitudesRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // 🔍 Obtener solicitud por ID
    public SolicitudResponseDTO obtenerPorId(Long id) {
        Solicitudes solicitud = solicitudesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));
        return mapper.toDTO(solicitud);
    }

    // 🆕 Registrar nueva solicitud
    public SolicitudResponseDTO registrar(SolicitudRequestDTO request) {
        Estudiantes estudiante = estudiantesRepository.findById(request.getEstudiantesId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + request.getEstudiantesId()));

        Alojamiento alojamiento = alojamientoRepository.findById(request.getAlojamientoId())
                .orElseThrow(() -> new RuntimeException("Alojamiento no encontrado con ID: " + request.getAlojamientoId()));

        Solicitudes solicitud = mapper.toEntity(request, estudiante, alojamiento);

        // 👇 Valor por defecto
        solicitud.setEstado("PENDIENTE");

        Solicitudes guardada = solicitudesRepository.save(solicitud);
        return mapper.toDTO(guardada);
    }


    // ✏️ Actualizar solicitud existente
    public SolicitudResponseDTO actualizar(Long id, SolicitudRequestDTO request) {
        Solicitudes solicitud = solicitudesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada con ID: " + id));

        Estudiantes estudiante = estudiantesRepository.findById(request.getEstudiantesId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + request.getEstudiantesId()));

        Alojamiento alojamiento = alojamientoRepository.findById(request.getAlojamientoId())
                .orElseThrow(() -> new RuntimeException("Alojamiento no encontrado con ID: " + request.getAlojamientoId()));

        solicitud.setMesesAlquiler(request.getMesesAlquiler());
        solicitud.setCantInquilinos(request.getCantInquilinos());
        solicitud.setMensaje(request.getMensaje());
        solicitud.setOferta(request.getOferta());
        solicitud.setEstudiantes(estudiante);
        solicitud.setAlojamiento(alojamiento);

        return mapper.toDTO(solicitudesRepository.save(solicitud));
    }

    // 🗑️ Eliminar solicitud
    public void eliminar(Long id) {
        if (!solicitudesRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Solicitud no encontrada con ID: " + id);
        }
        solicitudesRepository.deleteById(id);
    }

}
