package com.ViviEstu.mapper;


import com.ViviEstu.model.dto.request.EstudiantesRequestDTO;
import com.ViviEstu.model.dto.response.EstudianteResponseDTO;
import com.ViviEstu.model.entity.Estudiantes;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class EstudianteMapper {

    private final ModelMapper modelMapper;

    public Estudiantes convertToEntity(EstudiantesRequestDTO estudiantesRequestDTO) {
        return modelMapper.map(estudiantesRequestDTO, Estudiantes.class);
    }

    public EstudianteResponseDTO convertToDTO(Estudiantes estudiante) {
        return modelMapper.map(estudiante, EstudianteResponseDTO.class);
    }

    public List<EstudianteResponseDTO> convertToListDTO(List<Estudiantes> estudiantes) {
        return estudiantes.stream()
                .map(this::convertToDTO)
                .toList();
    }

}
