package com.ViviEstu.config;

import com.ViviEstu.model.entity.Estudiantes;
import com.ViviEstu.model.entity.Role;
import com.ViviEstu.model.entity.RoleType;
import com.ViviEstu.model.entity.User;
import com.ViviEstu.repository.EstudiantesRepository;
import com.ViviEstu.repository.RoleRepository;
import com.ViviEstu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EstudiantesRepository estudiantesRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing database with default data...");

        // Crear roles
        Role estudianteRole = createRoleIfNotExists(RoleType.ROLE_ESTUDIANTE);
        Role adminRole = createRoleIfNotExists(RoleType.ROLE_ADMIN);
        Role propietarioRole = createRoleIfNotExists(RoleType.ROLE_PROPIETARIO);


        // Crear usuario admin
        createAdminUserIfNotExists(adminRole);

        log.info("Database initialization completed.");
    }

    private Role createRoleIfNotExists(RoleType roleType) {
        return roleRepository.findByName(roleType)
                .orElseGet(() -> {
                    Role role = new Role(roleType);
                    roleRepository.save(role);
                    log.info("Created role: {}", roleType);
                    return role;
                });
    }

    private void createAdminUserIfNotExists(Role adminRole) {
        String adminEmail = "admin@fintech.com";

        if (userRepository.existsByCorreo(adminEmail)) {
            log.info("Admin user already exists: {}", adminEmail);
            return;
        }

        User adminUser = new User();
        adminUser.setCorreo(adminEmail);
        adminUser.setContrasenia(passwordEncoder.encode("admin123"));
        adminUser.setRole(adminRole);
        adminUser.setActive(true);
        User savedAdmin = userRepository.save(adminUser);


        log.info("========================================");
        log.info("DEFAULT ADMIN USER CREATED:");
        log.info("Email: {}", adminEmail);
        log.info("Password: admin123");
        log.info("⚠️  CHANGE THIS PASSWORD IN PRODUCTION!");
        log.info("========================================");
    }
}