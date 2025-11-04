package com.ViviEstu.repository;

import com.ViviEstu.model.entity.Solicitudes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitudes,Long> {
    // Buscar solicitudes por ID del estudiante
    List<Solicitudes> findByEstudiantes_Id(Long estudianteId);

    // Buscar solicitudes por ID del propietario del alojamiento
    @Query("SELECT s FROM Solicitudes s WHERE s.alojamiento.propietario.id = :propietarioId")
    List<Solicitudes> findByPropietarioId(Long propietarioId);
}
