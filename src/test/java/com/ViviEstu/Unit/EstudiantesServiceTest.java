package com.ViviEstu.Unit;

import com.ViviEstu.exception.DuplicateResourceException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.EstudianteMapper;
import com.ViviEstu.model.dto.request.EstudiantesRequestDTO;
import com.ViviEstu.model.dto.response.EstudianteResponseDTO;
import com.ViviEstu.model.entity.*;
import com.ViviEstu.repository.*;
import com.ViviEstu.service.EstudiantesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EstudiantesServiceTest {
    @Mock
    private EstudiantesRepository estudiantesRepository;
    @Mock private DistritoRepository distritoRepository;
    @Mock private UniversidadRepository universidadRepository;
    @Mock private DatosUniversitariosRepository datosUniversitariosRepository;
    @Mock private EstudianteMapper estudiantesMapper;
    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;

    @InjectMocks
    private EstudiantesService estudiantesService;

    private Estudiantes estudiante;
    private EstudiantesRequestDTO requestDTO;
    private EstudianteResponseDTO responseDTO;
    private Distrito distrito;
    private Universidad universidad;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        distrito = new Distrito();
        distrito.setId(1L);
        distrito.setNombre("Lima");

        universidad = new Universidad();
        universidad.setId(1L);
        universidad.setNombre("PUCP");

        role = new Role();
        role.setId(1L);
        role.setName(RoleType.ROLE_ESTUDIANTE);

        user = new User();
        user.setId(1L);
        user.setCorreo("test@pucp.edu.pe");
        user.setContrasenia("1234");
        user.setRole(role);
        user.setActive(true);

        estudiante = new Estudiantes();
        estudiante.setId(1L);
        estudiante.setNombre("Juan");
        estudiante.setApellidos("Pérez");
        estudiante.setDni("12345678");
        estudiante.setTelefono("999999999");
        estudiante.setCarrera("Ingeniería");
        estudiante.setCiclo(5);
        estudiante.setDistrito(distrito);
        estudiante.setUniversidad(universidad);
        estudiante.setUser(user);

        requestDTO = new EstudiantesRequestDTO();
        requestDTO.setNombre("Juan");
        requestDTO.setApellidos("Pérez");
        requestDTO.setDni("12345678");
        requestDTO.setTelefono("999999999");
        requestDTO.setCarrera("Ingeniería");
        requestDTO.setCiclo(5);
        requestDTO.setCorreo("test@pucp.edu.pe");
        requestDTO.setContrasenia("1234");
        requestDTO.setDistritoId(1L);
        requestDTO.setUniversidadId(1L);

        responseDTO = new EstudianteResponseDTO();
        responseDTO.setNombre("Juan");
        responseDTO.setApellidos("Pérez");
        responseDTO.setCorreo("test@pucp.edu.pe");
        responseDTO.setDistrito("Lima");
        responseDTO.setUniversidad("PUCP");
    }

    // -------------------------------------------------------------------------
    @Test
    void testGetAllEstudiantes_Exitoso() {
        // Arrange
        when(estudiantesRepository.findAll()).thenReturn(List.of(estudiante));
        when(estudiantesMapper.convertToDTO(any(Estudiantes.class))).thenReturn(responseDTO);

        // Act
        List<EstudianteResponseDTO> result = estudiantesService.getAllEstudiantes();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getNombre());
        verify(estudiantesRepository).findAll();
        verify(estudiantesMapper).convertToDTO(any(Estudiantes.class));
    }

    // -------------------------------------------------------------------------
    @Test
    void testGetEstudianteById_Exitoso() {
        // Arrange
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(estudiantesMapper.convertToDTO(estudiante)).thenReturn(responseDTO);

        // Act
        EstudianteResponseDTO result = estudiantesService.getEstudianteById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("Lima", result.getDistrito());
        verify(estudiantesRepository).findById(1L);
        verify(estudiantesMapper).convertToDTO(estudiante);
    }

    @Test
    void testGetEstudianteById_NoEncontrado() {
        // Arrange
        when(estudiantesRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> estudiantesService.getEstudianteById(99L));

        verify(estudiantesRepository).findById(99L);
    }

    // -------------------------------------------------------------------------
    @Test
    void testCreateEstudiante_Exitoso() {
        // Arrange
        when(datosUniversitariosRepository.existsByCorreoInstitucional("test@pucp.edu.pe")).thenReturn(true);
        when(estudiantesRepository.existsByNombreAndApellidos("Juan", "Pérez")).thenReturn(false);
        when(distritoRepository.findById(1L)).thenReturn(Optional.of(distrito));
        when(universidadRepository.findById(1L)).thenReturn(Optional.of(universidad));
        when(roleRepository.findByName(RoleType.ROLE_ESTUDIANTE)).thenReturn(Optional.of(role));
        when(estudiantesMapper.convertToDTO(any(Estudiantes.class))).thenReturn(responseDTO);

        // Act
        EstudianteResponseDTO result = estudiantesService.createEstudiante(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("PUCP", result.getUniversidad());
        verify(userRepository).save(any(User.class));
        verify(estudiantesRepository).save(any(Estudiantes.class));
        verify(estudiantesMapper).convertToDTO(any(Estudiantes.class));
    }

    @Test
    void testCreateEstudiante_CorreoNoEncontrado() {
        // Arrange
        when(datosUniversitariosRepository.existsByCorreoInstitucional(anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> estudiantesService.createEstudiante(requestDTO));

        verify(datosUniversitariosRepository).existsByCorreoInstitucional(anyString());
    }

    @Test
    void testCreateEstudiante_Duplicado() {
        // Arrange
        when(datosUniversitariosRepository.existsByCorreoInstitucional(anyString())).thenReturn(true);
        when(estudiantesRepository.existsByNombreAndApellidos(anyString(), anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class,
                () -> estudiantesService.createEstudiante(requestDTO));

        verify(estudiantesRepository).existsByNombreAndApellidos(anyString(), anyString());
    }

    // -------------------------------------------------------------------------
    @Test
    void testUpdateEstudiante_Exitoso() {
        // Arrange
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(estudiantesMapper.convertToDTO(any(Estudiantes.class))).thenReturn(responseDTO);

        // Act
        EstudianteResponseDTO result = estudiantesService.updateEstudiante(1L, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        verify(userRepository).save(any(User.class));
        verify(estudiantesRepository).save(any(Estudiantes.class));
    }

    @Test
    void testUpdateEstudiante_NoEncontrado() {
        // Arrange
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> estudiantesService.updateEstudiante(1L, requestDTO));

        verify(estudiantesRepository).findById(1L);
    }

    // -------------------------------------------------------------------------
    @Test
    void testDeleteEstudiante_Exitoso() {
        // Arrange
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));

        // Act
        estudiantesService.deleteEstudiante(1L);

        // Assert
        verify(userRepository).delete(any(User.class));
        verify(estudiantesRepository).deleteById(1L);
    }

    @Test
    void testDeleteEstudiante_NoEncontrado() {
        // Arrange
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> estudiantesService.deleteEstudiante(1L));

        verify(estudiantesRepository).findById(1L);
    }
}
