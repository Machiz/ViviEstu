package com.ViviEstu.mapper;

import com.ViviEstu.model.dto.request.TransporteRequestDTO;
import com.ViviEstu.model.dto.response.TransporteResponseDTO;
import com.ViviEstu.model.entity.Transporte;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TransporteMapper {

    private final ModelMapper modelMapper;

    public Transporte convertToEntity(TransporteRequestDTO dto) {
        return modelMapper.map(dto, Transporte.class);
    }

    public TransporteResponseDTO convertToDTO(Transporte entity) {
        TransporteResponseDTO dto = modelMapper.map(entity, TransporteResponseDTO.class);
        if (entity.getDistrito() != null) {
            dto.setDistritoId(entity.getDistrito().getId());
            dto.setDistritoNombre(entity.getDistrito().getNombre());
        }
        return dto;
    }

    public List<TransporteResponseDTO> convertToListDTO(List<Transporte> list) {
        return list.stream()
                .map(this::convertToDTO)
                .toList();
    }
}
