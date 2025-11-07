package com.ViviEstu.service;


import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.InteraccionMapper;
import com.ViviEstu.model.dto.request.InteraccionRequestDTO;
import com.ViviEstu.model.dto.response.InteraccionReporteResponseDTO;
import com.ViviEstu.model.dto.response.InteraccionResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.Estudiantes;
import com.ViviEstu.model.entity.Interacciones;
import com.ViviEstu.repository.AlojamientoRepository;
import com.ViviEstu.repository.EstudiantesRepository;
import com.ViviEstu.repository.InteraccionesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InteraccionService {

    private final InteraccionesRepository interaccionRepository;
    private final InteraccionMapper interaccionMapper;
    private final AlojamientoRepository alojamientoRepository;
    private final EstudiantesRepository estudiantesRepository;

    @Transactional(readOnly = true)
    public List<InteraccionResponseDTO> getAllInteracciones() {
        List<Interacciones> interacciones = interaccionRepository.findAll();
        return interaccionMapper.convertToListDTO(interacciones);
    }

    @Transactional(readOnly = true)
    public InteraccionResponseDTO getInteraccionById(Integer id) {
        Interacciones interaccion = interaccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interacción no encontrada con id: " + id));
        return interaccionMapper.convertToDTO(interaccion);
    }

    @Transactional
    public InteraccionResponseDTO createInteraccion(InteraccionRequestDTO dto) {

        Estudiantes estudiante = estudiantesRepository.findById(dto.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        Alojamiento alojamiento = alojamientoRepository.findById(dto.getAlojamientoId())
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado"));

        Interacciones interaccion = new Interacciones();
        interaccion.setEstudiante(estudiante);
        interaccion.setAlojamiento(alojamiento);
        interaccion.setFecha(LocalDateTime.now());

        Interacciones saved = interaccionRepository.save(interaccion);
        return interaccionMapper.convertToDTO(saved);
    }

    @Transactional(readOnly = true)
    public long contarPorAlojamiento(Long alojamientoId) {
        return interaccionRepository.contarPorAlojamiento(alojamientoId);
    }

    @Transactional
    public InteraccionResponseDTO updateInteraccion(Integer id, InteraccionRequestDTO dto) {

        Interacciones interaccion = interaccionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interacción no encontrada"));

        Estudiantes estudiante = estudiantesRepository.findById(dto.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        Alojamiento alojamiento = alojamientoRepository.findById(dto.getAlojamientoId())
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado"));

        interaccion.setEstudiante(estudiante);
        interaccion.setAlojamiento(alojamiento);
        interaccion.setFecha(LocalDateTime.now());

        Interacciones updated = interaccionRepository.save(interaccion);
        return interaccionMapper.convertToDTO(updated);
    }

    @Transactional
    public void deleteInteraccion(Integer id) {
        interaccionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public InteraccionReporteResponseDTO generarReportePorAlojamiento(Long alojamientoId) {

        List<Interacciones> interacciones = interaccionRepository.findByAlojamiento_Id(alojamientoId);

        if (interacciones.isEmpty()) {
            throw new ResourceNotFoundException("No hay interacciones para el alojamiento con ID " + alojamientoId);
        }

        String nombreAlojamiento = interacciones.get(0).getAlojamiento().getTitulo();

        long totalInteracciones = interacciones.size();

        List<Estudiantes> estudiantes = interacciones.stream()
                .map(Interacciones::getEstudiante)
                .distinct()
                .toList();

        long estudiantesUnicos = estudiantes.size();

        LocalDateTime ultimaInteraccion = interacciones.stream()
                .map(Interacciones::getFecha)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        // Universidad más frecuente
        String universidadPrincipal = estudiantes.stream()
                .filter(e -> e.getUniversidad() != null)
                .collect(Collectors.groupingBy(e -> e.getUniversidad().getNombre(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Sin universidad");

        // Distrito más frecuente
        String distritoPrincipal = estudiantes.stream()
                .filter(e -> e.getDistrito() != null)
                .collect(Collectors.groupingBy(e -> e.getDistrito().getNombre(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Sin distrito");

        double promedioInteraccionesPorEstudiante =
                estudiantesUnicos > 0 ? (double) totalInteracciones / estudiantesUnicos : 0.0;

        return new InteraccionReporteResponseDTO(
                alojamientoId,
                nombreAlojamiento,
                totalInteracciones,
                estudiantesUnicos,
                ultimaInteraccion,
                universidadPrincipal,
                distritoPrincipal,
                promedioInteraccionesPorEstudiante
        );
    }

}
