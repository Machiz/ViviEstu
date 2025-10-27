package com.ViviEstu.repository;


import com.ViviEstu.model.entity.UniAlojamiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UniAlojamientoRepository extends JpaRepository<UniAlojamiento, Long> {

    List<UniAlojamiento> findByAlojamientoId(Long alojamientoId);
    List<UniAlojamiento> findByUniversidadId(Long universidadId);
}
