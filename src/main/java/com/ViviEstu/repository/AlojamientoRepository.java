package com.ViviEstu.repository;

import com.ViviEstu.model.entity.Alojamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AlojamientoRepository extends JpaRepository<Alojamiento, Long> {

    boolean existsByNroPartida(String nroPartida);
    List<Alojamiento> findByDistritoId(Long distritoId);

    @Query("SELECT ua.alojamiento FROM UniAlojamiento ua WHERE ua.universidad.id = :universidadId")
    List<Alojamiento> findByUniversidadId(@Param("universidadId") Long universidadId);

    Long countByPropietarioIdAndAlquiladoIsFalse(long l);

    @Query("SELECT a FROM Alojamiento a WHERE a.latitud BETWEEN :minLat AND :maxLat AND a.longitud BETWEEN :minLng AND :maxLng")
    List<Alojamiento> findByBounds(@Param("minLat") double minLat, @Param("maxLat") double maxLat, @Param("minLng") double minLng, @Param("maxLng") double maxLng);

    List<Alojamiento> findByPropietarioId(Long propietarioId);
}

