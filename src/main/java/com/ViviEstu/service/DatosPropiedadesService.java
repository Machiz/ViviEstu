package com.ViviEstu.service;

import com.ViviEstu.exception.DuplicateResourceException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.DatosPropiedadesMapper;
import com.ViviEstu.model.dto.request.DatosPropiedadesRequestDTO;
import com.ViviEstu.model.dto.response.DatosPropiedadesResponseDTO;
import com.ViviEstu.model.entity.DatosPropiedades;
import com.ViviEstu.repository.DatosPropiedadesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class DatosPropiedadesService {

    private final DatosPropiedadesRepository datosPropiedadesRepository;
    private final DatosPropiedadesMapper mapper;

    @Transactional(readOnly = true)
    public List<DatosPropiedadesResponseDTO> getAllDatosPropiedades() {
        return mapper.convertToListDTO(datosPropiedadesRepository.findAll());
    }

    @Transactional(readOnly = true)
    public DatosPropiedadesResponseDTO getDatosPropiedadesById(Integer id) {
        DatosPropiedades datos = datosPropiedadesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de Propiedad no encontrado con id: " + id));
        return mapper.convertToDTO(datos);
    }

    @Transactional
    public DatosPropiedadesResponseDTO crearDatosPropiedades(DatosPropiedadesRequestDTO dto) {
        if (datosPropiedadesRepository.existsByDniPropietarioAndNroPartida(dto.getDniPropietario(),dto.getNroPartida())) {
            throw new DuplicateResourceException("El registro de Nro Partida y DNI ya existe.");
        }

        DatosPropiedades datos = mapper.convertToEntity(dto);
        datos = datosPropiedadesRepository.save(datos);
        return mapper.convertToDTO(datos);
    }

    @Transactional
    public DatosPropiedadesResponseDTO updateDatosPropiedades(Integer id, DatosPropiedadesRequestDTO dto) {
        DatosPropiedades datos = datosPropiedadesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro de Propiedad no encontrado con id: " + id));

        // Validación de duplicidad si los datos clave se modifican
        if (!datos.getNroPartida().equals(dto.getNroPartida()) || !datos.getDniPropietario().equals(dto.getDniPropietario())) {
            if (datosPropiedadesRepository.existsByDniPropietarioAndNroPartida(dto.getDniPropietario(),dto.getNroPartida())) {
                throw new DuplicateResourceException("El nuevo registro de Nro Partida y DNI ya existe.");
            }
        }

        datos.setNroPartida(dto.getNroPartida());
        datos.setDniPropietario(dto.getDniPropietario());

        return mapper.convertToDTO(datosPropiedadesRepository.save(datos));
    }

    @Transactional
    public void deleteDatosPropiedades(Integer id) {
        if (!datosPropiedadesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Registro de Propiedad no encontrado con id: " + id);
        }
        datosPropiedadesRepository.deleteById(id);
    }
}