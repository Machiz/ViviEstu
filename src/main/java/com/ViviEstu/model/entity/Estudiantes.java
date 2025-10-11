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

    @Column(name = "correo", nullable = false, unique = true, length = 50)
    private String correo;

    @Column(nullable = false, length = 20)
    private String contrasenia;

    @Column(nullable = false, length = 15)
    private String telefono;

    @Column(nullable = false)
    private boolean verificado = false; // Valor por defecto

    @Column(name = "distrito_preferencia", nullable = false, length = 50)
    private String distritoPreferencia = "No definido"; // Valor por defecto

    @Column(nullable = false, length = 50)
    private String carrera;

    @Column(name = "url_perfil", length = 100)
    private String urlPerfil;

    @Column(name = "DNI", nullable = false)
    private Integer dni;

    // Relaciones con otras tablas
    @ManyToOne
    @JoinColumn(name = "distrito_id")
    private Distrito distrito;

    @ManyToOne
    @JoinColumn(name = "universidad_id")
    private Universidad universidad;
}
