package com.sistema.tareas_domesticas.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private String prioridad; // LOW, MEDIUM, HIGH (Sincronizado con Front)

    @Column(name = "fecha_limite", nullable = false)
    private LocalDate fechaLimite;

    @Column(nullable = false)
    private String estado = "PENDIENTE";

    @Column(name = "hogar_id", nullable = false)
    private Long hogarId;

    // --- NUEVOS CAMPOS PARA REPARAR EL ERROR DE DEPENDENCIAS ---

    @Column(name = "usuario_asignado_id")
    private Long usuarioAsignadoId; // Este es el que pedía el Repository

    @Column(name = "usuario_asignado_nombre")
    private String usuarioAsignadoNombre; // Para mostrar el nombre en la TaskCard directamente

    /**
     * HU-11: Fecha y hora en que la tarea fue marcada como COMPLETADA.
     * Se registra automáticamente al cambiar el estado a COMPLETADA.
     */
    @Column(name = "fecha_completada")
    private LocalDateTime fechaCompletada;
}