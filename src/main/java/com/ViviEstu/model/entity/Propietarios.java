package com.ViviEstu.model.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "propietarios")
public class Propietarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", length = 20, nullable = false)
    @Size(max = 20, message = "El nombre no debe exceder los 20 caracteres")
    private String nombre;

    @Column(name = "apellidos", length = 50, nullable = false)
    @Size(max = 50, message = "Los apellidos no debe exceder los 50 caracteres")
    private String apellidos;


    @Column(name = "correo", length = 50, nullable = false)
    private String correo;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "contrasenia", length = 50, nullable = false)
    private String contrasenia;

    @Column(name = "dni", length = 8, nullable = false)
    private String dni;
}

