package com.ViviEstu.repository;


import com.ViviEstu.model.entity.Estudiantes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudiantesRepository extends JpaRepository<Estudiantes, Long> {

    boolean existsByNombreAndApellidos(String nombre, String apellidos);

}
