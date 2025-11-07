package com.ViviEstu.repository;

import com.ViviEstu.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}
