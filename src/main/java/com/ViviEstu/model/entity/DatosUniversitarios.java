package com.ViviEstu.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "datos_universitarios")
@NoArgsConstructor
@AllArgsConstructor
public class DatosUniversitarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "correo_institucional", nullable = false, length = 50)
    private String correoInstitucional;
}
