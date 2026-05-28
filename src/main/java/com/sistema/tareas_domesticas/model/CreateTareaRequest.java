package com.sistema.tareas_domesticas.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreateTareaRequest {
    private Long usuarioId;
    private String nombre;
    private String descripcion;
    private String prioridad;
    private LocalDate fechaLimite;
    private Long usuarioAsignadoId; // Necesario para HU-09: editar tarea
}