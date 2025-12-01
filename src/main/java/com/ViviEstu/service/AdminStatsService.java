package com.ViviEstu.service;

import com.ViviEstu.model.dto.response.AdminStatsResponseDTO;
import com.ViviEstu.repository.AlojamientoRepository;
import com.ViviEstu.repository.PropietariosRepository;
import com.ViviEstu.repository.UniversidadRepository;
import com.ViviEstu.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AdminStatsService {

    private final UserRepository userRepository;
    private final PropietariosRepository propietariosRepository;
    private final AlojamientoRepository alojamientoRepository;
    private final UniversidadRepository universidadRepository;

    /**
     * Obtiene el conteo de entidades principales en la base de datos.
     * Requiere el rol ADMIN.
     */
    @Transactional(readOnly = true)
    public AdminStatsResponseDTO getGeneralStats() {
        // JpaRepository.count() devuelve un Long.
        Long totalUsuarios = userRepository.count();
        Long totalPropietarios = propietariosRepository.count();
        Long totalAlojamientos = alojamientoRepository.count();
        Long totalUniversidades = universidadRepository.count();

        return new AdminStatsResponseDTO(
                totalUsuarios,
                totalPropietarios,
                totalAlojamientos,
                totalUniversidades
        );
    }
}