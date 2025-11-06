package com.ViviEstu.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Data
@Entity
@Table(name = "alojamientos")
@NoArgsConstructor
@AllArgsConstructor
public class Alojamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String titulo;

    @Column(nullable = false, columnDefinition = "text")
    private String descripcion;

    @Column(nullable = false, length = 100)
    private String direccion;

    @Column(name = "nro_partida", nullable = false, length = 8)
    private String nroPartida;

    @Column(name = "precio_mensual", nullable = false)
    private BigDecimal precioMensual;

    @Column(nullable = false)
    private Timestamp fecha;

    @Column(nullable = false)
    private Boolean alquilado;

    @Column()
    private double latitud;

    @Column()
    private double longitud;

    @ManyToOne()
    @JoinColumn(name = "distritos_id", nullable = false)
    private Distrito distrito;

    @ManyToOne()
    @JoinColumn(name = "propietarios_id", nullable = false)
    private Propietarios propietario;


}
