package com.ViviEstu.repository;

import com.ViviEstu.model.entity.ImagenesAlojamiento;
import com.ViviEstu.model.entity.Transporte;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransporteRepository extends JpaRepository<Transporte,Long> {
    List<Transporte> findByAlojamientoId(Long alojamientoId);

}
