package com.ViviEstu.repository;

import com.ViviEstu.model.entity.Universidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversidadRepository extends JpaRepository<Universidad, Long> {
    Optional<Universidad> findByNombre(String nombre);
    List<Universidad> findByNombreContainingIgnoreCase(String nombre);

}
