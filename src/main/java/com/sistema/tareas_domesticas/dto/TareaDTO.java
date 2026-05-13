package com.sistema.tareas_domesticas.dto;

import com.sistema.tareas_domesticas.model.enums.EstadoAlerta;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TareaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String prioridad;
    private LocalDate fechaLimite;
    private String estado; // "PENDIENTE", "EN_PROGRESO", "COMPLETADA"
    private Long hogarId;
    private String usuarioAsignadoNombre;

    //Atributo añadido para no ensuciar el codigo del modelo. Asi se calcula el estado de la tarea en proceso, sin tocar el modelo.
    private EstadoAlerta alerta;
}
