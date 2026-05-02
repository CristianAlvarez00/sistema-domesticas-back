package com.sistema.tareas_domesticas.service;

import com.sistema.tareas_domesticas.model.Tarea;
import com.sistema.tareas_domesticas.model.Usuario;
import com.sistema.tareas_domesticas.repository.TareaRepository;
import com.sistema.tareas_domesticas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Tarea crearTarea(Long usuarioId, String nombre, String descripcion, String prioridad, LocalDate fechaLimite) {
        // Validar campos obligatorios
        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("El nombre de la tarea es obligatorio");
        }
        if (prioridad == null || prioridad.isBlank()) {
            throw new RuntimeException("La prioridad de la tarea es obligatoria");
        }
        if (fechaLimite == null) {
            throw new RuntimeException("La fecha límite de la tarea es obligatoria");
        }

        // Validar fecha límite no anterior a hoy
        if (fechaLimite.isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha límite no puede ser anterior a hoy");
        }

        // Validar que el usuario sea administrador y pertenezca a un hogar
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!"ADMINISTRADOR".equals(usuario.getRol())) {
            throw new RuntimeException("Solo los administradores pueden crear tareas");
        }

        if (usuario.getFamiliaId() == null) {
            throw new RuntimeException("El usuario no pertenece a ningún hogar");
        }

        // Crear tarea
        Tarea tarea = new Tarea();
        tarea.setNombre(nombre);
        tarea.setDescripcion(descripcion);
        tarea.setPrioridad(prioridad.toUpperCase());
        tarea.setFechaLimite(fechaLimite);
        tarea.setEstado("Pendiente");
        tarea.setHogarId(usuario.getFamiliaId());

        return tareaRepository.save(tarea);
    }
}