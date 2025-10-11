package com.ViviEstu.service;

import com.ViviEstu.exception.BadRequestException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.PropietariosMapper;
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
    private final AlojamientoRepository alojamientoRepository;
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
    public Propietarios crearPropietario(Propietarios propietario) {
        return propietarioRepository.save(propietario);
    }

    @Transactional
    public Alojamiento registrarAlojamiento(Long propietarioId, Alojamiento alojamiento, List<ImagenesAlojamiento> imagenes) {
        Propietarios propietario = propietarioRepository.findById(propietarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado"));

        // Validaciones simples
        if (alojamiento.getTitulo() == null || alojamiento.getTitulo().isEmpty())
            throw new BadRequestException("El título es obligatorio");
        if (alojamiento.getPrecioMensual() == null || alojamiento.getPrecioMensual().compareTo(BigDecimal.ZERO) <= 0)
            throw new BadRequestException("El precio debe ser mayor que 0");

        alojamiento.setPropietario(propietario);
        alojamiento.setFecha(Timestamp.from(Instant.now()));
        alojamiento.setAlquilado(false);

        Alojamiento saved = alojamientoRepository.save(alojamiento);

        // Asignar alojamiento a las imágenes
        if (imagenes != null) {
            for (ImagenesAlojamiento img : imagenes) {
                img.setAlojamiento(saved);
            }
        }

        return saved;
    }
}
