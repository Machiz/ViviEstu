package com.ViviEstu.mapper;

import com.ViviEstu.model.dto.request.TransporteRequestDTO;
import com.ViviEstu.model.entity.Transporte;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TransporteMapper {
    private final ModelMapper modelMapper;
    public Transporte convertToEntity(TransporteRequestDTO transporteRequestDTO) {
        return modelMapper.map(transporteRequestDTO, Transporte.class);
    }
    public TransporteRequestDTO convertToDTO(Transporte transporte) {
        return modelMapper.map(transporte, TransporteRequestDTO.class);
    }
    public List<Transporte> convertToEntity(List<TransporteRequestDTO> transporteRequestDTO) {
        return transporteRequestDTO.stream()
                .map(this::convertToEntity)
                .toList();
    }
}
