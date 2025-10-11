package com.ViviEstu.mapper;


import com.ViviEstu.model.dto.request.InteraccionRequestDTO;
import com.ViviEstu.model.dto.response.InteraccionResponseDTO;
import com.ViviEstu.model.entity.Interacciones;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class InteraccionMapper {

    private final ModelMapper modelMapper;

    public Interacciones convertToEntity(InteraccionRequestDTO requestDTO) {
        return modelMapper.map(requestDTO, Interacciones.class);
    }

    public InteraccionResponseDTO convertToDTO(Interacciones interaccion) {
        return modelMapper.map(interaccion, InteraccionResponseDTO.class);
    }

    public List<InteraccionResponseDTO> convertToListDTO(List<Interacciones> interacciones) {
        return interacciones.stream()
                .map(this::convertToDTO)
                .toList();
    }
}
