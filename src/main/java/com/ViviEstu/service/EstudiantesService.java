package com.ViviEstu.service;

import com.ViviEstu.exception.DuplicateResourceException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.EstudianteMapper;
import com.ViviEstu.model.dto.request.EstudiantesRequestDTO;
import com.ViviEstu.model.dto.response.EstudianteResponseDTO;
import com.ViviEstu.model.entity.*;
import com.ViviEstu.repository.*;
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
    private final DatosUniversitariosRepository datosUniversitariosRepository;
    private final EstudianteMapper estudiantesMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<EstudianteResponseDTO> getAllEstudiantes() {
        List<Estudiantes> estudiantes = estudiantesRepository.findAll();

        return estudiantes.stream()
                .map(estudiante -> {
                    EstudianteResponseDTO dto = estudiantesMapper.convertToDTO(estudiante);
                    dto.setDistrito(estudiante.getDistrito().getNombre());
                    dto.setUniversidad(estudiante.getUniversidad().getNombre());
                    dto.setCorreo(estudiante.getUser().getCorreo());
                    return dto;
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public EstudianteResponseDTO getEstudianteById(Long id) {
        Estudiantes estudiante = estudiantesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));

        EstudianteResponseDTO responseDTO = estudiantesMapper.convertToDTO(estudiante);
        responseDTO.setDistrito(estudiante.getDistrito().getNombre());
        responseDTO.setUniversidad(estudiante.getUniversidad().getNombre());
        responseDTO.setCorreo(estudiante.getUser().getCorreo());
        return responseDTO;
    }

    @Transactional
    public EstudianteResponseDTO createEstudiante(EstudiantesRequestDTO dto) {

        if (!datosUniversitariosRepository.existsByCorreoInstitucional(dto.getCorreo())) {
            throw new ResourceNotFoundException("Correo no encontrado en base de datos");
        }


        if (estudiantesRepository.existsByNombreAndApellidos(dto.getNombre(), dto.getApellidos())){
            throw new DuplicateResourceException("Estudiante existente");
        }

        Distrito distrito = distritoRepository.findById(dto.getDistritoId())
                .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado"));

        Universidad universidad = universidadRepository.findById(dto.getUniversidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada"));

        User user = new User();
        user.setCorreo(dto.getCorreo());
        user.setContrasenia(dto.getContrasenia());

        Role role = roleRepository.findByName(RoleType.ROLE_ESTUDIANTE)
                .orElseThrow(() -> new ResourceNotFoundException("Rol ROLE_USER no encontrado"));
        user.setRole(role);
        user.setActive(true);

        userRepository.save(user);

        Estudiantes estudiante = new Estudiantes();
        estudiante.setNombre(dto.getNombre());
        estudiante.setApellidos(dto.getApellidos());
        estudiante.setTelefono(dto.getTelefono());
        estudiante.setCiclo(dto.getCiclo());
        estudiante.setDni(dto.getDni());
        estudiante.setCarrera(dto.getCarrera());
        estudiante.setUniversidad(universidad);
        estudiante.setDistrito(distrito);

        estudiantesRepository.save(estudiante);
        EstudianteResponseDTO responseDTO = estudiantesMapper.convertToDTO(estudiante);
        responseDTO.setDistrito(estudiante.getDistrito().getNombre());
        responseDTO.setUniversidad(estudiante.getUniversidad().getNombre());
        responseDTO.setCorreo(user.getCorreo());


        return responseDTO;

    }

    @Transactional
    public EstudianteResponseDTO updateEstudiante(Long id, EstudiantesRequestDTO estudianteRequestDTO) {
        Estudiantes estudiante = estudiantesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));

        estudiante.setNombre(estudianteRequestDTO.getNombre());
        estudiante.setApellidos(estudianteRequestDTO.getNombre());
        estudiante.setDni(estudianteRequestDTO.getDni());
        estudiante.setTelefono(estudianteRequestDTO.getTelefono());
        estudiante.setCarrera(estudianteRequestDTO.getCarrera());

        User user = estudiante.getUser();
        user.setCorreo(estudianteRequestDTO.getCorreo());
        user.setContrasenia(estudianteRequestDTO.getContrasenia());

        userRepository.save(user);
        estudiantesRepository.save(estudiante);

        EstudianteResponseDTO responseDTO = estudiantesMapper.convertToDTO(estudiante);
        responseDTO.setDistrito(estudiante.getDistrito().getNombre());
        responseDTO.setUniversidad(estudiante.getUniversidad().getNombre());
        responseDTO.setCorreo(user.getCorreo());
        return responseDTO;

    }

    @Transactional
    public void deleteEstudiante(Long id) {
        Estudiantes estudiante = estudiantesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));

        userRepository.delete(estudiante.getUser());
        estudiantesRepository.deleteById(id);
    }
}
