package com.ViviEstu.Unit;

import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.InteraccionMapper;
import com.ViviEstu.model.dto.request.InteraccionRequestDTO;
import com.ViviEstu.model.dto.response.InteraccionReporteResponseDTO;
import com.ViviEstu.model.dto.response.InteraccionResponseDTO;
import com.ViviEstu.model.entity.*;
import com.ViviEstu.repository.AlojamientoRepository;
import com.ViviEstu.repository.EstudiantesRepository;
import com.ViviEstu.repository.InteraccionesRepository;
import com.ViviEstu.service.InteraccionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InteraccionServiceTest {

    @Mock
    private InteraccionesRepository interaccionRepository;
    @Mock
    private InteraccionMapper interaccionMapper;
    @Mock
    private AlojamientoRepository alojamientoRepository;
    @Mock
    private EstudiantesRepository estudiantesRepository;
    @InjectMocks
    private InteraccionService interaccionService;

    private Interacciones interaccion;
    private InteraccionRequestDTO requestDTO;
    private Estudiantes estudiante;
    private Alojamiento alojamiento;
    private InteraccionResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        estudiante = new Estudiantes();
        estudiante.setId(1L);

        alojamiento = new Alojamiento();
        alojamiento.setId(1L);
        alojamiento.setTitulo("Alojamiento Test");

        interaccion = new Interacciones();
        interaccion.setId(1);
        interaccion.setEstudiante(estudiante);
        interaccion.setAlojamiento(alojamiento);
        interaccion.setFecha(LocalDateTime.now());

        requestDTO = new InteraccionRequestDTO();
        requestDTO.setEstudianteId(1L);
        requestDTO.setAlojamientoId(1L);

        responseDTO = new InteraccionResponseDTO();
        responseDTO.setId(1);
    }

    @Test
    @DisplayName("Obtener todas las interacciones")
    void testGetAllInteracciones() {
        when(interaccionRepository.findAll()).thenReturn(List.of(interaccion));
        when(interaccionMapper.convertToListDTO(any())).thenReturn(List.of(responseDTO));

        List<InteraccionResponseDTO> result = interaccionService.getAllInteracciones();

        assertEquals(1, result.size());
        verify(interaccionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Obtener interacción por ID - Éxito")
    void testGetInteraccionById_Exito() {
        when(interaccionRepository.findById(1)).thenReturn(Optional.of(interaccion));
        when(interaccionMapper.convertToDTO(any())).thenReturn(responseDTO);

        InteraccionResponseDTO result = interaccionService.getInteraccionById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    @DisplayName("Obtener interacción por ID - No encontrada")
    void testGetInteraccionById_NoEncontrada() {
        when(interaccionRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> interaccionService.getInteraccionById(1));
    }

    @Test
    @DisplayName("Crear interacción - Éxito")
    void testCreateInteraccion_Exito() {
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(interaccionRepository.save(any())).thenReturn(interaccion);
        when(interaccionMapper.convertToDTO(any())).thenReturn(responseDTO);

        InteraccionResponseDTO result = interaccionService.createInteraccion(requestDTO);

        assertNotNull(result);
        verify(interaccionRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Crear interacción - Estudiante no encontrado")
    void testCreateInteraccion_EstudianteNoEncontrado() {
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> interaccionService.createInteraccion(requestDTO));
    }

    @Test
    @DisplayName("Crear interacción - Alojamiento no encontrado")
    void testCreateInteraccion_AlojamientoNoEncontrado() {
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> interaccionService.createInteraccion(requestDTO));
    }

    @Test
    @DisplayName("Actualizar interacción - Éxito")
    void testUpdateInteraccion_Exito() {
        when(interaccionRepository.findById(1)).thenReturn(Optional.of(interaccion));
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.of(alojamiento));
        when(interaccionRepository.save(any())).thenReturn(interaccion);
        when(interaccionMapper.convertToDTO(any())).thenReturn(responseDTO);

        InteraccionResponseDTO result = interaccionService.updateInteraccion(1, requestDTO);

        assertNotNull(result);
        verify(interaccionRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Actualizar interacción - No encontrada")
    void testUpdateInteraccion_NoEncontrada() {
        when(interaccionRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> interaccionService.updateInteraccion(1, requestDTO));
    }

    @Test
    @DisplayName("Actualizar interacción - Estudiante no encontrado")
    void testUpdateInteraccion_EstudianteNoEncontrado() {
        when(interaccionRepository.findById(1)).thenReturn(Optional.of(interaccion));
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> interaccionService.updateInteraccion(1, requestDTO));
    }

    @Test
    @DisplayName("Actualizar interacción - Alojamiento no encontrado")
    void testUpdateInteraccion_AlojamientoNoEncontrado() {
        when(interaccionRepository.findById(1)).thenReturn(Optional.of(interaccion));
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(alojamientoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> interaccionService.updateInteraccion(1, requestDTO));
    }

    @Test
    @DisplayName("Contar interacciones por alojamiento")
    void testContarPorAlojamiento() {
        when(interaccionRepository.contarPorAlojamiento(1L)).thenReturn(5L);
        long count = interaccionService.contarPorAlojamiento(1L);
        assertEquals(5L, count);
    }

    @Test
    @DisplayName("Eliminar interacción - Éxito")
    void testDeleteInteraccion() {
        doNothing().when(interaccionRepository).deleteById(1);
        interaccionService.deleteInteraccion(1);
        verify(interaccionRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Generar reporte por alojamiento - Éxito")
    void testGenerarReportePorAlojamiento_Exito() {
        Universidad universidad = new Universidad();
        universidad.setNombre("UPC");
        Distrito distrito = new Distrito();
        distrito.setNombre("Lima");

        estudiante.setUniversidad(universidad);
        estudiante.setDistrito(distrito);

        interaccion.setEstudiante(estudiante);
        interaccion.setAlojamiento(alojamiento);
        interaccion.setFecha(LocalDateTime.now());

        when(interaccionRepository.findByAlojamiento_Id(1L)).thenReturn(List.of(interaccion));

        InteraccionReporteResponseDTO result = interaccionService.generarReportePorAlojamiento(1L);

        assertNotNull(result);
        assertEquals("Alojamiento Test", result.getNombreAlojamiento());
        assertEquals(1L, result.getTotalInteracciones());
    }

    @Test
    @DisplayName("Generar reporte por alojamiento - Sin interacciones")
    void testGenerarReportePorAlojamiento_SinInteracciones() {
        when(interaccionRepository.findByAlojamiento_Id(1L)).thenReturn(Collections.emptyList());
        assertThrows(ResourceNotFoundException.class, () -> interaccionService.generarReportePorAlojamiento(1L));
    }

    @Test
    @DisplayName("Generar reporte por alojamiento - Estudiantes sin universidad")
    void testGenerarReportePorAlojamiento_SinUniversidad() {
        Estudiantes estudianteSinUniversidad = new Estudiantes();
        estudianteSinUniversidad.setId(1L);
        Distrito distrito = new Distrito();
        distrito.setNombre("Lima");
        estudianteSinUniversidad.setDistrito(distrito);

        Alojamiento alojamiento = new Alojamiento();
        alojamiento.setId(1L);
        alojamiento.setTitulo("Alojamiento sin universidad");

        Interacciones interaccion = new Interacciones();
        interaccion.setAlojamiento(alojamiento);
        interaccion.setEstudiante(estudianteSinUniversidad);
        interaccion.setFecha(LocalDateTime.now());

        when(interaccionRepository.findByAlojamiento_Id(1L)).thenReturn(List.of(interaccion));

        InteraccionReporteResponseDTO result = interaccionService.generarReportePorAlojamiento(1L);

        assertNotNull(result);
        assertEquals("Sin universidad", result.getUniversidadPrincipal());
    }

    @Test
    @DisplayName("Generar reporte por alojamiento - Estudiantes sin distrito")
    void testGenerarReportePorAlojamiento_SinDistrito() {
        Universidad universidad = new Universidad();
        universidad.setNombre("San Marcos");

        Estudiantes estudianteSinDistrito = new Estudiantes();
        estudianteSinDistrito.setId(2L);
        estudianteSinDistrito.setUniversidad(universidad);


        Alojamiento alojamiento = new Alojamiento();
        alojamiento.setId(1L);
        alojamiento.setTitulo("Alojamiento sin distrito");

        Interacciones interaccion = new Interacciones();
        interaccion.setAlojamiento(alojamiento);
        interaccion.setEstudiante(estudianteSinDistrito);
        interaccion.setFecha(LocalDateTime.now());

        when(interaccionRepository.findByAlojamiento_Id(1L)).thenReturn(List.of(interaccion));

        InteraccionReporteResponseDTO result = interaccionService.generarReportePorAlojamiento(1L);

        assertNotNull(result);
        assertEquals("Sin distrito", result.getDistritoPrincipal());
    }

}
