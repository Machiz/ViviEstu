package com.ViviEstu.service;

import com.ViviEstu.exception.ResourceNotFoundException;
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
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class SolicitudService {

    private final SolicitudRepository solicitudesRepository;
    private final EstudiantesRepository estudiantesRepository;
    private final AlojamientoRepository alojamientoRepository;
    private final SolicitudMapper mapper;

    public List<SolicitudResponseDTO> listar() {
        return solicitudesRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public SolicitudResponseDTO obtenerPorId(Long id) {
        Solicitudes solicitud = solicitudesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con ID: " + id));
        return mapper.toDTO(solicitud);
    }

    public SolicitudResponseDTO registrar(SolicitudRequestDTO request) {
        Estudiantes estudiante = estudiantesRepository.findById(request.getEstudiantesId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + request.getEstudiantesId()));

        Alojamiento alojamiento = alojamientoRepository.findById(request.getAlojamientoId())
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con ID: " + request.getAlojamientoId()));

        Solicitudes solicitud = mapper.toEntity(request, estudiante, alojamiento);

        solicitud.setEstado("PENDIENTE");

        Solicitudes guardada = solicitudesRepository.save(solicitud);
        return mapper.toDTO(guardada);
    }

    public SolicitudResponseDTO actualizarEstado(Long id, String estado) {
        Solicitudes solicitud = solicitudesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada con id: " + id));


        solicitud.setEstado(estado);
        solicitudesRepository.save(solicitud);

        return mapper.toDTO(solicitud);
    }

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

    public List<SolicitudResponseDTO> obtenerPorEstudianteId(Long estudianteId) {
        List<Solicitudes> solicitudes = solicitudesRepository.findByEstudiantes_Id(estudianteId);

        if (solicitudes.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron solicitudes para el estudiante con ID: " + estudianteId);
        }

        return solicitudes.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SolicitudResponseDTO> obtenerPorPropietarioId(Long propietarioId) {
        List<Solicitudes> solicitudes = solicitudesRepository.findByPropietarioId(propietarioId);

        if (solicitudes.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron solicitudes para el propietario con ID: " + propietarioId);
        }

        return solicitudes.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SolicitudResponseDTO> obtenerPorEstudianteIdYAlojamientoId(Long estudianteId, Long alojamientoId) {
        List<Solicitudes> solicitudes = solicitudesRepository.findByEstudiantes_IdAndAlojamiento_Id(estudianteId, alojamientoId);

        if (solicitudes.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron solicitudes para el estudiante con ID: " + estudianteId + " y el alojamiento con ID: " + alojamientoId);
        }

        return solicitudes.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public void eliminar(Long id) {
        if (!solicitudesRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Solicitud no encontrada con ID: " + id);
        }
        solicitudesRepository.deleteById(id);
    }

}
