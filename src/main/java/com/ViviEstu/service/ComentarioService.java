package com.ViviEstu.service;

import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.ComentarioMapper;
import com.ViviEstu.model.dto.request.ComentarioRequestDTO;
import com.ViviEstu.model.dto.response.ComentarioResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.Comentario;
import com.ViviEstu.model.entity.Estudiantes;
import com.ViviEstu.repository.AlojamientoRepository;
import com.ViviEstu.repository.ComentarioRepository;
import com.ViviEstu.repository.EstudiantesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@AllArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final AlojamientoRepository alojamientoRepository;
    private final EstudiantesRepository estudiantesRepository;
    private final ComentarioMapper comentarioMapper;


    public ComentarioResponseDTO registrar(ComentarioRequestDTO request) {

        if (request.getContenido() == null || request.getContenido().isBlank()) {
            throw new IllegalArgumentException("El contenido del comentario no puede estar vacío");
        }

        if (request.getContenido().length() > 500) {
            throw new IllegalArgumentException("El comentario no puede exceder los 500 caracteres");
        }

        Estudiantes estudiante = estudiantesRepository.findById(request.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con ID: " + request.getEstudianteId()));

        Alojamiento alojamiento = alojamientoRepository.findById(request.getAlojamientoId())
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con ID: " + request.getAlojamientoId()));


        Comentario comentario = new Comentario();

        comentario.setEstudiante(estudiante);
        comentario.setAlojamiento(alojamiento);
        comentario.setContenido(request.getContenido());

        Comentario guardado = comentarioRepository.save(comentario);


        return comentarioMapper.toDTO(guardado);
    }

    public List<ComentarioResponseDTO> listarPorAlojamiento(Long alojamientoId) {
        var lista = comentarioRepository.findByAlojamientoId(alojamientoId);
        return comentarioMapper.toListDTO(lista);
    }

    public void eliminarComentario(Long id) {
        comentarioRepository.deleteById(id);
    }
}
