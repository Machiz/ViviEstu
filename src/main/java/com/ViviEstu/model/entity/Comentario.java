package com.ViviEstu.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "comentarios_alojamiento")
@AllArgsConstructor
@NoArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "text")
    private String contenido;

    @ManyToOne
    @JoinColumn(name = "alojamientos_id", nullable = false)
    private Alojamiento alojamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiantes_id", nullable = false)
    private Estudiantes estudiante;
}