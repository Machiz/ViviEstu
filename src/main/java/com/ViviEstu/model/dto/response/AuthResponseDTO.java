package com.ViviEstu.model.dto.response;

import com.ViviEstu.model.entity.Role;

public record AuthResponseDTO(
    String token,
    String type,
    String email,
    String name,
    Long id,
    String role
) {
    public AuthResponseDTO(String token, String email, String name, Long id, String role){
        this(token, "Baerer", email, name, id, role);
    }
}
