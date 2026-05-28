package com.sistema.tareas_domesticas.controller;

import com.sistema.tareas_domesticas.model.CreateTareaRequest;
import com.sistema.tareas_domesticas.model.Tarea;
import com.sistema.tareas_domesticas.model.TareaResponse;
import com.sistema.tareas_domesticas.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tareas")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.PATCH,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS
})
public class TareaController {

    @Autowired
    private TareaService tareaService;

    /**
     * HU-06: Crear una nueva tarea
     * Devuelve el TareaResponse completo requerido por la interfaz de React.
     */
    @PostMapping("/crear")
    public ResponseEntity<TareaResponse> crearTarea(@RequestBody CreateTareaRequest request) {
        try {
            Tarea tarea = tareaService.crearTarea(
                    request.getUsuarioId(),
                    request.getNombre(),
                    request.getDescripcion(),
                    request.getPrioridad(),
                    request.getFechaLimite()
            );

            TareaResponse response = new TareaResponse(
                    tarea.getId(),
                    tarea.getNombre(),
                    tarea.getDescripcion(),
                    request.getPrioridad(),
                    tarea.getFechaLimite(),
                    tarea.getEstado(),
                    tarea.getHogarId(),
                    null
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * HU-07: Listar tareas del hogar con Alerta calculada dinámicamente
     */
    @GetMapping("/hogar/{hogarId}")
    public List<TareaResponse> listarTareasPorHogar(@PathVariable Long hogarId) {
        LocalDate hoy = LocalDate.now();

        return tareaService.listarTareasPorHogar(hogarId).stream()
                .map(t -> {
                    String alertaCalculada = null;
                    if (!"COMPLETADA".equalsIgnoreCase(t.getEstado()) && t.getFechaLimite() != null) {
                        if (t.getFechaLimite().isBefore(hoy)) {
                            alertaCalculada = "VENCIDA";
                        } else if (t.getFechaLimite().isEqual(hoy)) {
                            alertaCalculada = "URGENTE";
                        } else {
                            alertaCalculada = "PENDIENTE";
                        }
                    }

                    return new TareaResponse(
                            t.getId(),
                            t.getNombre(),
                            t.getDescripcion(),
                            t.getPrioridad(),
                            t.getFechaLimite(),
                            t.getEstado(),
                            t.getHogarId(),
                            alertaCalculada
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * HU-13: Asignar un usuario a una tarea específica
     * Retorna una respuesta limpia y exitosa de tipo Void que Axios procesa sin problemas.
     */
    @PutMapping("/{taskId}/asignar/{userId}")
    public ResponseEntity<Void> asignarTarea(@PathVariable Long taskId, @PathVariable Long userId) {
        try {
            tareaService.asignarTareaAUser(taskId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * HU-11/12: Actualizar el estado de una tarea (PENDIENTE, EN_PROCESO, COMPLETADA)
     */
    @PatchMapping("/{taskId}/estado")
    public ResponseEntity<Void> cambiarEstadoTarea(
            @PathVariable Long taskId,
            @RequestParam Long usuarioId,
            @RequestParam String nuevoEstado) {
        try {
            tareaService.cambiarEstadoTarea(taskId, usuarioId, nuevoEstado);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * HU-14: Historial de tareas completadas
     */
    @GetMapping("/hogar/{hogarId}/historial")
    public List<TareaResponse> obtenerHistorial(@PathVariable Long hogarId) {
        return tareaService.obtenerHistorialPorHogar(hogarId).stream()
                .map(t -> new TareaResponse(
                        t.getId(),
                        t.getNombre(),
                        t.getDescripcion(),
                        t.getPrioridad(),
                        t.getFechaLimite(),
                        t.getEstado(),
                        t.getHogarId(),
                        null
                ))
                .collect(Collectors.toList());
    }

    /**
     * Endpoint para eliminar tareas del sistema
     * Captura el taskId de la URL y el usuarioId enviado como Query Parameter por React.
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> eliminarTarea(
            @PathVariable Long taskId,
            @RequestParam Long usuarioId) {
        try {
            // Nota: Verifica que el método de tu Service se llame exactamente 'eliminarTarea'
            tareaService.eliminarTarea(taskId, usuarioId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}