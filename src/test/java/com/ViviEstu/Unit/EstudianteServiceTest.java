package com.ViviEstu.Unit;

import com.ViviEstu.exception.BadRequestException;
import com.ViviEstu.exception.DuplicateResourceException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.EstudianteMapper;
import com.ViviEstu.model.dto.request.EstudiantesRequestDTO;
import com.ViviEstu.model.dto.response.EstudianteResponseDTO;
import com.ViviEstu.model.entity.*;
import com.ViviEstu.repository.*;
import com.ViviEstu.service.EstudiantesService;
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
public class EstudianteServiceTest {
    @Mock
    private EstudiantesRepository estudiantesRepository;

    @Mock
    private DistritoRepository distritoRepository;

    @Mock
    private UniversidadRepository universidadRepository;

    @Mock
    private DatosUniversitariosRepository datosUniversitariosRepository;

    @Mock
    private EstudianteMapper estudiantesMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private EstudiantesService estudiantesService;

    EstudiantesRequestDTO estudianteRequestDTO;
    Distrito distrito;
    Universidad universidad;
    User user;
    Role role;
    Estudiantes estudiante;
    EstudianteResponseDTO estudianteResponseDTO;


    @BeforeEach
    void setUp() {
        // DTO de prueba
        estudianteRequestDTO = new EstudiantesRequestDTO();
        estudianteRequestDTO.setNombre("David");
        estudianteRequestDTO.setApellidos("Serrudo");
        estudianteRequestDTO.setDni("12345678");
        estudianteRequestDTO.setTelefono("987654321");
        estudianteRequestDTO.setCarrera("Ingeniería de Software");
        estudianteRequestDTO.setCorreo("david@upc.edu.pe");
        estudianteRequestDTO.setContrasenia("password123");
        estudianteRequestDTO.setDistritoId(1L);
        estudianteRequestDTO.setUniversidadId(1L);

        // Entidades simuladas
        distrito = new Distrito();
        distrito.setId(1L);
        distrito.setNombre("Miraflores");

        universidad = new Universidad();
        universidad.setId(1L);
        universidad.setNombre("UPC");

        role = new Role();
        role.setId(1L);
        role.setName(RoleType.ROLE_ESTUDIANTE);


        user = new User();
        user.setRole(role);
        user.setId(1L);
        user.setCorreo("david@upc.edu.pe");
        user.setContrasenia("password123");

        estudiante = new Estudiantes();
        estudiante.setId(1L);
        estudiante.setNombre("David");
        estudiante.setApellidos("Serrudo");
        estudiante.setDni("12345678");
        estudiante.setTelefono("987654321");
        estudiante.setCarrera("Ingeniería de Software");
        estudiante.setDistrito(distrito);
        estudiante.setUniversidad(universidad);
        estudiante.setUser(user);

        estudianteResponseDTO = new EstudianteResponseDTO();
        estudianteResponseDTO.setId(1L);
        estudianteResponseDTO.setNombre("David");
        estudianteResponseDTO.setApellidos("Serrudo");
        estudianteResponseDTO.setCorreo("david@upc.edu.pe");
        estudianteResponseDTO.setDistrito("Miraflores");
        estudianteResponseDTO.setUniversidad("UPC");
    }

    @Test
    @DisplayName("Debe actualizar el perfil del estudiante exitosamente cuando todos los campos son válidos")
    void updateEstudiante_PerfilActualizado_Success() {
        // Arrange
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(estudiantesRepository.save(any(Estudiantes.class))).thenReturn(estudiante);
        when(estudiantesMapper.convertToDTO(any(Estudiantes.class))).thenReturn(estudianteResponseDTO);

        // Act
        EstudianteResponseDTO result = estudiantesService.updateEstudiante(1L, estudianteRequestDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getNombre()).isEqualTo("David");
        assertThat(result.getCorreo()).isEqualTo("david@upc.edu.pe");
        verify(userRepository, times(1)).save(any(User.class));
        verify(estudiantesRepository, times(1)).save(any(Estudiantes.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el formulario está vacío")
    void updateEstudiante_FormularioVacio_ThrowsException() {
        // Arrange
        EstudiantesRequestDTO emptyDto = new EstudiantesRequestDTO();

        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));

        // Act & Assert
        assertThatThrownBy(() -> estudiantesService.updateEstudiante(1L, emptyDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Campos obligatorios vacíos");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando faltan campos obligatorios")
    void updateEstudiante_FormularioIncompleto_ThrowsException() {
        // Arrange
        EstudiantesRequestDTO incompleteDto = new EstudiantesRequestDTO();
        incompleteDto.setNombre("David"); // Falta correo, teléfono, etc.

        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));

        // Act & Assert
        assertThatThrownBy(() -> estudiantesService.updateEstudiante(1L, incompleteDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Campos obligatorios vacíos");
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar actualizar un estudiante inexistente")
    void updateEstudiante_NoEncontrado_ThrowsException() {
        // Arrange
        EstudiantesRequestDTO request = new EstudiantesRequestDTO();
        request.setNombre("Carlos");

        when(estudiantesRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> estudiantesService.updateEstudiante(99L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Estudiante no encontrado con id: 99");

        verify(estudiantesRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe crear un estudiante exitosamente cuando todos los datos son válidos")
    void createEstudiante_Success() {
        // Arrange
        when(datosUniversitariosRepository.existsByCorreoInstitucional(estudianteRequestDTO.getCorreo())).thenReturn(true);
        when(estudiantesRepository.existsByNombreAndApellidos("David", "Serrudo")).thenReturn(false);
        when(distritoRepository.findById(1L)).thenReturn(Optional.of(distrito));
        when(universidadRepository.findById(1L)).thenReturn(Optional.of(universidad));
        when(roleRepository.findByName(RoleType.ROLE_ESTUDIANTE)).thenReturn(Optional.of(role));
        when(estudiantesMapper.convertToDTO(any(Estudiantes.class))).thenReturn(estudianteResponseDTO);

        // Act
        EstudianteResponseDTO result = estudiantesService.createEstudiante(estudianteRequestDTO);

        // Assert
        assertNotNull(result);
        assertThat(result.getNombre()).isEqualTo("David");
        assertThat(result.getCorreo()).isEqualTo("david@upc.edu.pe");
        verify(userRepository, times(1)).save(any(User.class));
        verify(estudiantesRepository, times(1)).save(any(Estudiantes.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el correo institucional no existe en base de datos")
    void createEstudiante_CorreoNoEncontrado_ThrowsException() {
        // Arrange
        when(datosUniversitariosRepository.existsByCorreoInstitucional(estudianteRequestDTO.getCorreo())).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> estudiantesService.createEstudiante(estudianteRequestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Correo no encontrado en base de datos");

        verify(datosUniversitariosRepository, times(1)).existsByCorreoInstitucional(estudianteRequestDTO.getCorreo());
        verifyNoMoreInteractions(estudiantesRepository);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando ya existe un estudiante con el mismo nombre y apellidos")
    void createEstudiante_EstudianteDuplicado_ThrowsException() {
        // Arrange
        when(datosUniversitariosRepository.existsByCorreoInstitucional(estudianteRequestDTO.getCorreo())).thenReturn(true);
        when(estudiantesRepository.existsByNombreAndApellidos("David", "Serrudo")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> estudiantesService.createEstudiante(estudianteRequestDTO))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Estudiante existente");

        verify(estudiantesRepository, times(1)).existsByNombreAndApellidos("David", "Serrudo");
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el distrito no existe")
    void createEstudiante_DistritoNoEncontrado_ThrowsException() {
        // Arrange
        when(datosUniversitariosRepository.existsByCorreoInstitucional(estudianteRequestDTO.getCorreo())).thenReturn(true);
        when(estudiantesRepository.existsByNombreAndApellidos("David", "Serrudo")).thenReturn(false);
        when(distritoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> estudiantesService.createEstudiante(estudianteRequestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Distrito no encontrado");

        verify(distritoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando la universidad no existe")
    void createEstudiante_UniversidadNoEncontrada_ThrowsException() {
        // Arrange
        when(datosUniversitariosRepository.existsByCorreoInstitucional(estudianteRequestDTO.getCorreo())).thenReturn(true);
        when(estudiantesRepository.existsByNombreAndApellidos("David", "Serrudo")).thenReturn(false);
        when(distritoRepository.findById(1L)).thenReturn(Optional.of(distrito));
        when(universidadRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> estudiantesService.createEstudiante(estudianteRequestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Universidad no encontrada");

        verify(universidadRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe retornar todos los estudiantes correctamente mapeados")
    void getAllEstudiantes_Success() {
        // Arrange
        Estudiantes estudiante2 = new Estudiantes();
        estudiante2.setId(2L);
        estudiante2.setNombre("María");
        estudiante2.setApellidos("Pérez");
        estudiante2.setDistrito(distrito);
        estudiante2.setUniversidad(universidad);

        User user2 = new User();
        user2.setCorreo("maria@upc.edu.pe");
        estudiante2.setUser(user2);

        EstudianteResponseDTO dto1 = estudianteResponseDTO;
        EstudianteResponseDTO dto2 = new EstudianteResponseDTO();
        dto2.setId(2L);
        dto2.setNombre("María");
        dto2.setApellidos("Pérez");
        dto2.setCorreo("maria@upc.edu.pe");
        dto2.setDistrito("Miraflores");
        dto2.setUniversidad("UPC");

        when(estudiantesRepository.findAll()).thenReturn(List.of(estudiante, estudiante2));
        when(estudiantesMapper.convertToDTO(estudiante)).thenReturn(dto1);
        when(estudiantesMapper.convertToDTO(estudiante2)).thenReturn(dto2);

        // Act
        var result = estudiantesService.getAllEstudiantes();

        // Assert
        assertNotNull(result);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getNombre()).isEqualTo("David");
        assertThat(result.get(1).getNombre()).isEqualTo("María");
        verify(estudiantesRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar un estudiante correctamente cuando existe el ID")
    void getEstudianteById_Success() {
        // Arrange
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));
        when(estudiantesMapper.convertToDTO(estudiante)).thenReturn(estudianteResponseDTO);

        // Act
        EstudianteResponseDTO result = estudiantesService.getEstudianteById(1L);

        // Assert
        assertNotNull(result);
        assertThat(result.getNombre()).isEqualTo("David");
        assertThat(result.getCorreo()).isEqualTo("david@upc.edu.pe");
        verify(estudiantesRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando no se encuentra el estudiante por ID")
    void getEstudianteById_NoEncontrado_ThrowsException() {
        // Arrange
        when(estudiantesRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> estudiantesService.getEstudianteById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Estudiante no encontrado con id: 99");

        verify(estudiantesRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Debe eliminar un estudiante correctamente cuando existe el ID")
    void deleteEstudiante_Success() {
        // Arrange
        when(estudiantesRepository.findById(1L)).thenReturn(Optional.of(estudiante));

        // Act
        estudiantesService.deleteEstudiante(1L);

        // Assert
        verify(estudiantesRepository, times(1)).findById(1L);
        verify(estudiantesRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar eliminar un estudiante inexistente")
    void deleteEstudiante_NoEncontrado_ThrowsException() {
        // Arrange
        when(estudiantesRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> estudiantesService.deleteEstudiante(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Estudiante no encontrado con id: 99");

        verify(estudiantesRepository, times(1)).findById(99L);
        verify(estudiantesRepository, never()).deleteById(anyLong());
    }


    @Test
    @DisplayName("Debe lanzar excepción cuando no se encuentra el rol ROLE_ESTUDIANTE")
    void createEstudiante_RolNoEncontrado_ThrowsException() {
        // Arrange
        when(datosUniversitariosRepository.existsByCorreoInstitucional(estudianteRequestDTO.getCorreo())).thenReturn(true);
        when(estudiantesRepository.existsByNombreAndApellidos("David", "Serrudo")).thenReturn(false);
        when(distritoRepository.findById(1L)).thenReturn(Optional.of(distrito));
        when(universidadRepository.findById(1L)).thenReturn(Optional.of(universidad));
        when(roleRepository.findByName(RoleType.ROLE_ESTUDIANTE)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> estudiantesService.createEstudiante(estudianteRequestDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Rol ROLE_USER no encontrado");

        verify(roleRepository, times(1)).findByName(RoleType.ROLE_ESTUDIANTE);
    }



}
