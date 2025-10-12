package com.ViviEstu.mapper;

import com.ViviEstu.model.dto.request.ComentarioRequestDTO;
import com.ViviEstu.model.dto.response.ComentarioResponseDTO;
import com.ViviEstu.model.entity.Comentario;
import lombok.AllArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ComentarioMapper {

    private final ModelMapper mapper;

    public Comentario toEntity(ComentarioRequestDTO dto) {
        return mapper.map(dto, Comentario.class);
    }

    // 🔹 Convierte de Entidad a DTO
    public ComentarioResponseDTO toDTO(Comentario entity) {
        return mapper.map(entity, ComentarioResponseDTO.class);
    }

    // 🔹 Convierte una lista de entidades a una lista de DTOs
    public List<ComentarioResponseDTO> toListDTO(List<Comentario> list) {
        return list.stream().map(this::toDTO).toList();
    }
}
