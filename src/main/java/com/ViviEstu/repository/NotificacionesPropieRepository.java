package com.ViviEstu.repository;

import com.ViviEstu.model.entity.NotificacionPropie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionesPropieRepository extends JpaRepository<NotificacionPropie,Long> {
    List<NotificacionPropie> findByPropietario_Id(Long propietarioId);
}
