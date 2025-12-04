package com.ViviEstu.mapper;

import com.ViviEstu.model.dto.request.DatosUniversitariosRequestDTO;
import com.ViviEstu.model.dto.response.DatosUniversitariosResponseDTO;
import com.ViviEstu.model.entity.DatosUniversitarios;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DatosUniversitariosMapper {

    public DatosUniversitariosResponseDTO convertToDTO(DatosUniversitarios entity) {
        if (entity == null) return null;
        DatosUniversitariosResponseDTO dto = new DatosUniversitariosResponseDTO();
        dto.setId(entity.getId());
        dto.setCorreoInstitucional(entity.getCorreoInstitucional());
        return dto;
    }

    public DatosUniversitarios convertToEntity(DatosUniversitariosRequestDTO dto) {
        if (dto == null) return null;
        DatosUniversitarios entity = new DatosUniversitarios();
        entity.setCorreoInstitucional(dto.getCorreoInstitucional());
        return entity;
    }

    public List<DatosUniversitariosResponseDTO> convertToListDTO(List<DatosUniversitarios> entities) {
        return entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}