package com.sistema.tareas_domesticas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sistema.tareas_domesticas.model.enums.EstadoAlerta;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor // Requerido por Spring Boot para mapear los payloads de los controladores de forma automática
public class TareaDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String prioridad;

    // Asegura que Jackson convierta correctamente los Strings del Front a LocalDate de Java
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaLimite;

    private String estado; // "PENDIENTE", "EN_PROCESO", "COMPLETADA"
    private Long hogarId;
    private String usuarioAsignadoNombre;

    // Atributo añadido para no ensuciar el código del modelo.
    // Así se calcula el estado de la tarea en proceso, sin tocar el modelo.
    private EstadoAlerta alerta;
}