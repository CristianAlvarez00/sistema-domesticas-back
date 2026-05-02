package com.sistema.tareas_domesticas.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateTareaRequest {
    private Long usuarioId;
    private String nombre;
    private String descripcion;
    private String prioridad;
    private LocalDate fechaLimite;
}