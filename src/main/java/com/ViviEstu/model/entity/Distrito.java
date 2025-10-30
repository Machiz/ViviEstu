package com.ViviEstu.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = "distritos")
public class Distrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio_prom")
    private Integer precioProm;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "url_img")
    private String urlImg;

    @Column(name = "seguridad")
    private Integer seguridad;

    @Column(name = "last_update")
    private LocalDate lastUpdate;

}
