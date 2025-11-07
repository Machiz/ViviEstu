package com.ViviEstu.repository;

import com.ViviEstu.model.entity.Distrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistritoRepository extends JpaRepository<Distrito,Long>{

    List<Distrito> findByNombreContainingIgnoreCase(String nombre);

    List<Distrito> findByPrecioPromBetween(Integer precioMin, Integer precioMax);


    List<Distrito> findByTipo(String tipo);
}
