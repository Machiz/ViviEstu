package com.ViviEstu.mapper;

import com.ViviEstu.model.dto.response.UniversidadResponseDTO;
import com.ViviEstu.model.entity.Universidad;

public class UniversidadMapper {
    public static UniversidadResponseDTO toResponse(Universidad u) {
        if (u == null) return null;
        UniversidadResponseDTO r = new UniversidadResponseDTO();
        r.setId(u.getId());
        r.setNombre(u.getNombre());
        if (u.getDistrito() != null) {
            r.setDistritoNombre(u.getDistrito().getNombre());
        }
        return r;
    }
}
