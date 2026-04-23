package com.sistema.tareas_domesticas.model;

import lombok.Data;

@Data
public class CreateHogarRequest {
    private Long usuarioId;
    private String nombre;
}
