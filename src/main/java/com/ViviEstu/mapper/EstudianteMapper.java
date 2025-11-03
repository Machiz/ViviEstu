package com.ViviEstu.mapper;


import com.ViviEstu.model.dto.request.EstudiantesRequestDTO;
import com.ViviEstu.model.dto.response.EstudianteResponseDTO;
import com.ViviEstu.model.dto.response.PropietariosResponseDTO;
import com.ViviEstu.model.entity.Estudiantes;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class EstudianteMapper {

    private final ModelMapper modelMapper;


    public EstudianteResponseDTO convertToDTO(Estudiantes estudiante) {
        EstudianteResponseDTO dto = modelMapper.map(estudiante, EstudianteResponseDTO.class);
        dto.setCorreo(estudiante.getUser().getCorreo());
        return dto;
    }

    public List<EstudianteResponseDTO> convertToListDTO(List<Estudiantes> estudiantes) {
        return estudiantes.stream()
                .map(this::convertToDTO)
                .toList();
    }

}
