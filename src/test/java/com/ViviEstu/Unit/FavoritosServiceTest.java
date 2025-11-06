package com.ViviEstu.Unit;

import com.ViviEstu.exception.BadRequestException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.FavoritosMapper;
import com.ViviEstu.model.dto.response.FavoritosResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.Estudiantes;
import com.ViviEstu.model.entity.Favoritos;
import com.ViviEstu.repository.AlojamientoRepository;
import com.ViviEstu.repository.EstudiantesRepository;
import com.ViviEstu.repository.FavoritosRepository;
import com.ViviEstu.service.FavoritosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FavoritosServiceTest {

    @Mock
    private FavoritosRepository favoritosRepository;

    @Mock
    private EstudiantesRepository estudiantesRepository;

    @Mock
    private AlojamientoRepository alojamientoRepository;

    @Mock
    private FavoritosMapper favoritosMapper;

    @InjectMocks
    private FavoritosService favoritosService;

    private Estudiantes estudiante;
    private Alojamiento alojamiento;
    private Favoritos favorito;
    private FavoritosResponseDTO favoritoResponseDTO;

    @BeforeEach
    void setUp() {
        estudiante = new Estudiantes();
        estudiante.setId(1L);
        estudiante.setNombre("David");

        alojamiento = new Alojamiento();
        alojamiento.setId(10L);
        alojamiento.setTitulo("Residencia Universitaria Lima");

        favorito = new Favoritos();
        favorito.setId(100);
        favorito.setEstudiante(estudiante);
        favorito.setAlojamiento(alojamiento);

        favoritoResponseDTO = new FavoritosResponseDTO();
        favoritoResponseDTO.setId(100L);
        favoritoResponseDTO.setAlojamientoId(10L);
        favoritoResponseDTO.setEstudianteId(1L);
    }

    @Test
    @DisplayName("Debe agregar un alojamiento a favoritos exitosamente")
    void addFavorito_Success() {
        // Arrange
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(alojamientoRepository.findById(10L)).thenReturn(Optional.of(alojamiento));
        when(favoritosRepository.countByEstudianteId(1L)).thenReturn(5L);
        when(favoritosRepository.existsByEstudianteIdAndAlojamientoId(1L, 10L)).thenReturn(false);
        when(favoritosRepository.save(any(Favoritos.class))).thenReturn(favorito);
        when(favoritosMapper.convertToDTO(any(Favoritos.class))).thenReturn(favoritoResponseDTO);

        // Act
        FavoritosResponseDTO result = favoritosService.addFavorito(1L, 10L);

        // Assert
        assertNotNull(result);
        assertThat(result.getEstudianteId()).isEqualTo(1L);
        assertThat(result.getAlojamientoId()).isEqualTo(10L);
        verify(favoritosRepository, times(1)).save(any(Favoritos.class));
    }


    @Test
    @DisplayName("Debe lanzar excepción cuando el estudiante supera el límite de favoritos")
    void addFavorito_LimiteAlcanzado_ThrowsException() {
        // Arrange
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(alojamientoRepository.findById(10L)).thenReturn(Optional.of(alojamiento));
        when(favoritosRepository.countByEstudianteId(1L)).thenReturn(15L);

        // Act & Assert
        assertThatThrownBy(() -> favoritosService.addFavorito(1L, 10L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Límite de favoritos alcanzado");

        verify(favoritosRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el alojamiento ya está en favoritos")
    void addFavorito_YaExiste_ThrowsException() {
        // Arrange
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(alojamientoRepository.findById(10L)).thenReturn(Optional.of(alojamiento));
        when(favoritosRepository.countByEstudianteId(1L)).thenReturn(3L);
        when(favoritosRepository.existsByEstudianteIdAndAlojamientoId(1L, 10L)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> favoritosService.addFavorito(1L, 10L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("ya está en tu lista de favoritos");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el estudiante no existe")
    void addFavorito_EstudianteNoEncontrado_ThrowsException() {

        // Arrange
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> favoritosService.addFavorito(1L, 10L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Estudiante no encontrado");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el alojamiento no existe")
    void addFavorito_AlojamientoNoEncontrado_ThrowsException() {
        // Arrange
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(alojamientoRepository.findById(10L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> favoritosService.addFavorito(1L, 10L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Alojamiento no encontrado");
    }

    @Test
    @DisplayName("Debe retornar todos los favoritos del estudiante exitosamente")
    void getFavoritosByEstudianteId_Success() {
        // Arrange
        when(estudiantesRepository.existsById(1L)).thenReturn(true);
        when(favoritosRepository.findByEstudianteId(1L)).thenReturn(List.of(favorito));
        when(favoritosMapper.convertToDTO(favorito)).thenReturn(favoritoResponseDTO);

        // Act
        var result = favoritosService.getFavoritosByEstudianteId(1L);

        // Assert
        assertNotNull(result);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getAlojamientoId()).isEqualTo(10L);
        verify(favoritosRepository, times(1)).findByEstudianteId(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el estudiante no existe al consultar favoritos")
    void getFavoritosByEstudianteId_NoEncontrado_ThrowsException() {
        // Arrange
        when(estudiantesRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> favoritosService.getFavoritosByEstudianteId(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Estudiante no encontrado");
    }

    @Test
    @DisplayName("Debe eliminar un favorito exitosamente cuando existe")
    void removeFavorito_Success() {
        // Arrange
        when(favoritosRepository.existsByEstudianteIdAndAlojamientoId(1L, 10L)).thenReturn(true);

        // Act
        favoritosService.removeFavorito(1L, 10L);

        // Assert
        verify(favoritosRepository, times(1)).deleteByEstudianteIdAndAlojamientoId(1L, 10L);
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar eliminar un favorito inexistente")
    void removeFavorito_NoEncontrado_ThrowsException() {
        // Arrange
        when(favoritosRepository.existsByEstudianteIdAndAlojamientoId(1L, 10L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> favoritosService.removeFavorito(1L, 10L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Favorito no encontrado");

        verify(favoritosRepository, never()).deleteByEstudianteIdAndAlojamientoId(anyLong(), anyLong());
    }
}
