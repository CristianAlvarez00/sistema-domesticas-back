package com.sistema.tareas_domesticas.controller;

import com.sistema.tareas_domesticas.model.HogarResponse;
import com.sistema.tareas_domesticas.model.Usuario; // Ajusta según tu paquete de modelos
import com.sistema.tareas_domesticas.repository.UsuarioRepository; // Ajusta según tus repositorios
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hogares")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.PATCH,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS
})
public class HogarController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Endpoint para obtener los miembros pertenecientes al hogar del usuario actual.
     * Resuelve el error 404 (Not Found) al ingresar al Dashboard.
     */
    @GetMapping("/miembros")
    public ResponseEntity<List<Usuario>> obtenerMiembrosPorHogar(@RequestParam Long usuarioId) {
        try {
            // 1. Buscar al usuario consultante para saber a qué hogar pertenece
            Usuario usuarioAdmin = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Long familiaId = usuarioAdmin.getFamiliaId();

            if (familiaId == null) {
                // Si el administrador aún no crea un hogar, devolvemos una lista vacía o solo a él
                return ResponseEntity.ok(List.of(usuarioAdmin));
            }

            // 2. Buscar a todos los usuarios que compartan ese mismo id de familia/hogar
            List<Usuario> miembros = usuarioRepository.findByFamiliaId(familiaId);

            return ResponseEntity.ok(miembros);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}