package com.ViviEstu.repository;

import com.ViviEstu.model.entity.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByAlojamientoId(Long alojamientoId);
}
