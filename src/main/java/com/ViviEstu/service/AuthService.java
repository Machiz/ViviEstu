package com.ViviEstu.service;

import com.ViviEstu.exception.DuplicateResourceException;
import com.ViviEstu.exception.ResourceNotFoundException;
import com.ViviEstu.model.dto.request.LoginRequestDTO;
import com.ViviEstu.model.dto.request.RegisterEstudianteRequestDTO;
import com.ViviEstu.model.dto.request.RegisterPropietarioRequestDTO;
import com.ViviEstu.model.dto.response.AuthResponseDTO;
import com.ViviEstu.model.entity.*;
import com.ViviEstu.repository.*;
import com.ViviEstu.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EstudiantesService estudiantesService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final DatosUniversitariosRepository datosUniversitariosRepository;
    private final EstudiantesRepository estudiantesRepository;
    private final DistritoRepository distritoRepository;
    private final UniversidadRepository universidadRepository;
    private final PropietariosRepository propietariosRepository;


    @Transactional
    public AuthResponseDTO registerEstudiante(RegisterEstudianteRequestDTO request) {
        // Crear User
        User user = new User();
        user.setCorreo(request.getCorreo());
        user.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
        user.setRole(roleRepository.findByName(RoleType.ROLE_ESTUDIANTE)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado")));
        user.setActive(true);

        if (!datosUniversitariosRepository.existsByCorreoInstitucional(request.getCorreo())) {
            throw new ResourceNotFoundException("Correo no encontrado en base de datos");
        }

        User savedUser = new User();
        try {
            savedUser = userRepository.save(user);

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("El correo ya está registrado");
        }

        // Crear Estudiante
        Universidad universidad = universidadRepository.findById(request.getUniversidadId())
                .orElseThrow(() -> new ResourceNotFoundException("Universidad no encontrada"));

        Distrito distrito = distritoRepository.findById(request.getDistritoId())
                .orElseThrow(() -> new ResourceNotFoundException("Distrito no encontrado"));

        Estudiantes estudiante = new Estudiantes();
        estudiante.setNombre(request.getNombre());
        estudiante.setApellidos(request.getApellidos());
        estudiante.setTelefono(request.getTelefono());
        estudiante.setCarrera(request.getCarrera());
        estudiante.setCiclo(request.getCiclo());
        estudiante.setUniversidad(universidad);
        estudiante.setDistrito(distrito);
        estudiante.setUser(savedUser);
        estudiante.setDni(request.getDni());

        estudiantesRepository.save(estudiante);

        // Token
        String token = jwtUtil.generateToken(
                savedUser.getCorreo(),
                estudiante.getNombre(),
                estudiante.getId(),
                savedUser.getRole().getName().toString()
        );

        return new AuthResponseDTO(token, savedUser.getCorreo(), estudiante.getNombre());
    }

    @Transactional
    public AuthResponseDTO registerPropietario(RegisterPropietarioRequestDTO request) {
        // Crear User
        User user = new User();
        user.setCorreo(request.getCorreo());
        user.setContrasenia(passwordEncoder.encode(request.getContrasenia()));
        user.setRole(roleRepository.findByName(RoleType.ROLE_PROPIETARIO)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado")));
        user.setActive(true);

        User savedUser = new User();

        try {
            savedUser = userRepository.save(user);

        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("El correo ya está registrado");
        }


        // Crear Propietario
        Propietarios propietario = new Propietarios();
        propietario.setNombre(request.getNombre());
        propietario.setApellidos(request.getApellidos());
        propietario.setTelefono(request.getTelefono());
        propietario.setDni(request.getDni());
        propietario.setUser(savedUser);

        propietariosRepository.save(propietario);

        // Token
        String token = jwtUtil.generateToken(
                savedUser.getCorreo(),
                propietario.getNombre(),
                propietario.getId(),
                savedUser.getRole().getName().toString()
        );

        return new AuthResponseDTO(token, savedUser.getCorreo(), propietario.getNombre());
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO request) {
        // 1. Autenticar usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(),
                        request.getContrasenia()
                )
        );

        // 2. Buscar usuario
        User user = userRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 3. Verificar rol
        RoleType role = user.getRole().getName();

        // ADMIN → no se relaciona con estudiante ni propietario
        if (role == RoleType.ROLE_ADMIN) {
            String token = jwtUtil.generateToken(
                    user.getCorreo(),
                    "Admin",
                    null,
                    "ROLE_ADMIN"

            );
            return new AuthResponseDTO(token, user.getCorreo(), "Administrador");
        }

        // ESTUDIANTE → buscar en tabla estudiantes
        if (role == RoleType.ROLE_ESTUDIANTE) {
            Estudiantes estudiante = estudiantesRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Estudiante no encontrado para el usuario"));

            String token = jwtUtil.generateToken(
                    user.getCorreo(),
                    estudiante.getNombre(),
                    estudiante.getId(),
                    "ROLE_ESTUDIANTE"
            );
            return new AuthResponseDTO(token, user.getCorreo(), estudiante.getNombre());
        }

        // PROPIETARIO → buscar en tabla propietarios
        if (role == RoleType.ROLE_PROPIETARIO) {
            Propietarios propietario = propietariosRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado para el usuario"));

            String token = jwtUtil.generateToken(
                    user.getCorreo(),
                    propietario.getNombre(),
                    propietario.getId(),
                    "ROLE_PROPIETARIO"
            );
            return new AuthResponseDTO(token, user.getCorreo(), propietario.getNombre());
        }

        // 4. Si llega acá, el rol no está reconocido
        throw new RuntimeException("Rol no reconocido: " + role);
    }
    @Transactional
    public boolean verificarCorreoInstitucional(String correoInstitucional) {
        if (correoInstitucional == null || !correoInstitucional.contains("@")) {
            throw new IllegalArgumentException("Correo institucional inválido");
        }

        String dominio = correoInstitucional.substring(correoInstitucional.indexOf("@") + 1);

        if (!datosUniversitariosRepository.existsByCorreoInstitucional(dominio)) {
            throw new RuntimeException("El dominio del correo no pertenece a ninguna universidad registrada.");
        }

        if (datosUniversitariosRepository.existsByCorreoInstitucional(correoInstitucional)) {
            throw new RuntimeException("Este correo institucional ya está verificado por otro estudiante.");
        }

        if (!datosUniversitariosRepository.existsByCorreoInstitucional(correoInstitucional)) {
            throw new RuntimeException("El correo institucional no figura en la base de datos universitaria.");
        }

        return true;
    }

}