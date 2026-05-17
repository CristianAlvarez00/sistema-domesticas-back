package com.sistema.tareas_domesticas.controller;

import com.sistema.tareas_domesticas.model.CreateTareaRequest;
import com.sistema.tareas_domesticas.model.Tarea;
import com.sistema.tareas_domesticas.model.TareaResponse;
import com.sistema.tareas_domesticas.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tareas")
@CrossOrigin(origins = "*")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    /**
     * HU-06: Crear una nueva tarea
     * Este endpoint recibe los datos del formulario del front y delega al service.
     */
    @PostMapping("/crear")
    public TareaResponse crearTarea(@RequestBody CreateTareaRequest request) {
        // Llamada al servicio con los campos del request
        Tarea tarea = tareaService.crearTarea(
                request.getUsuarioId(),
                request.getNombre(),
                request.getDescripcion(),
                request.getPrioridad(),
                request.getFechaLimite()
        );

        // Retornamos la respuesta mapeada al DTO TareaResponse
        return mapToResponse(tarea);
    }

    /**
     * HU-07: Listar tareas del hogar (Adelanto para dejar el controller listo)
     * GET /api/tareas/hogar/{hogarId}
     */
    @GetMapping("/hogar/{hogarId}")
    public List<TareaResponse> listarTareasPorHogar(@PathVariable Long hogarId) {
        return tareaService.listarTareasPorHogar(hogarId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * HU-10: Eliminar una tarea.
     * DELETE /api/tareas/{id}?usuarioId={usuarioId}
     * Solo el ADMINISTRADOR del hogar puede eliminar tareas.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarTarea(
            @PathVariable Long id,
            @RequestParam Long usuarioId) {
        tareaService.eliminarTarea(id, usuarioId);
        return ResponseEntity.ok(Map.of("mensaje", "Tarea eliminada exitosamente"));
    }

    /**
     * HU-11: Cambiar el estado de una tarea.
     * PATCH /api/tareas/{id}/estado
     * Body: { "usuarioId": 1, "estado": "EN_PROGRESO" }
     * Solo el responsable asignado puede cambiar el estado.
     */
    @PatchMapping("/{id}/estado")
    public TareaResponse cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        Long usuarioId = Long.valueOf(body.get("usuarioId").toString());
        String nuevoEstado = body.get("estado").toString();
        Tarea tarea = tareaService.cambiarEstado(id, usuarioId, nuevoEstado);
        return mapToResponse(tarea);
    }

    /**
     * Método auxiliar para mapear Tarea -> TareaResponse.
     */
    private TareaResponse mapToResponse(Tarea t) {
        return new TareaResponse(
                t.getId(),
                t.getNombre(),
                t.getDescripcion(),
                t.getPrioridad(),
                t.getFechaLimite(),
                t.getEstado(),
                t.getHogarId(),
                t.getFechaCompletada()
        );
    }
}