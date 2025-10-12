package com.ViviEstu.service;

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

        Estudiantes estudiante = estudiantesRepository.findById(request.getEstudianteId())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + request.getEstudianteId()));

        Alojamiento alojamiento = alojamientoRepository.findById(request.getAlojamientoId())
                .orElseThrow(() -> new RuntimeException("Alojamiento no encontrado con ID: " + request.getAlojamientoId()));


        Comentario comentario = comentarioMapper.toEntity(request);


        comentario.setEstudiante(estudiante);
        comentario.setAlojamiento(alojamiento);


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
