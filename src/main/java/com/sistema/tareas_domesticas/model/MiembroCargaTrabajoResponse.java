package com.sistema.tareas_domesticas.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MiembroCargaTrabajoResponse {
    private Long usuarioId;
    private String nombre;
    private String rol;
    private Long tareasPendientes;
    private Long tareasEnProceso;
    private Long tareasCompletadas;
    private Long tareasVencidas;
    private Long totalTareas;
}
