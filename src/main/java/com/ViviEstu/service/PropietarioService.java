package com.ViviEstu.service;

import com.ViviEstu.exception.DuplicateResourceException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.PropietariosMapper;
import com.ViviEstu.model.dto.request.PropietariosRequestDTO;
import com.ViviEstu.model.dto.response.PropietariosResponseDTO;
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


    // 🔹 US-16: PERFIL DE PROPIETARIO PERSONALIZABLE

    @Transactional
    public PropietariosResponseDTO updatePropietario(Long id, PropietariosRequestDTO propietariosRequestDTO) {
        // Validar campos obligatorios
        if (propietariosRequestDTO.getNombre() == null ||
                propietariosRequestDTO.getApellidos() == null ||
                propietariosRequestDTO.getDni() == null ||
                propietariosRequestDTO.getTelefono() == null ||
                propietariosRequestDTO.getCorreo() == null ||
                propietariosRequestDTO.getContrasenia() == null) {
            throw new IllegalArgumentException("Debe completar todos los campos obligatorios antes de guardar.");
        }

        // Buscar propietario
        Propietarios propietario = propietarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado con id: " + id));

        // Actualizar datos personales
        propietario.setNombre(propietariosRequestDTO.getNombre());
        propietario.setApellidos(propietariosRequestDTO.getApellidos());
        propietario.setDni(propietariosRequestDTO.getDni());
        propietario.setTelefono(propietariosRequestDTO.getTelefono());

        // Actualizar datos de usuario
        User user = propietario.getUser();
        user.setCorreo(propietariosRequestDTO.getCorreo());
        user.setContrasenia(propietariosRequestDTO.getContrasenia());

        userRepository.save(user);
        propietarioRepository.save(propietario);

        PropietariosResponseDTO responseDTO = propietariosMapper.toDTO(propietario);
        responseDTO.setCorreo(user.getCorreo());
        return responseDTO;
    }



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
        if (propietarioRepository.existsByNombreAndApellidos(dto.getNombre(), dto.getApellidos())) {
            throw new DuplicateResourceException("Propietario existente");
        }

        Propietarios propietario = propietariosMapper.toEntity(dto);
        propietarioRepository.save(propietario);
        return propietariosMapper.toDTO(propietario);
    }

    @Transactional
    public void deletePropietario(Long id) {
        propietarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PropietariosResponseDTO findPropietarioByCorreo(String correo) {
        Propietarios propietario = propietarioRepository.findByUser_Correo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado con correo: " + correo));

        PropietariosResponseDTO responseDTO = propietariosMapper.toDTO(propietario);
        responseDTO.setCorreo(propietario.getUser().getCorreo());
        return responseDTO;
    }

    @Transactional
    public PropietariosResponseDTO updatePropietarioMe(String correo, PropietariosRequestDTO dto) {

        // 1. Buscar el propietario por el correo del Usuario asociado
        Propietarios propietario = propietarioRepository.findByUser_Correo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado con correo: " + correo));

        // 2. Validaciones mínimas (puedes agregar más si necesitas)
        if (dto.getNombre() == null || dto.getApellidos() == null) {
            throw new IllegalArgumentException("Nombre y apellidos son obligatorios");
        }

        // 3. Actualizar SOLO datos del Propietario (NO tocar User/Correo/Contraseña)
        propietario.setNombre(dto.getNombre());
        propietario.setApellidos(dto.getApellidos());
        propietario.setTelefono(dto.getTelefono());
        propietario.setDni(dto.getDni());

        // 4. Guardar cambios
        propietarioRepository.save(propietario);

        // 5. Preparar respuesta
        PropietariosResponseDTO responseDTO = propietariosMapper.toDTO(propietario);

        // Asignamos el correo original para que el front lo vea, aunque no haya cambiado
        if (propietario.getUser() != null) {
            responseDTO.setCorreo(propietario.getUser().getCorreo());
        }

        return responseDTO;
    }

    @Transactional
    public PropietariosResponseDTO updatePropietarioByCorreo(String correo, PropietariosRequestDTO propietariosRequestDTO) {

        // Validación de campos obligatorios
        if (propietariosRequestDTO.getNombre() == null ||
                propietariosRequestDTO.getApellidos() == null) {
            throw new IllegalArgumentException("Debe completar los campos obligatorios.");
        }

        // Buscar propietario por correo
        Propietarios propietario = propietarioRepository.findByUser_Correo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado con correo: " + correo));

        // Actualizar datos personales
        propietario.setNombre(propietariosRequestDTO.getNombre());
        propietario.setApellidos(propietariosRequestDTO.getApellidos());
        propietario.setDni(propietariosRequestDTO.getDni());
        propietario.setTelefono(propietariosRequestDTO.getTelefono());

        // Actualizar datos de usuario
        User user = propietario.getUser();
        if (user != null) {
            user.setCorreo(propietariosRequestDTO.getCorreo());
            user.setContrasenia(propietariosRequestDTO.getContrasenia()); // Deberías encriptar esto en producción
            userRepository.save(user);
        }

        propietarioRepository.save(propietario);

        PropietariosResponseDTO responseDTO = propietariosMapper.toDTO(propietario);
        responseDTO.setCorreo(user.getCorreo());
        return responseDTO;
    }
}
