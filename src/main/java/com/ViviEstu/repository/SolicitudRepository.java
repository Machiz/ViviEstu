package com.ViviEstu.repository;

import com.ViviEstu.model.entity.Solicitudes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudRepository extends JpaRepository<Solicitudes,Long> {

}
