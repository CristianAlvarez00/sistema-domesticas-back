package com.sistema.tareas_domesticas.controller;

import com.sistema.tareas_domesticas.model.CreateTareaRequest;
import com.sistema.tareas_domesticas.model.Tarea;
import com.sistema.tareas_domesticas.model.TareaResponse;
import com.sistema.tareas_domesticas.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tareas")
@CrossOrigin(origins = "*")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @PostMapping("/crear")
    public TareaResponse crearTarea(@RequestBody CreateTareaRequest request) {
        Tarea tarea = tareaService.crearTarea(
                request.getUsuarioId(),
                request.getNombre(),
                request.getDescripcion(),
                request.getPrioridad(),
                request.getFechaLimite()
        );
        return new TareaResponse(
                tarea.getId(),
                tarea.getNombre(),
                tarea.getDescripcion(),
                tarea.getPrioridad(),
                tarea.getFechaLimite(),
                tarea.getEstado(),
                tarea.getHogarId()
        );
    }
}