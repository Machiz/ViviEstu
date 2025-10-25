package com.ViviEstu.service;

import com.ViviEstu.exception.BadRequestException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.PropietariosMapper;
import com.ViviEstu.model.dto.request.PropietariosRequestDTO;
import com.ViviEstu.model.dto.response.PropietariosResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.ImagenesAlojamiento;
import com.ViviEstu.model.entity.Propietarios;
import com.ViviEstu.repository.AlojamientoRepository;
import com.ViviEstu.repository.PropietarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PropietarioService {

    private final PropietarioRepository propietarioRepository;
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
        Propietarios propietario = propietariosMapper.toEntity(dto);
        propietarioRepository.save(propietario);
        return propietariosMapper.toDTO(propietario);
    }



}
