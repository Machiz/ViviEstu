package com.ViviEstu.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "datos_propiedades")
@NoArgsConstructor
@AllArgsConstructor
public class DatosPropiedades {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nro_partida", nullable = false, length = 8)
    private String nroPartida;

    @Column(name = "DNI_propietario", nullable = false, length = 8)
    private String dniPropietario;
}
