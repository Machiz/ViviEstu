package com.ViviEstu.repository;


import com.ViviEstu.model.entity.Estudiantes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudiantesRepository extends JpaRepository<Estudiantes, Long> {

    boolean existsByNombreAndApellidos(String nombre, String apellidos);
    Optional<Estudiantes> findByUser_Correo(String correo);
    Optional<Estudiantes> findByUserId(Long id);
}
