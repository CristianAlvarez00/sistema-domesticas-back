package com.sistema.tareas_domesticas.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "hogares")
@Data
public class Hogar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String codigoInvitacion;
}
