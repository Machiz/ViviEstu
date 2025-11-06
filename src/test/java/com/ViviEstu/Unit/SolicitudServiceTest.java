package com.ViviEstu.service;

import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.SolicitudMapper;
import com.ViviEstu.model.dto.request.SolicitudRequestDTO;
import com.ViviEstu.model.dto.response.SolicitudResponseDTO;
import com.ViviEstu.model.entity.Alojamiento;
import com.ViviEstu.model.entity.Estudiantes;
import com.ViviEstu.model.entity.Solicitudes;
import com.ViviEstu.repository.AlojamientoRepository;
import com.ViviEstu.repository.EstudiantesRepository;
import com.ViviEstu.repository.SolicitudRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private EstudiantesRepository estudiantesRepository;

    @Mock
    private AlojamientoRepository alojamientoRepository;

    @Mock
    private SolicitudMapper mapper;

    @InjectMocks
    private SolicitudService solicitudService;

    private Estudiantes estudiante;
    private Alojamiento alojamiento;
    private Solicitudes solicitud;
    private SolicitudRequestDTO requestDTO;
    private SolicitudResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        estudiante = new Estudiantes();
        estudiante.setId(1L);

        alojamiento = new Alojamiento();
        alojamiento.setId(10L);

        solicitud = new Solicitudes();
        solicitud.setId(100L);
        solicitud.setEstado("PENDIENTE");
        solicitud.setEstudiantes(estudiante);
        solicitud.setAlojamiento(alojamiento);

        requestDTO = new SolicitudRequestDTO();
        requestDTO.setEstudiantesId(1L);
        requestDTO.setAlojamientoId(10L);
        requestDTO.setCantInquilinos(2);
        requestDTO.setMesesAlquiler(6);
        requestDTO.setMensaje("Mensaje de prueba");
        requestDTO.setOferta(850.0);

        responseDTO = new SolicitudResponseDTO();
        responseDTO.setId(100L);
        responseDTO.setEstado("PENDIENTE");
    }

    // ------------------------- listar() -------------------------

    @Test
    @DisplayName("listar(): retorna lista de solicitudes correctamente")
    void listar_Success() {
        when(solicitudRepository.findAll()).thenReturn(List.of(solicitud));
        when(mapper.toDTO(solicitud)).thenReturn(responseDTO);

        List<SolicitudResponseDTO> result = solicitudService.listar();

        assertThat(result).hasSize(1);
        verify(solicitudRepository).findAll();
    }

    @Test
    @DisplayName("listar(): lista vacía retorna lista vacía sin excepción")
    void listar_EmptyList_ReturnsEmpty() {
        when(solicitudRepository.findAll()).thenReturn(Collections.emptyList());

        List<SolicitudResponseDTO> result = solicitudService.listar();

        assertThat(result).isEmpty();
        verify(solicitudRepository).findAll();
    }

    // ------------------------- obtenerPorId() -------------------------

    @Test
    @DisplayName("obtenerPorId(): retorna solicitud correctamente")
    void obtenerPorId_Success() {
        when(solicitudRepository.findById(100L)).thenReturn(Optional.of(solicitud));
        when(mapper.toDTO(solicitud)).thenReturn(responseDTO);

        SolicitudResponseDTO result = solicitudService.obtenerPorId(100L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("obtenerPorId(): lanza excepción si no existe solicitud")
    void obtenerPorId_NoEncontrado_ThrowsException() {
        when(solicitudRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> solicitudService.obtenerPorId(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Solicitud no encontrada");
    }

    // ------------------------- registrar() -------------------------

    @Test
    @DisplayName("registrar(): crea solicitud correctamente")
    void registrar_Success() {
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(alojamientoRepository.findById(10L)).thenReturn(Optional.of(alojamiento));
        when(mapper.toEntity(requestDTO, estudiante, alojamiento)).thenReturn(solicitud);
        when(solicitudRepository.save(solicitud)).thenReturn(solicitud);
        when(mapper.toDTO(solicitud)).thenReturn(responseDTO);

        SolicitudResponseDTO result = solicitudService.registrar(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getEstado()).isEqualTo("PENDIENTE");
        verify(solicitudRepository).save(solicitud);
    }

    @Test
    @DisplayName("registrar(): lanza excepción si estudiante no existe")
    void registrar_EstudianteNoEncontrado_ThrowsException() {
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> solicitudService.registrar(requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Estudiante no encontrado");
    }

    @Test
    @DisplayName("registrar(): lanza excepción si alojamiento no existe")
    void registrar_AlojamientoNoEncontrado_ThrowsException() {
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(alojamientoRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> solicitudService.registrar(requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Alojamiento no encontrado");
    }

    // ------------------------- actualizarEstado() -------------------------

    @Test
    @DisplayName("actualizarEstado(): cambia estado correctamente")
    void actualizarEstado_Success() {
        when(solicitudRepository.findById(100L)).thenReturn(Optional.of(solicitud));
        when(mapper.toDTO(solicitud)).thenReturn(responseDTO);

        SolicitudResponseDTO result = solicitudService.actualizarEstado(100L, "CANCELADA");

        assertThat(result).isNotNull();
        assertThat(solicitud.getEstado()).isEqualTo("CANCELADA");
        verify(solicitudRepository).save(solicitud);
    }

    @Test
    @DisplayName("actualizarEstado(): lanza excepción si solicitud no existe")
    void actualizarEstado_NoEncontrado_ThrowsException() {
        when(solicitudRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> solicitudService.actualizarEstado(100L, "ACEPTADA"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Solicitud no encontrada");
    }

    // ------------------------- actualizar() -------------------------

    @Test
    @DisplayName("actualizar(): modifica solicitud correctamente")
    void actualizar_Success() {
        when(solicitudRepository.findById(100L)).thenReturn(Optional.of(solicitud));
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(alojamientoRepository.findById(10L)).thenReturn(Optional.of(alojamiento));
        when(solicitudRepository.save(any(Solicitudes.class))).thenReturn(solicitud);
        when(mapper.toDTO(solicitud)).thenReturn(responseDTO);

        SolicitudResponseDTO result = solicitudService.actualizar(100L, requestDTO);

        assertThat(result).isNotNull();
        verify(solicitudRepository).save(solicitud);
    }

    @Test
    @DisplayName("actualizar(): lanza excepción si solicitud no existe")
    void actualizar_SolicitudNoEncontrada_ThrowsException() {
        when(solicitudRepository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> solicitudService.actualizar(100L, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Solicitud no encontrada");
    }

    @Test
    @DisplayName("actualizar(): lanza excepción si estudiante no existe")
    void actualizar_EstudianteNoEncontrado_ThrowsException() {
        when(solicitudRepository.findById(100L)).thenReturn(Optional.of(solicitud));
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> solicitudService.actualizar(100L, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Estudiante no encontrado");
    }

    @Test
    @DisplayName("actualizar(): lanza excepción si alojamiento no existe")
    void actualizar_AlojamientoNoEncontrado_ThrowsException() {
        when(solicitudRepository.findById(100L)).thenReturn(Optional.of(solicitud));
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(alojamientoRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> solicitudService.actualizar(100L, requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Alojamiento no encontrado");
    }

    // ------------------------- obtenerPorEstudianteId() -------------------------

    @Test
    @DisplayName("obtenerPorEstudianteId(): retorna lista correctamente")
    void obtenerPorEstudianteId_Success() {
        when(solicitudRepository.findByEstudiantes_Id(1L)).thenReturn(List.of(solicitud));
        when(mapper.toDTO(solicitud)).thenReturn(responseDTO);

        List<SolicitudResponseDTO> result = solicitudService.obtenerPorEstudianteId(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("obtenerPorEstudianteId(): lanza excepción si no hay solicitudes")
    void obtenerPorEstudianteId_Empty_ThrowsException() {
        when(solicitudRepository.findByEstudiantes_Id(1L)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> solicitudService.obtenerPorEstudianteId(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No se encontraron solicitudes");
    }

    // ------------------------- obtenerPorPropietarioId() -------------------------

    @Test
    @DisplayName("obtenerPorPropietarioId(): retorna lista correctamente")
    void obtenerPorPropietarioId_Success() {
        when(solicitudRepository.findByPropietarioId(5L)).thenReturn(List.of(solicitud));
        when(mapper.toDTO(solicitud)).thenReturn(responseDTO);

        List<SolicitudResponseDTO> result = solicitudService.obtenerPorPropietarioId(5L);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("obtenerPorPropietarioId(): lanza excepción si no hay solicitudes")
    void obtenerPorPropietarioId_Empty_ThrowsException() {
        when(solicitudRepository.findByPropietarioId(5L)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> solicitudService.obtenerPorPropietarioId(5L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("No se encontraron solicitudes");
    }

    // ------------------------- eliminar() -------------------------

    @Test
    @DisplayName("eliminar(): elimina solicitud correctamente")
    void eliminar_Success() {
        when(solicitudRepository.existsById(100L)).thenReturn(true);

        solicitudService.eliminar(100L);

        verify(solicitudRepository).deleteById(100L);
    }

    @Test
    @DisplayName("eliminar(): lanza excepción si no existe solicitud")
    void eliminar_NoExiste_ThrowsException() {
        when(solicitudRepository.existsById(100L)).thenReturn(false);

        assertThatThrownBy(() -> solicitudService.eliminar(100L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Solicitud no encontrada");
    }
}
