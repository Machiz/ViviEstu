package com.ViviEstu.repository;

import com.ViviEstu.model.entity.Interacciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteraccionesRepository extends JpaRepository<Interacciones, Integer> {

    @Query("SELECT COUNT(i) FROM Interacciones i WHERE i.alojamiento.id = :alojamientoId")
    long contarPorAlojamiento(@Param("alojamientoId") Long alojamientoId);

    List<Interacciones> findByAlojamiento_Id(Long alojamientoId);

}
