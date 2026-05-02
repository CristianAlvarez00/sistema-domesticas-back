package com.sistema.tareas_domesticas.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sistema.tareas_domesticas.model.Tarea;
import com.sistema.tareas_domesticas.service.TareaService;

@RestController
@RequestMapping("/api/tareas")
@CrossOrigin(origins = "*")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @GetMapping("/hogar/{hogarId}")
    public ResponseEntity<?> listarTareas(
            @PathVariable Long hogarId,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long miembroId,
            @RequestParam(defaultValue = "0") int pagina) {

        Page<Tarea> tareas = tareaService.listarTareas(hogarId, estado, miembroId, pagina);

        if (tareas.isEmpty()) {
            return ResponseEntity.ok(Collections.singletonMap("mensaje", "No hay tareas registradas para este hogar."));
        }

        return ResponseEntity.ok(tareas);
    }
}