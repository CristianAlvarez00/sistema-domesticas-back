package com.sistema.tareas_domesticas.controller;

import com.sistema.tareas_domesticas.model.Usuario;
import com.sistema.tareas_domesticas.service.MiembroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hogares")
@CrossOrigin(origins = "*")
public class MiembroController {

    @Autowired
    private MiembroService miembroService;

    /**
     * HU-05 – Escenario 1: Ver lista de miembros del hogar
     * GET /api/hogares/{hogarId}/miembros
     * Solo el administrador debería llamar este endpoint desde el front.
     */
    @GetMapping("/{hogarId}/miembros")
    public ResponseEntity<List<Usuario>> listarMiembros(@PathVariable Long hogarId) {
        List<Usuario> miembros = miembroService.listarMiembros(hogarId);
        return ResponseEntity.ok(miembros);
    }

    /**
     * HU-05 – Escenario 2 y 3: Eliminar miembro del hogar
     * DELETE /api/hogares/{hogarId}/miembros/{usuarioId}
     * Param: adminId (quien hace la petición) para validar auto-eliminación.
     */
    @DeleteMapping("/{hogarId}/miembros/{usuarioId}")
    public ResponseEntity<Map<String, String>> eliminarMiembro(
            @PathVariable Long hogarId,
            @PathVariable Long usuarioId,
            @RequestParam Long adminId) {

        miembroService.eliminarMiembro(hogarId, usuarioId, adminId);
        return ResponseEntity.ok(Map.of("mensaje", "Miembro eliminado exitosamente"));
    }
}
