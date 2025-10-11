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

    /**
     * Agrega un alojamiento a la lista de favoritos de un estudiante.
     * @param requestDTO DTO con los IDs del estudiante y el alojamiento.
     * @return el DTO del favorito creado.
     */
    @Transactional
    public FavoritosResponseDTO addFavorito(FavoritosRequestDTO requestDTO) {
        // Validar que el estudiante existe y está verificado (RN-024)
        Estudiantes estudiante = estudiantesRepository.findById(requestDTO.getEstudianteId())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + requestDTO.getEstudianteId()));

        // Asumiendo que la entidad Estudiantes tiene un campo booleano 'verificado'
        if (!estudiante.isVerificado()) {
            throw new BadRequestException("Solo los estudiantes verificados pueden agregar favoritos.");
        }

        // Validar que el alojamiento existe y está disponible
        Alojamiento alojamiento = alojamientoRepository.findById(requestDTO.getAlojamientoId())
                .orElseThrow(() -> new ResourceNotFoundException("Alojamiento no encontrado con id: " + requestDTO.getAlojamientoId()));

        // Asumiendo que la entidad Alojamiento tiene un campo booleano 'alquilado'
        if (alojamiento.getAlquilado()) {
            throw new BadRequestException("El alojamiento ya no está disponible.");
        }

        // Validar que no se ha alcanzado el límite de favoritos (RN-023)
        if (favoritosRepository.countByEstudianteId(estudiante.getId()) >= MAX_FAVORITOS) {
            throw new BadRequestException("Límite de favoritos alcanzado. No se pueden agregar más de " + MAX_FAVORITOS + ".");
        }

        // Validar que el favorito no exista previamente
        if (favoritosRepository.existsByEstudianteIdAndAlojamientoId(estudiante.getId(), alojamiento.getId())) {
            throw new BadRequestException("Este alojamiento ya está en tu lista de favoritos.");
        }

        // Crear y guardar el nuevo favorito
        Favoritos favorito = new Favoritos();
        favorito.setEstudiante(estudiante);
        favorito.setAlojamiento(alojamiento);

        Favoritos savedFavorito = favoritosRepository.save(favorito);
        return favoritosMapper.convertToDTO(savedFavorito);
    }

    /**
     * Obtiene todos los favoritos de un estudiante.
     * @param estudianteId el ID del estudiante.
     * @return una lista de DTOs de los favoritos.
     */
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

    /**
     * Elimina un favorito de la lista de un estudiante.
     * @param favoritoId el ID del favorito a eliminar.
     */
    @Transactional
    public void removeFavorito(Long favoritoId) {
        if (!favoritosRepository.existsById(favoritoId)) {
            throw new ResourceNotFoundException("Favorito no encontrado con id: " + favoritoId);
        }
        favoritosRepository.deleteById(favoritoId);
    }

    /**
     * Elimina un favorito de la lista de un estudiante usando el ID del estudiante y el ID del alojamiento.
     * @param estudianteId el ID del estudiante.
     * @param alojamientoId el ID del alojamiento.
     */
    @Transactional
    public void removeFavorito(Long estudianteId, Long alojamientoId) {
        if (!favoritosRepository.existsByEstudianteIdAndAlojamientoId(estudianteId, alojamientoId)) {
            throw new ResourceNotFoundException("Favorito no encontrado para el estudiante " + estudianteId + " y alojamiento " + alojamientoId);
        }
        favoritosRepository.deleteByEstudianteIdAndAlojamientoId(estudianteId, alojamientoId);
    }

    /**
     * Elimina un favorito de la lista de un estudiante usando el ID del favorito.
     * @param favoritoId el ID del favorito a eliminar.
     */
    @Transactional
    public void removeFavoritoById(Long favoritoId) {
        if (!favoritosRepository.existsById(favoritoId)) {
            throw new ResourceNotFoundException("Favorito no encontrado con id: " + favoritoId);
        }
        favoritosRepository.deleteById(favoritoId);
    }
}