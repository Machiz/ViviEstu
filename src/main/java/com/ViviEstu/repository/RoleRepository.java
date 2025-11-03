package com.ViviEstu.repository;

import com.ViviEstu.model.entity.Role;
import com.ViviEstu.model.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
