package com.sistema.tareas_domesticas.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema.tareas_domesticas.model.Tarea;
import com.sistema.tareas_domesticas.model.Usuario;
import com.sistema.tareas_domesticas.repository.TareaRepository;
import com.sistema.tareas_domesticas.repository.UsuarioRepository;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * HU-07: Crear una nueva tarea.
     */
    public Tarea crearTarea(Long usuarioId, String nombre, String descripcion, String prioridad, LocalDate fechaLimite) {
        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("El nombre de la tarea es obligatorio");
        }
        if (prioridad == null || prioridad.isBlank()) {
            throw new RuntimeException("La prioridad de la tarea es obligatoria");
        }
        if (fechaLimite == null) {
            throw new RuntimeException("La fecha límite de la tarea es obligatoria");
        }
        if (fechaLimite.isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha límite no puede ser anterior a hoy");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!"ADMINISTRADOR".equals(usuario.getRol())) {
            throw new RuntimeException("Solo los administradores pueden crear tareas");
        }

        if (usuario.getFamiliaId() == null) {
            throw new RuntimeException("El usuario no pertenece a ningún hogar");
        }

        Tarea tarea = new Tarea();
        tarea.setNombre(nombre);
        tarea.setDescripcion(descripcion);
        tarea.setPrioridad(prioridad.toUpperCase());
        tarea.setFechaLimite(fechaLimite);
        tarea.setEstado("PENDIENTE");
        tarea.setHogarId(usuario.getFamiliaId());

        return tareaRepository.save(tarea);
    }

    /**
     * HU-08: Listar tareas del hogar con filtro opcional por estado.
     * Escenario 1: Sin filtro -> retorna todas las tareas del hogar
     * Escenario 2: Con filtro -> retorna solo las tareas del estado indicado
     * Escenario 3: Sin tareas -> retorna lista vacía (el controller maneja el mensaje)
     */
    public List<Tarea> listarTareasPorHogar(Long hogarId, String estado) {
        if (hogarId == null) {
            throw new RuntimeException("El ID del hogar es obligatorio para listar tareas");
        }
        if (estado != null && !estado.isBlank()) {
            return tareaRepository.findByHogarIdAndEstado(hogarId, estado.toUpperCase());
        }
        return tareaRepository.findByHogarId(hogarId);
    }

    /**
     * HU-09: Editar una tarea existente.
     * Escenario 1: Edición exitosa si la tarea no está completada
     * Escenario 2: No se puede editar si está COMPLETADA
     * Escenario 3: Solo el ADMINISTRADOR puede editar
     */
    public Tarea editarTarea(Long tareaId, Long usuarioId, String nombre, String descripcion,
                              String prioridad, LocalDate fechaLimite, Long usuarioAsignadoId) {

        // Validar que el usuario sea administrador
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!"ADMINISTRADOR".equals(usuario.getRol())) {
            throw new RuntimeException("Acceso denegado: solo los administradores pueden editar tareas");
        }

        // Buscar la tarea
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        // No se puede editar una tarea completada
        if ("COMPLETADA".equals(tarea.getEstado())) {
            throw new RuntimeException("No se puede editar una tarea completada");
        }

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

        // Aplicar cambios
        tarea.setNombre(nombre);
        tarea.setDescripcion(descripcion);
        tarea.setPrioridad(prioridad.toUpperCase());
        tarea.setFechaLimite(fechaLimite);
        tarea.setUsuarioAsignadoId(usuarioAsignadoId);

        return tareaRepository.save(tarea);
    }
}