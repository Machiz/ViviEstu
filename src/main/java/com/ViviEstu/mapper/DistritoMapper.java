package com.ViviEstu.mapper;

import com.upc.viviestu.model.dto.DistritoRequestDTO;
import org.modelmapper.ModelMapper;
import com.upc.viviestu.model.dto.DistritoResponseDTO;
import com.upc.viviestu.model.entity.Distrito;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@AllArgsConstructor
public class DistritoMapper {

    private final ModelMapper modelMapper;

    public Distrito convertToEntity(DistritoRequestDTO distritoRequestDTO) {
        return modelMapper.map(distritoRequestDTO, Distrito.class);
    }

    public DistritoResponseDTO convertToDTO(Distrito distrito) {
        return modelMapper.map(distrito, DistritoResponseDTO.class);
    }

    public List<DistritoResponseDTO> convertListToDTO(List<Distrito> distritos) {
        return distritos.stream()
                .map(this::convertToDTO)
                .toList();
    }


}
