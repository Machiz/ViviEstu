package com.ViviEstu.service;

import com.ViviEstu.exception.DuplicateResourceException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.PropietariosMapper;
import com.ViviEstu.model.dto.request.PropietariosRequestDTO;
import com.ViviEstu.model.dto.response.PropietariosResponseDTO;
import com.ViviEstu.model.entity.Propietarios;
import com.ViviEstu.repository.PropietariosRepository;
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
    public void deletePropietario(Long id) {
        propietarioRepository.deleteById(id);
    }

}
