package com.ViviEstu.repository;

import com.ViviEstu.model.entity.Estudiantes;
import com.ViviEstu.model.entity.Propietarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropietariosRepository extends JpaRepository<Propietarios, Long> {
    boolean existsByNombreAndApellidos(String nombre, String apellidos);

    Optional<Propietarios> findByUserId(Long id);
}
