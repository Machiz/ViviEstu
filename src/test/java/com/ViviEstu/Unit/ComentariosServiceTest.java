package com.ViviEstu.Unit;

import com.ViviEstu.mapper.ComentarioMapper;
import com.ViviEstu.model.dto.request.ComentarioRequestDTO;
import com.ViviEstu.model.dto.response.ComentarioResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.Comentario;
import com.ViviEstu.model.entity.Estudiantes;
import com.ViviEstu.repository.AlojamientoRepository;
import com.ViviEstu.repository.ComentarioRepository;
import com.ViviEstu.repository.EstudiantesRepository;
import com.ViviEstu.service.ComentarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ViviEstu.exception.ResourceNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ComentariosServiceTest {
    @Mock
    private ComentarioRepository comentarioRepository;
    @Mock
    private AlojamientoRepository alojamientoRepository;
    @Mock
    private EstudiantesRepository estudiantesRepository;
    @Mock
    private ComentarioMapper comentarioMapper;

    @InjectMocks
    private ComentarioService comentarioService;

    private ComentarioRequestDTO requestDTO;
    private Estudiantes estudiante;
    private Alojamiento alojamiento;
    private Comentario comentario;
    private ComentarioResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        // Datos de entrada
        requestDTO = new ComentarioRequestDTO(1L, 1L, "Este es un comentario de prueba con más de 150 caracteres para asegurar que la validación de longitud, si existiera en otro lugar, no sea un problema aquí. El contenido es detallado y relevante.");

        // Entidades simuladas
        estudiante = new Estudiantes();
        estudiante.setId(1L);
        estudiante.setNombre("Juan Perez");

        alojamiento = new Alojamiento();
        alojamiento.setId(1L);
        alojamiento.setTitulo("Excelente Alojamiento");

        comentario = new Comentario(1L, requestDTO.getContenido(), alojamiento, estudiante);

        // DTO de respuesta simulado
        responseDTO = new ComentarioResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setContenido(requestDTO.getContenido());
        responseDTO.setEstudianteNombre(estudiante.getNombre());
        responseDTO.setAlojamientoTitulo(alojamiento.getTitulo());
    }

    @Test
    @DisplayName("Usuario registrado publica comentario exitosamente")
    void testRegistrarComentario_Exitoso() {
        // Arrange
        when(estudiantesRepository.findById(requestDTO.getEstudianteId())).thenReturn(Optional.of(estudiante));
        when(alojamientoRepository.findById(requestDTO.getAlojamientoId())).thenReturn(Optional.of(alojamiento));
        when(comentarioRepository.save(any(Comentario.class))).thenReturn(comentario);
        when(comentarioMapper.toDTO(any(Comentario.class))).thenReturn(responseDTO);

        // Act
        ComentarioResponseDTO result = comentarioService.registrar(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO.getContenido(), result.getContenido());
        assertEquals(responseDTO.getEstudianteNombre(), result.getEstudianteNombre());

        verify(estudiantesRepository).findById(requestDTO.getEstudianteId());
        verify(alojamientoRepository).findById(requestDTO.getAlojamientoId());
        verify(comentarioRepository).save(any(Comentario.class));
        verify(comentarioMapper).toDTO(any(Comentario.class));
    }

    @Test
    @DisplayName("Intento de enviar comentario vacío")
    void testRegistrarComentario_ContenidoVacio() {
        // Arrange
        requestDTO.setContenido("   "); // Simular contenido vacío o con espacios

        // Act & Assert
        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            comentarioService.registrar(requestDTO);
        });

        assertEquals("El contenido del comentario no puede estar vacío", exception.getMessage());

        // Verificar que no se intentó guardar
        org.mockito.Mockito.verify(comentarioRepository, org.mockito.Mockito.never()).save(any());
    }

    @Test
    @DisplayName("Comentario excede longitud máxima")
    void testRegistrarComentario_ContenidoExcedeLongitudMaxima() {
        // Arrange
        String contenidoLargo = "a".repeat(501); // Generar un string de 501 caracteres
        requestDTO.setContenido(contenidoLargo);

        // Act & Assert
        IllegalArgumentException exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
            comentarioService.registrar(requestDTO);
        });

        assertEquals("El comentario no puede exceder los 500 caracteres", exception.getMessage());

        // Verificar que no se intentó guardar
        org.mockito.Mockito.verify(comentarioRepository, org.mockito.Mockito.never()).save(any());
    }

    @Test
    @DisplayName("Usuario anónimo intenta comentar")
    void testRegistrarComentario_UsuarioNoAutenticado() {
        // Arrange
        // Simular que el estudiante no se encuentra (usuario anónimo)
        when(estudiantesRepository.findById(requestDTO.getEstudianteId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            comentarioService.registrar(requestDTO);
        });

        assertEquals("Estudiante no encontrado con ID: " + requestDTO.getEstudianteId(), exception.getMessage());

        // Verificar que no se intentó guardar el comentario ni buscar el alojamiento
        org.mockito.Mockito.verify(comentarioRepository, org.mockito.Mockito.never()).save(any());
        org.mockito.Mockito.verify(alojamientoRepository, org.mockito.Mockito.never()).findById(any());
    }
}
