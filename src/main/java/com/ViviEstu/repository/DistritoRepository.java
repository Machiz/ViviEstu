package com.ViviEstu.repository;

import com.upc.viviestu.model.entity.Distrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistritoRepository extends JpaRepository<Distrito,Long>{

    /**
     * Permite una búsqueda flexible que no distingue mayúsculas de minúsculas.
     * @param nombre Termino de búsqueda para el nombre del distrito.
     * @return Lista de distritos que coinciden con el nombre.
     */
    List<Distrito> findByNombreContainingIgnoreCase(String nombre);

    /**
     * @param precioMin Precio promedio mínimo.
     * @param precioMax Precio promedio máximo.
     * @return Lista de distritos dentro del rango de precios.
     */
    List<Distrito> findByPrecioPromBetween(Integer precioMin, Integer precioMax);

    /**
     * @param tipo Tipo de alojamiento (ej. "departamento", "habitación").
     * @return Lista de distritos que ofrecen ese tipo de alojamiento.
     */
    List<Distrito> findByTipo(String tipo);
}
