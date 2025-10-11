package com.ViviEstu.mapper;


import com.ViviEstu.model.dto.request.NotificacionesPropieRequestDTO;
import com.ViviEstu.model.dto.response.NotificacionesPropieResponseDTO;
import com.ViviEstu.model.dto.response.PropietariosResponseDTO;
import com.ViviEstu.model.entity.NotificacionPropie;
import com.ViviEstu.model.entity.Propietarios;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class NotificacionesPropieMapper {

    private final ModelMapper mapper = new ModelMapper();
    private final ModelMapper modelMapper;

    public NotificacionPropie convertToEntity(NotificacionesPropieRequestDTO dto) {
        return mapper.map(dto, NotificacionPropie.class);
    }

    public NotificacionesPropieResponseDTO convertToDTO(NotificacionPropie notiPropietarios) {
        return modelMapper.map(notiPropietarios, NotificacionesPropieResponseDTO.class);
    }

    public List<NotificacionesPropieResponseDTO> convertToListDTO(List<NotificacionPropie> list) {
        return list.stream()
                .map(this::convertToDTO)
                .toList();
    }
}
