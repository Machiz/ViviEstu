package com.ViviEstu.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notificaciones_propietarios")
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionPropie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 50)
    private String mensaje;

    @ManyToOne
    @JoinColumn(name = "propietarios_id", nullable = false)
    private Propietarios propietario;
}
