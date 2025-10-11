package com.ViviEstu.service;

import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.AlojamientoMapper;
import com.ViviEstu.model.dto.request.AlojamientoRequestDTO;
import com.ViviEstu.model.dto.response.AlojamientoResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.Distrito;
import com.ViviEstu.model.entity.Propietarios;
import com.ViviEstu.repository.AlojamientoRepository;
import com.ViviEstu.repository.DistritoRepository;
import com.ViviEstu.repository.PropietariosRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor
public class AlojamientoService {

    private final AlojamientoRepository alojamientoRepository;
    private final DistritoRepository distritoRepository;
    private final PropietariosRepository propietariosRepository;
    private final AlojamientoMapper mapper;

    @Transactional(readOnly = true)
    public List<AlojamientoResponseDTO> getAllAlojamientos() {
        return mapper.convertToListDTO(alojamientoRepository.findAll());
    }

    @Transactional(readOnly = true)
    public AlojamientoResponseDTO getAlojamientoById(Long id) {
        Alojamiento alojamiento = alojamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con id: " + id));
        return mapper.convertToDTO(alojamiento);
    }

    @Transactional
    public AlojamientoResponseDTO createAlojamiento(AlojamientoRequestDTO dto) {

        Propietarios propietario = propietariosRepository.findById(dto.getPropietarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado"));

        Distrito zona = distritoRepository.findById(dto.getDistritoId())
                .orElseThrow(() -> new ResourceNotFoundException("Zona no encontrada"));

        Alojamiento alojamiento = mapper.convertToEntity(dto);
        alojamiento.setPropietario(propietario);
        alojamiento.setDistrito(zona);
        alojamiento.setFecha(new Timestamp(System.currentTimeMillis()));

        Alojamiento saved = alojamientoRepository.save(alojamiento);
        return mapper.convertToDTO(saved);
    }

    @Transactional
    public AlojamientoResponseDTO updateAlojamiento(Long id, AlojamientoRequestDTO dto) {
        Alojamiento alojamiento = alojamientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con id: " + id));

        alojamiento.setTitulo(dto.getTitulo());
        alojamiento.setDescripcion(dto.getDescripcion());
        alojamiento.setDireccion(dto.getDireccion());
        alojamiento.setPrecioMensual(dto.getPrecioMensual());
        alojamiento.setAlquilado(dto.getAlquilado());

        Alojamiento updated = alojamientoRepository.save(alojamiento);
        return mapper.convertToDTO(updated);
    }

    @Transactional
    public void deleteAlojamiento(Long id) {
        alojamientoRepository.deleteById(id);
    }
}
