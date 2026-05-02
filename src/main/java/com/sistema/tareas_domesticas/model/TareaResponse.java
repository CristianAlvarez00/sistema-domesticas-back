package com.sistema.tareas_domesticas.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

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
}