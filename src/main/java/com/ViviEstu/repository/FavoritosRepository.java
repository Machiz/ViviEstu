package com.ViviEstu.repository;

import com.ViviEstu.model.entity.Favoritos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritosRepository extends JpaRepository<Favoritos, Long> {
    /**
     * Cuenta el número de favoritos para un estudiante específico.
     * @param estudianteId el ID del estudiante.
     * @return el número de favoritos del estudiante.
     */
    long countByEstudianteId(Long estudianteId);

    /**
     * Verifica si ya existe un favorito para una combinación de estudiante y alojamiento.
     * @param estudianteId el ID del estudiante.
     * @param alojamientoId el ID del alojamiento.
     * @return true si ya existe, false en caso contrario.
     */
    boolean existsByEstudianteIdAndAlojamientoId(Long estudianteId, Long alojamientoId);
}

