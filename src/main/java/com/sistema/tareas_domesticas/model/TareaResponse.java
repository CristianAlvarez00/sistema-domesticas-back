package com.sistema.tareas_domesticas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TareaResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private String prioridad;
    private LocalDate fechaLimite;
    private String estado;
    private Long hogarId;

    /**
     * HU-11: Fecha y hora de finalización. Será null si la tarea no está COMPLETADA.
     */
    private LocalDateTime fechaCompletada;
}