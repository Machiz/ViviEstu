package com.ViviEstu.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "interacciones")
@NoArgsConstructor
@AllArgsConstructor
public class Interacciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "estudiantes_id", nullable = false)
    private Estudiantes estudiante;

    @ManyToOne
    @JoinColumn(name = "alojamientos_id", nullable = false)
    private Alojamiento alojamiento;
}
