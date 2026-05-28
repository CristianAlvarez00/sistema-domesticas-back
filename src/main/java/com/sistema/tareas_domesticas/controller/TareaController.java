package com.sistema.tareas_domesticas.controller;

import com.sistema.tareas_domesticas.model.CreateTareaRequest;
import com.sistema.tareas_domesticas.model.Tarea;
import com.sistema.tareas_domesticas.model.TareaResponse;
import com.sistema.tareas_domesticas.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tareas")
@CrossOrigin(origins = "*")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    /**
     * HU-07: Crear una nueva tarea
     */
    @PostMapping("/crear")
    public TareaResponse crearTarea(@RequestBody CreateTareaRequest request) {
        Tarea tarea = tareaService.crearTarea(
                request.getUsuarioId(),
                request.getNombre(),
                request.getDescripcion(),
                request.getPrioridad(),
                request.getFechaLimite()
        );
        return mapToResponse(tarea);
    }

    /**
     * HU-08: Listar tareas del hogar con filtro opcional por estado
     * Escenario 1: GET /api/tareas/hogar/{hogarId} -> todas las tareas
     * Escenario 2: GET /api/tareas/hogar/{hogarId}?estado=PENDIENTE -> filtradas
     * Escenario 3: Lista vacía -> mensaje "No hay tareas registradas en este hogar"
     */
    @GetMapping("/hogar/{hogarId}")
    public ResponseEntity<?> listarTareasPorHogar(
            @PathVariable Long hogarId,
            @RequestParam(required = false) String estado) {

        List<Tarea> tareas = tareaService.listarTareasPorHogar(hogarId, estado);

        if (tareas.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonMap("mensaje", "No hay tareas registradas en este hogar"));
        }

        List<TareaResponse> response = tareas.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * HU-09: Editar una tarea existente
     * Escenario 1: Edición exitosa
     * Escenario 2: No se puede editar tarea completada
     * Escenario 3: Acceso denegado a Miembro
     */
    @PutMapping("/editar/{tareaId}")
    public ResponseEntity<?> editarTarea(
            @PathVariable Long tareaId,
            @RequestBody CreateTareaRequest request) {
        try {
            Tarea tarea = tareaService.editarTarea(
                    tareaId,
                    request.getUsuarioId(),
                    request.getNombre(),
                    request.getDescripcion(),
                    request.getPrioridad(),
                    request.getFechaLimite(),
                    request.getUsuarioAsignadoId()
            );
            return ResponseEntity.ok(mapToResponse(tarea));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    // Método auxiliar para mapear Tarea a TareaResponse
    private TareaResponse mapToResponse(Tarea tarea) {
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