package com.ViviEstu.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "ImagenesAlojamiento")
@NoArgsConstructor
@AllArgsConstructor
public class ImagenesAlojamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(name = "public_id")
    private String publicId;

    @ManyToOne
    @JoinColumn(name = "alojamientos_id", nullable = false)
    private Alojamiento alojamiento;
}
