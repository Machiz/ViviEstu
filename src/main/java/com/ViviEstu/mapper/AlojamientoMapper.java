package com.ViviEstu.mapper;

import com.ViviEstu.model.dto.request.AlojamientoRequestDTO;
import com.ViviEstu.model.dto.response.AlojamientoResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class AlojamientoMapper {

    private final ModelMapper mapper = new ModelMapper();

    public Alojamiento convertToEntity(AlojamientoRequestDTO dto) {
        return mapper.map(dto, Alojamiento.class);
    }

    public AlojamientoResponseDTO convertToDTO(Alojamiento alojamiento) {
        AlojamientoResponseDTO dto = mapper.map(alojamiento, AlojamientoResponseDTO.class);
        dto.setPropietario(alojamiento.getPropietario().getNombre());
        dto.setDistrito(alojamiento.getDistrito().getNombre());
        return dto;
    }

    public List<AlojamientoResponseDTO> convertToListDTO(List<Alojamiento> list) {
        return list.stream().map(this::convertToDTO).toList();
    }
}
