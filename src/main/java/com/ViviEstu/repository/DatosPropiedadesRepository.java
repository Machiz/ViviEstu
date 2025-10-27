package com.ViviEstu.repository;

import com.ViviEstu.model.entity.DatosPropiedades;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatosPropiedadesRepository extends JpaRepository<DatosPropiedades, Long> {

    boolean existsByDniPropietarioAndNroPartida(String dni, String nroPartida);
}
