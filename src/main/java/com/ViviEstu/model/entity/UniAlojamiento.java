package com.ViviEstu.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "alojamientos_universidades")
@NoArgsConstructor
@AllArgsConstructor
public class UniAlojamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "alojamiento_id")
    private Alojamiento alojamiento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "universidad_id")
    private Universidad universidad;

}