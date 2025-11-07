package com.ViviEstu.Unit;

import com.ViviEstu.exception.DuplicateResourceException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.mapper.PropietariosMapper;
import com.ViviEstu.model.dto.request.PropietariosRequestDTO;
import com.ViviEstu.model.dto.response.PropietariosResponseDTO;
import com.ViviEstu.model.entity.Propietarios;
import com.ViviEstu.model.entity.User;
import com.ViviEstu.repository.PropietariosRepository;
import com.ViviEstu.repository.UserRepository;
import com.ViviEstu.service.PropietarioService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PropietarioServiceTest {

    @Mock
    private PropietariosRepository propietarioRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PropietariosMapper propietariosMapper;

    @InjectMocks
    private PropietarioService propietarioService;

    private Propietarios propietario;
    private PropietariosRequestDTO request;
    private User user;
    private PropietariosResponseDTO response;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setCorreo("test@mail.com");
        user.setContrasenia("1234abcd");

        propietario = new Propietarios();
        propietario.setId(1L);
        propietario.setUser(user);
        propietario.setNombre("Juan");
        propietario.setApellidos("Lopez");
        propietario.setDni("12345678");
        propietario.setTelefono("999888777");

        request = new PropietariosRequestDTO();
        request.setNombre("Juan");
        request.setApellidos("Lopez");
        request.setDni("12345678");
        request.setTelefono("999888777");
        request.setCorreo("nuevo@mail.com");
        request.setContrasenia("abcd1234");

        response = new PropietariosResponseDTO();
        response.setId(1L);
        response.setNombre("Juan");
        response.setApellidos("Lopez");
        response.setCorreo("nuevo@mail.com");
    }

    @Test
    @DisplayName("CrearPropietario - Exitoso")
    void testCrearPropietario_Exitoso() {
        // Arrange
        when(propietarioRepository.existsByNombreAndApellidos("Juan", "Lopez")).thenReturn(false);
        when(propietariosMapper.toEntity(request)).thenReturn(propietario);
        when(propietariosMapper.toDTO(propietario)).thenReturn(response);

        // Act
        PropietariosResponseDTO result = propietarioService.crearPropietario(request);

        // Assert
        assertNotNull(result);
        assertEquals("Juan", result.getNombre());
        assertEquals("Lopez", result.getApellidos());
        verify(propietarioRepository).existsByNombreAndApellidos("Juan", "Lopez");
        verify(propietariosMapper).toEntity(request);
        verify(propietarioRepository).save(propietario);
        verify(propietariosMapper).toDTO(propietario);
    }

    // -------------------------------------------------------------------------
    @Test
    @DisplayName("CrearPropietario - Duplicado")
    void testCrearPropietario_Duplicado() {
        // Arrange
        when(propietarioRepository.existsByNombreAndApellidos("Juan", "Lopez")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class,
                () -> propietarioService.crearPropietario(request));

        verify(propietarioRepository).existsByNombreAndApellidos("Juan", "Lopez");
        verify(propietarioRepository, never()).save(any(Propietarios.class));
    }




    @Test
    @DisplayName("Actualizar propietario - Éxito")
    void testUpdatePropietario_Exitoso() {
        when(propietarioRepository.findById(1L)).thenReturn(Optional.of(propietario));
        when(propietariosMapper.toDTO(propietario)).thenReturn(response);

        PropietariosResponseDTO result = propietarioService.updatePropietario(1L, request);

        assertEquals("nuevo@mail.com", result.getCorreo());
        verify(userRepository, times(1)).save(user);
        verify(propietarioRepository, times(1)).save(propietario);
    }

    @Test
    @DisplayName("Actualizar propietario - No encontrado")
    void testUpdatePropietario_NoEncontrado() {
        when(propietarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> propietarioService.updatePropietario(1L, request));
    }

    @Test
    @DisplayName("Actualizar propietario - Falta campo obligatorio (nombre)")
    void testUpdatePropietario_FaltaCampoObligatorio_Nombre() {
        request.setNombre(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> propietarioService.updatePropietario(1L, request));
        assertEquals("Debe completar todos los campos obligatorios antes de guardar.", ex.getMessage());
    }

    @Test
    @DisplayName("Actualizar propietario - Falta campo obligatorio (Apellidos nulos)")
    void testUpdatePropietario_FaltaCampoObligatorio_ApellidosNulos() {
        request.setApellidos(null); // <- el campo que activa la línea 34

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> propietarioService.updatePropietario(1L, request));

        assertEquals("Debe completar todos los campos obligatorios antes de guardar.", ex.getMessage());
    }


    @Test
    @DisplayName("Actualizar propietario - Falta campo obligatorio (dni)")
    void testUpdatePropietario_FaltaCampoObligatorio_Dni() {
        request.setDni(null);
        assertThrows(IllegalArgumentException.class,
                () -> propietarioService.updatePropietario(1L, request));
    }

    @Test
    @DisplayName("Actualizar propietario - Falta campo obligatorio (correo)")
    void testUpdatePropietario_FaltaCampoObligatorio_Correo() {
        request.setCorreo(null);
        assertThrows(IllegalArgumentException.class,
                () -> propietarioService.updatePropietario(1L, request));
    }

    @Test
    @DisplayName("Actualizar propietario - Falta campo obligatorio (contraseña)")
    void testUpdatePropietario_FaltaCampoObligatorio_Contrasenia() {
        request.setContrasenia(null);
        assertThrows(IllegalArgumentException.class,
                () -> propietarioService.updatePropietario(1L, request));
    }

    @Test
    @DisplayName("Buscar propietario por ID - Éxito")
    void testFindById_Exito() {
        when(propietarioRepository.findById(1L)).thenReturn(Optional.of(propietario));
        when(propietariosMapper.toDTO(propietario)).thenReturn(response);

        PropietariosResponseDTO result = propietarioService.findPropietarioById(1L);
        assertEquals(response.getId(), result.getId());
    }

    @Test
    @DisplayName("Buscar propietario por ID - No encontrado")
    void testFindById_NoEncontrado() {
        when(propietarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> propietarioService.findPropietarioById(1L));
    }

    @Test
    @DisplayName("Listar propietarios - Éxito")
    void testFindAll_Exito() {
        when(propietarioRepository.findAll()).thenReturn(Collections.singletonList(propietario));
        when(propietariosMapper.toDTO(propietario)).thenReturn(response);

        List<PropietariosResponseDTO> result = propietarioService.findAllPropietarios();
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Eliminar propietario - Éxito")
    void testDeletePropietario_Exito() {
        propietarioService.deletePropietario(1L);
        verify(propietarioRepository, times(1)).deleteById(1L);
    }
}
