package com.ViviEstu.repository;

import com.ViviEstu.model.entity.ImagenesAlojamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenesRepository extends JpaRepository<ImagenesAlojamiento,Long> {


    List<ImagenesAlojamiento> findByAlojamientoId(Long alojamientoId);

}
