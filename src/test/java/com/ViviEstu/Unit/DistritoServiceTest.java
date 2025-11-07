package com.ViviEstu.Unit;

import com.ViviEstu.exception.NoDataFoundException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.DistritoMapper;
import com.ViviEstu.model.dto.request.DistritoRequestDTO;
import com.ViviEstu.model.dto.response.DistritoResponseDTO;
import com.ViviEstu.model.entity.Distrito;
import com.ViviEstu.repository.DistritoRepository;
import com.ViviEstu.service.DistritoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DistritoServiceTest {
    @Mock
    DistritoRepository distritoRepository;

    @Mock
    private DistritoMapper distritoMapper;

    @InjectMocks
    private DistritoService distritoService;

    @Test
    @DisplayName("createDistrito - valid request should return created distrito")
    void createDistrito_validRequest_shouldReturnCreatedDistrito() {
        // Arrange
        DistritoRequestDTO requestDTO = new DistritoRequestDTO("San Borja", "Distrito residencial", 1800, "Departamento", "url", 5);
        Distrito distrito = new Distrito(1L, "San Borja", "Distrito residencial", 1800, "Departamento", "url", 5, LocalDate.now());
        DistritoResponseDTO expectedResponse = new DistritoResponseDTO(1L, "San Borja", "Distrito residencial", 1800, "Departamento", "url", 5, LocalDate.now());

        when(distritoMapper.convertToEntity(any(DistritoRequestDTO.class))).thenReturn(distrito);
        when(distritoRepository.save(any(Distrito.class))).thenReturn(distrito);
        when(distritoMapper.convertToDTO(any(Distrito.class))).thenReturn(expectedResponse);

        // Act
        DistritoResponseDTO actualResponse = distritoService.createDistrito(requestDTO);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getNombre(), actualResponse.getNombre());
        verify(distritoRepository, times(1)).save(distrito);
    }

    @Test
    @DisplayName("listAll - when distritos exist should return list of distritos")
    void listAll_whenDistritosExist_shouldReturnListOfDistritos() {
        // Arrange
        Distrito distrito = new Distrito(1L, "Miraflores", "Distrito turístico", 1500, "Departamento", "url", 4, LocalDate.now());
        List<Distrito> distritos = List.of(distrito);
        DistritoResponseDTO responseDTO = new DistritoResponseDTO(1L, "Miraflores", "Distrito turístico", 1500, "Departamento", "url", 4, LocalDate.now());
        List<DistritoResponseDTO> expectedResponse = List.of(responseDTO);

        when(distritoRepository.findAll()).thenReturn(distritos);
        when(distritoMapper.convertListToDTO(distritos)).thenReturn(expectedResponse);

        // Act
        List<DistritoResponseDTO> actualResponse = distritoService.listAll();

        // Assert
        assertFalse(actualResponse.isEmpty());
        assertEquals(1, actualResponse.size());
    }

    @Test
    @DisplayName("listAll - when no distritos exist should return empty list")
    void listAll_whenNoDistritosExist_shouldReturnEmptyList() {
        // Arrange
        when(distritoRepository.findAll()).thenReturn(Collections.emptyList());
        when(distritoMapper.convertListToDTO(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Act
        List<DistritoResponseDTO> actualResponse = distritoService.listAll();

        // Assert
        assertTrue(actualResponse.isEmpty());
    }

    @Test
    @DisplayName("getDistritoById - existing ID should return Distrito info")
    void getDistritoById_existingId_shouldReturnDistritoInfo() {
        // Arrange
        Long distritoId = 1L;
        LocalDate date = LocalDate.now();
        Distrito distrito = new Distrito(distritoId, "Miraflores", "Distrito turístico", 1500, "Departamento", "url", 4, date);
        DistritoResponseDTO expectedResponse = new DistritoResponseDTO(distritoId, "Miraflores", "Distrito turístico", 1500, "Departamento", "url", 4, date);

        when(distritoRepository.findById(distritoId)).thenReturn(Optional.of(distrito));
        when(distritoMapper.convertToDTO(any(Distrito.class))).thenReturn(expectedResponse);

        // Act
        DistritoResponseDTO actualResponse = distritoService.getDistritoById(distritoId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getNombre(), actualResponse.getNombre());
        assertEquals(expectedResponse.getDescripcion(), actualResponse.getDescripcion());
        assertEquals(expectedResponse.getPrecioProm(), actualResponse.getPrecioProm());
        assertEquals(expectedResponse.getTipo(), actualResponse.getTipo());
        assertEquals(expectedResponse.getSeguridad(), actualResponse.getSeguridad());
    }

    @Test
    @DisplayName("getDistritoById - outdated info should return info with last update date")
    void getDistritoById_outdatedInfo_shouldReturnInfoWithLastUpdateDate() {
        // Arrange
        Long distritoId = 2L;
        LocalDate outdatedDate = LocalDate.of(2023, 1, 1);
        Distrito distrito = new Distrito(distritoId, "Lince", "Distrito céntrico", 1200, "Departamento", "url", 3, outdatedDate);
        DistritoResponseDTO expectedResponse = new DistritoResponseDTO(distritoId, "Lince", "Distrito céntrico", 1200, "Departamento", "url", 3, outdatedDate);

        when(distritoRepository.findById(distritoId)).thenReturn(Optional.of(distrito));
        when(distritoMapper.convertToDTO(any(Distrito.class))).thenReturn(expectedResponse);

        // Act
        DistritoResponseDTO actualResponse = distritoService.getDistritoById(distritoId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertEquals(expectedResponse.getNombre(), actualResponse.getNombre());
        assertEquals(expectedResponse.getDescripcion(), actualResponse.getDescripcion());
        assertEquals(expectedResponse.getPrecioProm(), actualResponse.getPrecioProm());
        assertEquals(expectedResponse.getTipo(), actualResponse.getTipo());
        assertEquals(expectedResponse.getSeguridad(), actualResponse.getSeguridad());
        assertEquals(outdatedDate, actualResponse.getLastUpdate());
    }

    @Test
    @DisplayName("getDistritoById - no data should throw NoDataFoundException")
    void getDistritoById_noData_shouldThrowNoDataFoundException() {
        // Arrange
        Long distritoId = 3L;
        Distrito distritoSinDatos = new Distrito(distritoId, "Ancon", null, null, "Distrito", "url", 2, null);

        when(distritoRepository.findById(distritoId)).thenReturn(Optional.of(distritoSinDatos));

        // Act & Assert
        NoDataFoundException exception = assertThrows(NoDataFoundException.class, () -> {
            distritoService.getDistritoById(distritoId);
        });

        assertEquals("Aún no tenemos información disponible para este distrito", exception.getMessage());
    }

    @Test
    @DisplayName("getDistritoById - non-existing ID should throw ResourceNotFoundException")
    void getDistritoById_nonExistingId_shouldThrowResourceNotFoundException() {
        // Arrange
        Long distritoId = 99L;
        when(distritoRepository.findById(distritoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            distritoService.getDistritoById(distritoId);
        });
    }

    @Test
    @DisplayName("updateDistrito - existing id should update and return distrito")
    void updateDistrito_existingId_shouldUpdateAndReturnDistrito() {
        // Arrange
        Long distritoId = 1L;
        DistritoRequestDTO requestDTO = new DistritoRequestDTO("Miraflores Updated", "Desc Updated", 2000, "Casa", "new_url", 5);
        Distrito existingDistrito = new Distrito(distritoId, "Miraflores", "Desc", 1500, "Departamento", "url", 4, LocalDate.now());
        DistritoResponseDTO expectedResponse = new DistritoResponseDTO(distritoId, "Miraflores Updated", "Desc Updated", 2000, "Casa", "new_url", 5, LocalDate.now());

        when(distritoRepository.findById(distritoId)).thenReturn(Optional.of(existingDistrito));
        when(distritoRepository.save(any(Distrito.class))).thenReturn(existingDistrito);
        when(distritoMapper.convertToDTO(existingDistrito)).thenReturn(expectedResponse);
        doNothing().when(distritoMapper).updateEntityFromDTO(requestDTO, existingDistrito);

        // Act
        DistritoResponseDTO actualResponse = distritoService.updateDistrito(distritoId, requestDTO);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getNombre(), actualResponse.getNombre());
        verify(distritoRepository, times(1)).findById(distritoId);
        verify(distritoRepository, times(1)).save(existingDistrito);
        verify(distritoMapper, times(1)).updateEntityFromDTO(requestDTO, existingDistrito);
    }

    @Test
    @DisplayName("updateDistrito - non-existing id should throw ResourceNotFoundException")
    void updateDistrito_nonExistingId_shouldThrowResourceNotFoundException() {
        // Arrange
        Long distritoId = 99L;
        DistritoRequestDTO requestDTO = new DistritoRequestDTO();
        when(distritoRepository.findById(distritoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            distritoService.updateDistrito(distritoId, requestDTO);
        });
        verify(distritoRepository, never()).save(any());
    }

    @Test
    @DisplayName("deleteDistrito - existing id should delete distrito")
    void deleteDistrito_existingId_shouldDeleteDistrito() {
        // Arrange
        Long distritoId = 1L;
        Distrito distrito = new Distrito();
        when(distritoRepository.findById(distritoId)).thenReturn(Optional.of(distrito));
        doNothing().when(distritoRepository).delete(distrito);

        // Act
        distritoService.deleteDistrito(distritoId);

        // Assert
        verify(distritoRepository, times(1)).findById(distritoId);
        verify(distritoRepository, times(1)).delete(distrito);
    }

    @Test
    @DisplayName("deleteDistrito - non-existing id should throw ResourceNotFoundException")
    void deleteDistrito_nonExistingId_shouldThrowResourceNotFoundException() {
        // Arrange
        Long distritoId = 99L;
        when(distritoRepository.findById(distritoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            distritoService.deleteDistrito(distritoId);
        });
        verify(distritoRepository, never()).delete(any());
    }

    @Test
    @DisplayName("searchByNombre - valid name should return matching distritos")
    void searchByNombre_validName_shouldReturnMatchingDistritos() {
        // Arrange
        String nombre = "Mira";
        List<Distrito> distritos = List.of(new Distrito());
        List<DistritoResponseDTO> expectedResponse = List.of(new DistritoResponseDTO());
        when(distritoRepository.findByNombreContainingIgnoreCase(nombre)).thenReturn(distritos);
        when(distritoMapper.convertListToDTO(distritos)).thenReturn(expectedResponse);

        // Act
        List<DistritoResponseDTO> actualResponse = distritoService.searchByNombre(nombre);

        // Assert
        assertFalse(actualResponse.isEmpty());
        verify(distritoRepository, times(1)).findByNombreContainingIgnoreCase(nombre);
    }

    @Test
    @DisplayName("searchByNombre - null name should throw IllegalArgumentException")
    void searchByNombre_nullName_shouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            distritoService.searchByNombre(null);
        });
        assertEquals("El nombre no debe ser nulo o vacío.", exception.getMessage());
    }

    @Test
    @DisplayName("searchByNombre - empty name should throw IllegalArgumentException")
    void searchByNombre_emptyName_shouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            distritoService.searchByNombre("");
        });
        assertEquals("El nombre no debe ser nulo o vacío.", exception.getMessage());
    }

    @Test
    @DisplayName("filterByPrecio - valid range should return matching distritos")
    void filterByPrecio_validRange_shouldReturnMatchingDistritos() {
        // Arrange
        Integer min = 1000;
        Integer max = 2000;
        List<Distrito> distritos = List.of(new Distrito());
        List<DistritoResponseDTO> expectedResponse = List.of(new DistritoResponseDTO());
        when(distritoRepository.findByPrecioPromBetween(min, max)).thenReturn(distritos);
        when(distritoMapper.convertListToDTO(distritos)).thenReturn(expectedResponse);

        // Act
        List<DistritoResponseDTO> actualResponse = distritoService.filterByPrecio(min, max);

        // Assert
        assertFalse(actualResponse.isEmpty());
        verify(distritoRepository, times(1)).findByPrecioPromBetween(min, max);
    }

    @Test
    @DisplayName("filterByPrecio - null prices should throw IllegalArgumentException")
    void filterByPrecio_nullPrices_shouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            distritoService.filterByPrecio(null, null);
        });
        assertEquals("Los precios no deben ser nulos.", exception.getMessage());
    }

    @Test
    @DisplayName("filterByPrecio - min price greater than max price should throw IllegalArgumentException")
    void filterByPrecio_minPriceGreaterThanMaxPrice_shouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            distritoService.filterByPrecio(1500, 1200);
        });
        assertEquals("El precio mínimo no debe ser mayor que el precio máximo.", exception.getMessage());
    }

    @Test
    @DisplayName("filterByTipo - valid type should return matching distritos")
    void filterByTipo_validType_shouldReturnMatchingDistritos() {
        // Arrange
        String tipo = "Departamento";
        List<Distrito> distritos = List.of(new Distrito());
        List<DistritoResponseDTO> expectedResponse = List.of(new DistritoResponseDTO());
        when(distritoRepository.findByTipo(tipo)).thenReturn(distritos);
        when(distritoMapper.convertListToDTO(distritos)).thenReturn(expectedResponse);

        // Act
        List<DistritoResponseDTO> actualResponse = distritoService.filterByTipo(tipo);

        // Assert
        assertFalse(actualResponse.isEmpty());
        verify(distritoRepository, times(1)).findByTipo(tipo);
    }

    @Test
    @DisplayName("filterByTipo - null type should throw IllegalArgumentException")
    void filterByTipo_nullType_shouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            distritoService.filterByTipo(null);
        });
        assertEquals("El tipo no debe ser nulo o vacío.", exception.getMessage());
    }

    @Test
    @DisplayName("filterByTipo - empty type should throw IllegalArgumentException")
    void filterByTipo_emptyType_shouldThrowIllegalArgumentException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            distritoService.filterByTipo("");
        });
        assertEquals("El tipo no debe ser nulo o vacío.", exception.getMessage());
    }
}
