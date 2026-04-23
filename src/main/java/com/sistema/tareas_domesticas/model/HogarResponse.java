package com.sistema.tareas_domesticas.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HogarResponse {
    private Long hogarId;
    private String nombre;
    private String codigoInvitacion;
}
