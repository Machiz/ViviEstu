package com.ViviEstu.mapper;

import com.ViviEstu.model.dto.request.AlojamientoRequestDTO;
import com.ViviEstu.model.dto.response.AlojamientoResponseDTO;
import com.ViviEstu.model.dto.response.ImagenResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.ImagenesAlojamiento;
import com.ViviEstu.model.entity.Transporte;
import com.ViviEstu.repository.ImagenesRepository;
import com.ViviEstu.repository.TransporteRepository;
import com.ViviEstu.repository.UniAlojamientoRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AlojamientoMapper {


    private final ModelMapper mapper;
    private final ImagenesRepository imagenesRepository;
    private final TransporteRepository transporteRepository;
    private final UniAlojamientoRepository uniAlojamientoRepository;

    public Alojamiento convertToEntity(AlojamientoRequestDTO dto) {
        return mapper.map(dto, Alojamiento.class);
    }

    public AlojamientoResponseDTO convertToDTO(Alojamiento alojamiento) {
        AlojamientoResponseDTO dto = mapper.map(alojamiento, AlojamientoResponseDTO.class);
        dto.setPropietario(alojamiento.getPropietario().getNombre());
        dto.setDistrito(alojamiento.getDistrito().getNombre());

        List<ImagenResponseDTO> imagenes = imagenesRepository.findByAlojamientoId(alojamiento.getId())
                .stream()
                .map(img -> {
                    ImagenResponseDTO imgDto = new ImagenResponseDTO();
                    imgDto.setId(img.getId());
                    imgDto.setUrl(img.getUrl());
                    return imgDto;
                })
                .collect(Collectors.toList());


        List<String> transportes = transporteRepository.findByAlojamientoId(alojamiento.getId())
                .stream()
                .map(Transporte::getNombre)
                .toList();

        // === UNIVERSIDADES ===
        List<String> universidades = uniAlojamientoRepository.findByAlojamientoId(alojamiento.getId())
                .stream()
                .map(rel -> rel.getUniversidad().getNombre())
                .toList();

        dto.setUniversidades(universidades);


        dto.setTransportes(transportes);

        dto.setImagenes(imagenes);
        return dto;
    }

    public List<AlojamientoResponseDTO> convertToListDTO(List<Alojamiento> list) {
        return list.stream().map(this::convertToDTO).toList();
    }
}
