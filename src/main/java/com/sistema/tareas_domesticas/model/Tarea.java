package com.sistema.tareas_domesticas.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "tareas")
@Data
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String descripcion;

    @Column(nullable = false)
    private String prioridad; // ALTA, MEDIA, BAJA

    @Column(nullable = false)
    private LocalDate fechaLimite;

    @Column(nullable = false)
    private String estado = "Pendiente";

    @Column(nullable = false)
    private Long hogarId;
}