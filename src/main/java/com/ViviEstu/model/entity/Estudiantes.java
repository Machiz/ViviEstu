package com.ViviEstu.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estudiantes")
public class Estudiantes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellidos", nullable = false, length = 50)
    private String apellidos;

    @Column(name = "correo", nullable = false, unique = true, length = 50)
    private String correo;

    @Column(nullable = false, length = 20)
    private String contrasenia;

    @Column(nullable = false, length = 15)
    private String telefono;

    @Column(nullable = false, length = 50)
    private String carrera;

    @Column(nullable = false, length = 50)
    private Integer ciclo;

    @Column(name = "DNI", nullable = false)
    private String dni;

    // Relaciones con otras tablas
    @ManyToOne
    @JoinColumn(name = "distrito_id")
    private Distrito distrito;

    @ManyToOne
    @JoinColumn(name = "universidad_id")
    private Universidad universidad;
}
