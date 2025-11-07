package com.ViviEstu.Unit;

import com.ViviEstu.exception.DuplicateResourceException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.model.dto.request.LoginRequestDTO;
import com.ViviEstu.model.dto.request.RegisterEstudianteRequestDTO;
import com.ViviEstu.model.dto.request.RegisterPropietarioRequestDTO;
import com.ViviEstu.model.dto.response.AuthResponseDTO;
import com.ViviEstu.model.entity.*;
import com.ViviEstu.repository.*;
import com.ViviEstu.security.JwtUtil;
import com.ViviEstu.service.AuthService;
import com.ViviEstu.service.EstudiantesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private EstudiantesService estudiantesService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private DatosUniversitariosRepository datosUniversitariosRepository;
    @Mock private EstudiantesRepository estudiantesRepository;
    @Mock private DistritoRepository distritoRepository;
    @Mock private UniversidadRepository universidadRepository;
    @Mock private PropietariosRepository propietariosRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Usuario registrado exitosamente")
    void testRegisterEstudiante_Success() {
        RegisterEstudianteRequestDTO req = new RegisterEstudianteRequestDTO();
        req.setCorreo("test@correo.com");
        req.setContrasenia("1234");
        req.setNombre("Juan");
        req.setApellidos("Perez");
        req.setTelefono("999999999");
        req.setCarrera("Sistemas");
        req.setCiclo(5);
        req.setUniversidadId(1L);
        req.setDistritoId(2L);
        req.setDni("12345678");

        Role role = new Role();
        role.setName(RoleType.ROLE_ESTUDIANTE);

        when(roleRepository.findByName(RoleType.ROLE_ESTUDIANTE)).thenReturn(Optional.of(role));
        when(datosUniversitariosRepository.existsByCorreoInstitucional(req.getCorreo())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(estudiantesRepository.existsByNombreAndApellidos(anyString(), anyString())).thenReturn(false);

        Universidad universidad = new Universidad();
        universidad.setId(1L);
        when(universidadRepository.findById(1L)).thenReturn(Optional.of(universidad));

        Distrito distrito = new Distrito();
        distrito.setId(2L);
        when(distritoRepository.findById(2L)).thenReturn(Optional.of(distrito));

        when(jwtUtil.generateToken(any(), any(), any(), any())).thenReturn("fake-token");

        AuthResponseDTO response = authService.registerEstudiante(req);

        assertNotNull(response);
        assertEquals("test@correo.com", response.email());
        assertEquals("Juan", response.name());
        assertEquals("fake-token", response.token());
        verify(userRepository, times(1)).save(any(User.class));
        verify(estudiantesRepository, times(1)).save(any(Estudiantes.class));
    }

    @Test
    @DisplayName("Registro de Estudiante-Correo no encontrado")
    void testRegisterEstudiante_CorreoNoEncontrado() {
        RegisterEstudianteRequestDTO req = new RegisterEstudianteRequestDTO();
        req.setCorreo("notfound@uni.com");

        when(roleRepository.findByName(RoleType.ROLE_ESTUDIANTE))
                .thenReturn(Optional.of(new Role(RoleType.ROLE_ESTUDIANTE)));
        when(datosUniversitariosRepository.existsByCorreoInstitucional(req.getCorreo())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> authService.registerEstudiante(req));
    }

    @Test
    @DisplayName("Registro de Estudiante-EstudianteDuplicado")
    void testRegisterEstudiante_EstudianteDuplicado() {
        RegisterEstudianteRequestDTO req = new RegisterEstudianteRequestDTO();
        req.setCorreo("a@a.com");
        req.setNombre("Juan");
        req.setApellidos("Perez");

        when(roleRepository.findByName(RoleType.ROLE_ESTUDIANTE))
                .thenReturn(Optional.of(new Role(RoleType.ROLE_ESTUDIANTE)));
        when(datosUniversitariosRepository.existsByCorreoInstitucional(req.getCorreo())).thenReturn(true);
        when(userRepository.save(any())).thenReturn(new User());
        when(estudiantesRepository.existsByNombreAndApellidos("Juan", "Perez")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> authService.registerEstudiante(req));
    }

    // ---------------------------
    // registerPropietario()
    // ---------------------------
    @Test
    @DisplayName("Registro de Propietarios-Exitosamente")
    void testRegisterPropietario_Success() {
        RegisterPropietarioRequestDTO req = new RegisterPropietarioRequestDTO();
        req.setCorreo("prop@correo.com");
        req.setContrasenia("1234");
        req.setNombre("Luis");
        req.setApellidos("Ramos");
        req.setTelefono("123456789");
        req.setDni("87654321");

        Role role = new Role();
        role.setName(RoleType.ROLE_PROPIETARIO);

        when(roleRepository.findByName(RoleType.ROLE_PROPIETARIO)).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtUtil.generateToken(any(), any(), any(), any())).thenReturn("token-prop");

        AuthResponseDTO res = authService.registerPropietario(req);

        assertNotNull(res);
        assertEquals("prop@correo.com", res.email());
        assertEquals("Luis", res.name());
        assertEquals("token-prop", res.token());
        verify(propietariosRepository, times(1)).save(any(Propietarios.class));
    }


    @Test
    @DisplayName("Login-Admin")
    void testLogin_Admin() {
        LoginRequestDTO req = new LoginRequestDTO("admin@correo.com", "1234");
        User user = new User();
        Role role = new Role();
        role.setName(RoleType.ROLE_ADMIN);
        user.setCorreo(req.getCorreo());
        user.setRole(role);

        when(userRepository.findByCorreo(req.getCorreo())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(any(), any(), any(), any())).thenReturn("token-admin");

        AuthResponseDTO res = authService.login(req);

        assertEquals("token-admin", res.token());
        assertEquals("Administrador", res.name());
    }

    @Test
    @DisplayName("Login Estudiante")
    void testLogin_Estudiante() {
        LoginRequestDTO req = new LoginRequestDTO("est@correo.com", "1234");
        User user = new User();
        user.setId(1L);
        user.setCorreo(req.getCorreo());
        Role role = new Role();
        role.setName(RoleType.ROLE_ESTUDIANTE);
        user.setRole(role);

        Estudiantes est = new Estudiantes();
        est.setId(10L);
        est.setNombre("Carlos");

        when(userRepository.findByCorreo(req.getCorreo())).thenReturn(Optional.of(user));
        when(estudiantesRepository.findByUserId(1L)).thenReturn(Optional.of(est));
        when(jwtUtil.generateToken(any(), any(), any(), any())).thenReturn("token-est");

        AuthResponseDTO res = authService.login(req);

        assertEquals("token-est", res.token());
        assertEquals("Carlos", res.name());
    }

    @Test
    @DisplayName("Login Propietario")
    void testLogin_Propietario() {
        LoginRequestDTO req = new LoginRequestDTO("prop@correo.com", "1234");
        User user = new User();
        user.setId(2L);
        user.setCorreo(req.getCorreo());
        Role role = new Role();
        role.setName(RoleType.ROLE_PROPIETARIO);
        user.setRole(role);

        Propietarios prop = new Propietarios();
        prop.setId(20L);
        prop.setNombre("Mario");

        when(userRepository.findByCorreo(req.getCorreo())).thenReturn(Optional.of(user));
        when(propietariosRepository.findByUserId(2L)).thenReturn(Optional.of(prop));
        when(jwtUtil.generateToken(any(), any(), any(), any())).thenReturn("token-prop");

        AuthResponseDTO res = authService.login(req);

        assertEquals("token-prop", res.token());
        assertEquals("Mario", res.name());
    }

    @Test
    @DisplayName("Login Usuario no Encontrado")
    void testLogin_UserNotFound() {
        LoginRequestDTO req = new LoginRequestDTO("notfound@correo.com", "1234");
        when(userRepository.findByCorreo(req.getCorreo())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.login(req));
    }
}
