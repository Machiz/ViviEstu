package com.ViviEstu.repository;

import com.ViviEstu.model.entity.DatosUniversitarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatosUniversitariosRepository extends JpaRepository<DatosUniversitarios, Long>
{
    boolean existsByCorreoInstitucional(String correoInstitucional);

}
