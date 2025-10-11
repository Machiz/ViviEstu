package com.ViviEstu.service;


import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.InteraccionMapper;
import com.ViviEstu.model.dto.request.InteraccionRequestDTO;
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
}