package com.ViviEstu.Unit;

import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.UniversidadMapper;
import com.ViviEstu.model.dto.request.UniversidadRequestDTO;
import com.ViviEstu.model.dto.response.UniversidadResponseDTO;
import com.ViviEstu.model.entity.Distrito;
import com.ViviEstu.model.entity.Universidad;
import com.ViviEstu.repository.DistritoRepository;
import com.ViviEstu.repository.UniversidadRepository;
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
class UniversidadServiceTest {

    @Mock
    private UniversidadRepository universidadRepository;

    @Mock
    private DistritoRepository distritoRepository;

    @InjectMocks
    private UniversidadService universidadService;

    private Universidad universidad;
    private Distrito distrito;
    private UniversidadRequestDTO requestDTO;
    private UniversidadResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        distrito = new Distrito();
        distrito.setId(1L);
        distrito.setNombre("Lima Centro");

        universidad = new Universidad();
        universidad.setId(10L);
        universidad.setNombre("Universidad Nacional de Ingeniería");
        universidad.setDistrito(distrito);

        requestDTO = new UniversidadRequestDTO();
        requestDTO.setNombre("Universidad Nacional de Ingeniería");
        requestDTO.setDistritoId(1L);

        responseDTO = new UniversidadResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setNombre("Universidad Nacional de Ingeniería");
        responseDTO.setDistritoNombre("Lima Centro");
    }

    // ------------------------- crear() -------------------------

    @Test
    @DisplayName("crear(): crea universidad correctamente")
    void crear_Success() {
        when(distritoRepository.findById(1L)).thenReturn(Optional.of(distrito));
        when(universidadRepository.save(any(Universidad.class))).thenReturn(universidad);

        UniversidadResponseDTO result = universidadService.crear(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Universidad Nacional de Ingeniería");
        verify(universidadRepository).save(any(Universidad.class));
    }

    @Test
    @DisplayName("crear(): lanza excepción si el distrito no existe")
    void crear_DistritoNoEncontrado_ThrowsException() {
        when(distritoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> universidadService.crear(requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Distrito no encontrado");
    }

    // ------------------------- listar() -------------------------

    @Test
    @DisplayName("listar(): retorna lista correctamente")
    void listar_Success() {
        when(universidadRepository.findAll()).thenReturn(List.of(universidad));

        List<UniversidadResponseDTO> result = universidadService.listar();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("Universidad Nacional de Ingeniería");
        verify(universidadRepository).findAll();
    }

    @Test
    @DisplayName("listar(): lista vacía retorna lista vacía sin excepción")
    void listar_Empty_ReturnsEmptyList() {
        when(universidadRepository.findAll()).thenReturn(Collections.emptyList());

        List<UniversidadResponseDTO> result = universidadService.listar();

        assertThat(result).isEmpty();
        verify(universidadRepository).findAll();
    }

    // ------------------------- obtenerPorId() -------------------------

    @Test
    @DisplayName("obtenerPorId(): retorna universidad correctamente")
    void obtenerPorId_Success() {
        when(universidadRepository.findById(10L)).thenReturn(Optional.of(universidad));

        UniversidadResponseDTO result = universidadService.obtenerPorId(10L);

        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("Universidad Nacional de Ingeniería");
    }

    @Test
    @DisplayName("obtenerPorId(): lanza excepción si no se encuentra universidad")
    void obtenerPorId_NoEncontrado_ThrowsException() {
        when(universidadRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> universidadService.obtenerPorId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Universidad no encontrada");
    }

    // ------------------------- buscarPorNombre() -------------------------

    @Test
    @DisplayName("buscarPorNombre(): retorna lista de universidades que coinciden")
    void buscarPorNombre_Success() {
        when(universidadRepository.findByNombreContainingIgnoreCase("ingeniería"))
                .thenReturn(List.of(universidad));

        List<UniversidadResponseDTO> result = universidadService.buscarPorNombre("ingeniería");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).containsIgnoringCase("ingeniería");
        verify(universidadRepository).findByNombreContainingIgnoreCase("ingeniería");
    }

    @Test
    @DisplayName("buscarPorNombre(): retorna lista vacía si no hay coincidencias")
    void buscarPorNombre_Empty_ReturnsEmpty() {
        when(universidadRepository.findByNombreContainingIgnoreCase("xyz"))
                .thenReturn(Collections.emptyList());

        List<UniversidadResponseDTO> result = universidadService.buscarPorNombre("xyz");

        assertThat(result).isEmpty();
    }

    // ------------------------- actualizar() -------------------------

    @Test
    @DisplayName("actualizar(): modifica universidad correctamente sin cambiar distrito")
    void actualizar_Success_SinCambioDistrito() {
        when(universidadRepository.findById(10L)).thenReturn(Optional.of(universidad));
        when(universidadRepository.save(any(Universidad.class))).thenReturn(universidad);

        UniversidadResponseDTO result = universidadService.actualizar(10L, requestDTO);

        assertThat(result).isNotNull();
        verify(universidadRepository).save(universidad);
        verifyNoInteractions(distritoRepository); // no consulta distrito si no cambia
    }

    @Test
    @DisplayName("actualizar(): modifica universidad cambiando distrito correctamente")
    void actualizar_Success_CambioDistrito() {
        Distrito nuevoDistrito = new Distrito();
        nuevoDistrito.setId(2L);
        nuevoDistrito.setNombre("San Isidro");

        requestDTO.setDistritoId(2L);

        when(universidadRepository.findById(10L)).thenReturn(Optional.of(universidad));
        when(distritoRepository.findById(2L)).thenReturn(Optional.of(nuevoDistrito));
        when(universidadRepository.save(any(Universidad.class))).thenReturn(universidad);

        UniversidadResponseDTO result = universidadService.actualizar(10L, requestDTO);

        assertThat(result).isNotNull();
        verify(distritoRepository).findById(2L);
        verify(universidadRepository).save(universidad);
    }

    @Test
    @DisplayName("actualizar(): lanza excepción si universidad no existe")
    void actualizar_UniversidadNoEncontrada_ThrowsException() {
        when(universidadRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> universidadService.actualizar(10L, requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Universidad no encontrada");
    }

    @Test
    @DisplayName("actualizar(): lanza excepción si nuevo distrito no existe")
    void actualizar_DistritoNoEncontrado_ThrowsException() {
        requestDTO.setDistritoId(2L);
        when(universidadRepository.findById(10L)).thenReturn(Optional.of(universidad));
        when(distritoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> universidadService.actualizar(10L, requestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Distrito no encontrado");
    }

    // ------------------------- eliminar() -------------------------

    @Test
    @DisplayName("eliminar(): elimina universidad correctamente")
    void eliminar_Success() {
        when(universidadRepository.existsById(10L)).thenReturn(true);

        universidadService.eliminar(10L);

        verify(universidadRepository).deleteById(10L);
    }

    @Test
    @DisplayName("eliminar(): lanza excepción si universidad no existe")
    void eliminar_NoExiste_ThrowsException() {
        when(universidadRepository.existsById(10L)).thenReturn(false);

        assertThatThrownBy(() -> universidadService.eliminar(10L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Universidad no encontrada");
    }
}
