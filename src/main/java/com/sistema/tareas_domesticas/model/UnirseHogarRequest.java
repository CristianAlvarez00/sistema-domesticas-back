package com.sistema.tareas_domesticas.model;
import lombok.Data;

@Data
public class UnirseHogarRequest {
    private String codigo;
    private Long usuarioId;
}