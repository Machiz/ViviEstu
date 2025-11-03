package com.ViviEstu.mapper;

import com.ViviEstu.model.dto.request.PropietariosRequestDTO;
import com.ViviEstu.model.dto.response.AlojamientoResponseDTO;
import com.ViviEstu.model.dto.response.PropietariosResponseDTO;
import com.ViviEstu.model.entity.Propietarios;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PropietariosMapper {

    private final ModelMapper mapper = new ModelMapper();

    public Propietarios toEntity(PropietariosRequestDTO dto) {
        return mapper.map(dto, Propietarios.class);
    }

    public PropietariosResponseDTO toDTO(Propietarios entity) {
        PropietariosResponseDTO dto = mapper.map(entity, PropietariosResponseDTO.class);
        dto.setCorreo(entity.getUser().getCorreo());
        return dto;
    }

    public List<PropietariosResponseDTO> toListDTO(List<Propietarios> list) {
        return list.stream().map(this::toDTO).toList();
    }
}
