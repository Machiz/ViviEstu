package com.ViviEstu.repository;

import com.ViviEstu.model.entity.Interacciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InteraccionesRepository extends JpaRepository<Interacciones, Integer> {
}
