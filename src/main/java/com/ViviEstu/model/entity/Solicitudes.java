package com.ViviEstu.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="solicitud")
public class Solicitudes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meses_alquiler", nullable = false)
    private Integer mesesAlquiler;

    @Column(name = "cant_inquilinos", nullable = false)
    private Integer cantInquilinos;

    @Column(name = "mensaje", nullable = true, length = 500)
    private String mensaje;

    @Column(name = "oferta", nullable = false)
    private Double oferta;


    @ManyToOne
    @JoinColumn(name = "estudiantes_id", nullable = false)
    private Estudiantes estudiantes;

    @ManyToOne
    @JoinColumn(name="alojamientos_id", nullable = false)
    private Alojamiento alojamiento;

}