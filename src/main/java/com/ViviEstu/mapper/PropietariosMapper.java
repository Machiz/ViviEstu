package com.ViviEstu.mapper;
import com.ViviEstu.config.ModelMapperConfig;
import com.ViviEstu.model.dto.request.PropietariosRequestDTO;
import com.ViviEstu.model.dto.response.PropietariosResponseDTO;
import com.ViviEstu.model.entity.Propietarios;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PropietariosMapper {
    private final ModelMapper modelMapper;

    public Propietarios convertToEntity(PropietariosRequestDTO propietariosRequestDTO) {
        return modelMapper.map(propietariosRequestDTO, Propietarios.class);
    }

    public PropietariosResponseDTO convertToDTO(Propietarios propietarios) {
        return modelMapper.map(propietarios, PropietariosResponseDTO.class);
    }

    public List<PropietariosResponseDTO> convertListToDTO(List<Propietarios> propietariosList) {
        return propietariosList.stream()
                .map(this::convertToDTO)
                .toList();
    }

}
