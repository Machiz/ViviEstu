package com.ViviEstu.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "transporte")
@AllArgsConstructor
@NoArgsConstructor
public class Transporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "alojamientos_id", nullable = false)
    private Alojamiento alojamiento;

}
