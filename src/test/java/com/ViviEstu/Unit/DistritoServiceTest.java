package com.ViviEstu.Unit;

import com.ViviEstu.exception.NoDataFoundException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.DistritoMapper;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DistritoServiceTest {
    @Mock
    DistritoRepository distritoRepository;

    @Mock
    private DistritoMapper distritoMapper;

    @InjectMocks
    private DistritoService distritoService;

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


}
