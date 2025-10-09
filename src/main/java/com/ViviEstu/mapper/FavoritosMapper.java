package com.ViviEstu.mapper;

import com.ViviEstu.model.dto.request.FavoritosRequestDTO;
import com.ViviEstu.model.dto.response.FavoritosResponseDTO;
import com.ViviEstu.model.entity.Favoritos;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@AllArgsConstructor
public class FavoritosMapper {

    private final ModelMapper mapper = new ModelMapper();

    public Favoritos convertToEntity(FavoritosRequestDTO favoritosRequestDTO) {
        return mapper.map(favoritosRequestDTO, Favoritos.class);
    }

    public FavoritosResponseDTO convertToDTO(Favoritos favoritos) {
        return mapper.map(favoritos, FavoritosResponseDTO.class);
    }

    public List<FavoritosResponseDTO> convertToListDTO(List<Favoritos> favoritosList) {
        return favoritosList.stream()
                .map(this::convertToDTO)
                .toList();
    }
}
