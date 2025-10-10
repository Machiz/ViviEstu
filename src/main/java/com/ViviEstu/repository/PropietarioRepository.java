package com.ViviEstu.repository;

import com.ViviEstu.model.entity.Propietarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PropietarioRepository  extends JpaRepository<Propietarios, Long> {
    @Query("SELECT p FROM Propietarios p WHERE p.nombre = ?1")
    Propietarios findByNombre(String nombre);
    // Custom JPQL query: find by email
    @Query("SELECT p FROM Propietarios p WHERE p.correo = ?1")
    Propietarios findByEmail(String email);
}
