package com.ViviEstu.service;

import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.EstudianteMapper;
import com.ViviEstu.model.dto.request.EstudiantesRequestDTO;
import com.ViviEstu.model.dto.response.EstudianteResponseDTO;
import com.ViviEstu.model.entity.Distrito;
import com.ViviEstu.model.entity.Estudiantes;
import com.ViviEstu.model.entity.Universidad;
import com.ViviEstu.repository.DistritoRepository;
import com.ViviEstu.repository.EstudiantesRepository;
import com.ViviEstu.repository.UniversidadRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class EstudiantesService{

    private final EstudiantesRepository estudiantesRepository;
    private final DistritoRepository distritoRepository;
    private final UniversidadRepository universidadRepository;
    private final EstudianteMapper estudiantesMapper;

    @Transactional(readOnly = true)
    public List<EstudianteResponseDTO> getAllEstudiantes() {
        List<Estudiantes> estudiantes = estudiantesRepository.findAll();
        return estudiantesMapper.convertToListDTO(estudiantes);
    }

    @Transactional(readOnly = true)
    public EstudianteResponseDTO getEstudianteById(Long id) {
        Estudiantes estudiante = estudiantesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));
        return estudiantesMapper.convertToDTO(estudiante);
    }

    @Transactional
    public EstudianteResponseDTO createEstudiante(EstudiantesRequestDTO dto) {

        // Buscar relaciones
        Distrito distrito = distritoRepository.findById(dto.getDistritoId())
                .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado"));

        Universidad universidad = universidadRepository.findById(dto.getUniversidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada"));

        // Crear entidad manualmente
        Estudiantes estudiante = new Estudiantes();
        estudiante.setNombre(dto.getNombre());
        estudiante.setCorreo(dto.getCorreo());
        estudiante.setContrasenia(dto.getContrasenia());
        estudiante.setTelefono(dto.getTelefono());
        estudiante.setDni(dto.getDni());
        estudiante.setCarrera(dto.getCarrera());
        estudiante.setDistrito(distrito);
        estudiante.setUniversidad(universidad);
        estudiante.setDistritoPreferencia(dto.getDistritoPreferencia() != null ? dto.getDistritoPreferencia() : "No definido");
        estudiante.setUrlPerfil(dto.getUrlPerfil());

        Estudiantes saved = estudiantesRepository.save(estudiante);

        // Crear DTO de respuesta
        EstudianteResponseDTO response = new EstudianteResponseDTO();
        response.setId(saved.getId());
        response.setNombre(saved.getNombre());
        response.setCorreo(saved.getCorreo());
        response.setTelefono(saved.getTelefono());
        response.setCarrera(saved.getCarrera());
        response.setVerificado(saved.isVerificado());
        response.setDistrito(saved.getDistrito().getNombre());
        response.setUniversidad(saved.getUniversidad().getNombre());
        response.setUrlPerfil(saved.getUrlPerfil());

        return response;
    }


    @Transactional
    public EstudianteResponseDTO updateEstudiante(Long id, EstudiantesRequestDTO estudianteRequestDTO) {
        Estudiantes estudiante = estudiantesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));

        estudiante.setNombre(estudianteRequestDTO.getNombre());
        estudiante.setCorreo(estudianteRequestDTO.getCorreo());

        estudiantesRepository.save(estudiante);
        return estudiantesMapper.convertToDTO(estudiante);
    }

    @Transactional
    public void deleteEstudiante(Long id) {
        estudiantesRepository.deleteById(id);
    }
}
