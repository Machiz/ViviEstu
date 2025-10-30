package com.ViviEstu.Unit;


import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.AlojamientoMapper;
import com.ViviEstu.model.dto.request.AlojamientoRequestDTO;
import com.ViviEstu.model.dto.response.AlojamientoResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.Distrito;
import com.ViviEstu.model.entity.Propietarios;
import com.ViviEstu.repository.*;
import com.ViviEstu.service.AlojamientoService;
import com.ViviEstu.service.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlojamientoServiceTest {
    @Mock
    private AlojamientoRepository alojamientoRepository;
    @Mock
    private DistritoRepository distritoRepository;
    @Mock
    private PropietariosRepository propietariosRepository;
    @Mock
    private ImagenesRepository imagenesRepository;
    @Mock
    private AlojamientoMapper mapper;
    @Mock
    private DatosPropiedadesRepository datosPropiedadesRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private AlojamientoService alojamientoService;

    private AlojamientoRequestDTO alojamientoRequestDTO;
    private Propietarios propietario;
    private Distrito distrito;
    private Alojamiento alojamiento;
    private AlojamientoResponseDTO alojamientoResponseDTO;

    @BeforeEach
    void setUp() {
        // Configuración de datos de prueba
        alojamientoRequestDTO = new AlojamientoRequestDTO();
        alojamientoRequestDTO.setTitulo("Título de prueba");
        alojamientoRequestDTO.setDescripcion("Esta es una descripción de más de cincuenta caracteres para la prueba.");
        alojamientoRequestDTO.setDireccion("Dirección de prueba");
        alojamientoRequestDTO.setPrecioMensual(new BigDecimal("500.00"));
        alojamientoRequestDTO.setNroPartida("12345678");
        alojamientoRequestDTO.setAlquilado(false);
        alojamientoRequestDTO.setPropietarioId(1L);
        alojamientoRequestDTO.setDistritoId(1L);
        MultipartFile mockFile = mock(MultipartFile.class);
        alojamientoRequestDTO.setImagenes(Collections.singletonList(mockFile));

        propietario = new Propietarios();
        propietario.setId(1L);
        propietario.setDni("12345678A");

        distrito = new Distrito();
        distrito.setId(1L);

        alojamiento = new Alojamiento();
        alojamiento.setId(1L);
        alojamiento.setTitulo(alojamientoRequestDTO.getTitulo());

        alojamientoResponseDTO = new AlojamientoResponseDTO();
        alojamientoResponseDTO.setId(1L);
        alojamientoResponseDTO.setTitulo("Título de prueba");
    }

    @Test
    @DisplayName("Crear Alojamiento - Publicación Exitosa")
    void testCrearAlojamiento_PublicacionExitosa() throws IOException {
        // Arrange
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(false);
        when(distritoRepository.findById(anyLong())).thenReturn(Optional.of(distrito));
        when(propietariosRepository.findById(anyLong())).thenReturn(Optional.of(propietario));
        when(datosPropiedadesRepository.existsByDniPropietarioAndNroPartida(anyString(), anyString())).thenReturn(true);
        when(alojamientoRepository.save(any(Alojamiento.class))).thenReturn(alojamiento);

        Map<String, String> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "http://example.com/image.jpg");
        uploadResult.put("public_id", "public_id_123");
        when(cloudinaryService.subirImagen(any(MultipartFile.class))).thenReturn(uploadResult);

        when(mapper.convertToDTO(any(Alojamiento.class))).thenReturn(alojamientoResponseDTO);

        // Act
        AlojamientoResponseDTO result = alojamientoService.crearAlojamiento(alojamientoRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(alojamientoResponseDTO.getId(), result.getId());
        assertEquals(alojamientoResponseDTO.getTitulo(), result.getTitulo());

        verify(alojamientoRepository, times(1)).existsByNroPartida("12345678");
        verify(propietariosRepository, times(1)).findById(1L);
        verify(datosPropiedadesRepository, times(1)).existsByDniPropietarioAndNroPartida("12345678A", "12345678");
        verify(alojamientoRepository, times(1)).save(any(Alojamiento.class));
        verify(cloudinaryService, times(1)).subirImagen(any(MultipartFile.class));
        verify(imagenesRepository, times(1)).save(any());
        verify(mapper, times(1)).convertToDTO(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Descripción Corta")
    void testCrearAlojamiento_DescripcionCorta() {
        // Arrange
        alojamientoRequestDTO.setDescripcion("Depa"); // Menos de 50 caracteres

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            alojamientoService.crearAlojamiento(alojamientoRequestDTO);
        });

        assertEquals("La descripción debe tener al menos 50 caracteres.", exception.getMessage());

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Precio Fuera de Rango (Menor)")
    void testCrearAlojamiento_PrecioMenorAlMinimo() {
        // Arrange
        alojamientoRequestDTO.setPrecioMensual(new BigDecimal("100.00")); // Menor a 200

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            alojamientoService.crearAlojamiento(alojamientoRequestDTO);
        });

        assertEquals("El precio debe estar entre S/200 y S/5000.", exception.getMessage());

        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
    }

    @Test
    @DisplayName("Crear Alojamiento - Documentación Insuficiente")
    void testCrearAlojamiento_DocumentacionInsuficiente() throws IOException {
        // Arrange
        when(alojamientoRepository.existsByNroPartida(anyString())).thenReturn(false);
        when(distritoRepository.findById(anyLong())).thenReturn(Optional.of(distrito));
        when(propietariosRepository.findById(anyLong())).thenReturn(Optional.of(propietario));
        // Simular que la documentación (DNI + Nro Partida) no se encuentra
        when(datosPropiedadesRepository.existsByDniPropietarioAndNroPartida(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            alojamientoService.crearAlojamiento(alojamientoRequestDTO);
        });

        assertEquals("Datos no encontrados en base de datos", exception.getMessage());

        // Verificar que no se guardó el alojamiento ni se subieron imágenes
        verify(alojamientoRepository, never()).save(any(Alojamiento.class));
        verify(cloudinaryService, never()).subirImagen(any());
    }
}
