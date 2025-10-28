package com.ViviEstu.service;

import com.ViviEstu.model.dto.request.FavoritosRequestDTO;
import com.ViviEstu.model.dto.response.FavoritosResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.Estudiantes;
import com.ViviEstu.model.entity.Favoritos;
import com.ViviEstu.repository.AlojamientoRepository;
import com.ViviEstu.repository.EstudiantesRepository;
import com.ViviEstu.repository.FavoritosRepository;
import com.ViviEstu.mapper.FavoritosMapper;
import com.ViviEstu.exception.BadRequestException;
import com.ViviEstu.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FavoritosService {

    private static final int MAX_FAVORITOS = 15;
    private final FavoritosRepository favoritosRepository;
    private final EstudiantesRepository estudiantesRepository;
    private final AlojamientoRepository alojamientoRepository;
    private final FavoritosMapper favoritosMapper;

    @Transactional
    public FavoritosResponseDTO addFavorito(Long estudianteId, Long alojamientoId) {

        Estudiantes estudiante = estudiantesRepository.findById(estudianteId)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + estudianteId));

        Alojamiento alojamiento = alojamientoRepository.findById(alojamientoId)
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con id: " + alojamientoId));


        // Validar que no se ha alcanzado el límite de favoritos (RN-023)
        if (favoritosRepository.countByEstudianteId(estudiante.getId()) >= MAX_FAVORITOS) {
            throw new BadRequestException("Límite de favoritos alcanzado. No se pueden agregar más de " + MAX_FAVORITOS + ".");
        }

        // Validar que el favorito no exista previamente
        if (favoritosRepository.existsByEstudianteIdAndAlojamientoId(estudiante.getId(), alojamiento.getId())) {
            throw new BadRequestException("Este alojamiento ya está en tu lista de favoritos.");
        }


        Favoritos favorito = new Favoritos();
        favorito.setEstudiante(estudiante);
        favorito.setAlojamiento(alojamiento);

        Favoritos savedFavorito = favoritosRepository.save(favorito);

        return favoritosMapper.convertToDTO(savedFavorito);
    }

    @Transactional(readOnly = true)
    public List<FavoritosResponseDTO> getFavoritosByEstudianteId(Long estudianteId) {
        if (!estudiantesRepository.existsById(estudianteId)) {
            throw new ResourceNotFoundException("Estudiante no encontrado con id: " + estudianteId);
        }
        List<Favoritos> favoritos = favoritosRepository.findByEstudianteId(estudianteId);
        return favoritos.stream()
                .map(favoritosMapper::convertToDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public void removeFavorito(Long estudianteId, Long alojamientoId) {
        if (!favoritosRepository.existsByEstudianteIdAndAlojamientoId(estudianteId, alojamientoId)) {
            throw new ResourceNotFoundException("Favorito no encontrado para el estudiante " + estudianteId + " y alojamiento " + alojamientoId);
        }
        favoritosRepository.deleteByEstudianteIdAndAlojamientoId(estudianteId, alojamientoId);
    }

}