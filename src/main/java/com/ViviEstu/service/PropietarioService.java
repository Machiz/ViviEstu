package com.ViviEstu.service;

import com.ViviEstu.exception.DuplicateResourceException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.PropietariosMapper;
import com.ViviEstu.model.dto.request.EstudiantesRequestDTO;
import com.ViviEstu.model.dto.request.PropietariosRequestDTO;
import com.ViviEstu.model.dto.response.EstudianteResponseDTO;
import com.ViviEstu.model.dto.response.PropietariosResponseDTO;
import com.ViviEstu.model.entity.Estudiantes;
import com.ViviEstu.model.entity.Propietarios;
import com.ViviEstu.model.entity.User;
import com.ViviEstu.repository.PropietariosRepository;
import com.ViviEstu.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PropietarioService {

    private final PropietariosRepository propietarioRepository;
    private final PropietariosMapper propietariosMapper;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<PropietariosResponseDTO> findAllPropietarios() {
        return propietarioRepository.findAll()
                .stream()
                .map(propietariosMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PropietariosResponseDTO findPropietarioById(Long id) {
        Propietarios propietario = propietarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado con id: " + id));
        return propietariosMapper.toDTO(propietario);
    }

    @Transactional
    public PropietariosResponseDTO crearPropietario(PropietariosRequestDTO dto) {

        if (propietarioRepository.existsByNombreAndApellidos(dto.getNombre(), dto.getApellidos())){
            throw new DuplicateResourceException("Propietario existente");
        }

        Propietarios propietario = propietariosMapper.toEntity(dto);
        propietarioRepository.save(propietario);
        return propietariosMapper.toDTO(propietario);
    }

    @Transactional
    public PropietariosResponseDTO updatePropietario(Long id, PropietariosRequestDTO propietariosRequestDTO) {
        Propietarios propietarios = propietarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));

        propietarios.setNombre(propietariosRequestDTO.getNombre());
        propietarios.setApellidos(propietariosRequestDTO.getNombre());
        propietarios.setDni(propietariosRequestDTO.getDni());
        propietarios.setTelefono(propietariosRequestDTO.getTelefono());

        User user = propietarios.getUser();
        user.setCorreo(propietariosRequestDTO.getCorreo());
        user.setContrasenia(propietariosRequestDTO.getContrasenia());

        userRepository.save(user);
        propietarioRepository.save(propietarios);

        PropietariosResponseDTO responseDTO = propietariosMapper.toDTO(propietarios);
        responseDTO.setCorreo(user.getCorreo());
        return responseDTO;
    }


    @Transactional
    public void deletePropietario(Long id) {
        propietarioRepository.deleteById(id);
    }

}
