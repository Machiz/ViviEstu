package com.ViviEstu.mapper;

import com.ViviEstu.model.dto.request.DatosPropiedadesRequestDTO;
import com.ViviEstu.model.dto.response.DatosPropiedadesResponseDTO;
import com.ViviEstu.model.entity.DatosPropiedades;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DatosPropiedadesMapper {

    public DatosPropiedadesResponseDTO convertToDTO(DatosPropiedades entity) {
        if (entity == null) return null;
        DatosPropiedadesResponseDTO dto = new DatosPropiedadesResponseDTO();
        dto.setId(entity.getId());
        dto.setNroPartida(entity.getNroPartida());
        dto.setDniPropietario(entity.getDniPropietario());
        return dto;
    }

    public DatosPropiedades convertToEntity(DatosPropiedadesRequestDTO dto) {
        if (dto == null) return null;
        DatosPropiedades entity = new DatosPropiedades();
        entity.setNroPartida(dto.getNroPartida());
        entity.setDniPropietario(dto.getDniPropietario());
        return entity;
    }

    public List<DatosPropiedadesResponseDTO> convertToListDTO(List<DatosPropiedades> entities) {
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}