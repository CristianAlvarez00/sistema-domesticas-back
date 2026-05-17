package com.sistema.tareas_domesticas.service;

import com.sistema.tareas_domesticas.model.Tarea;
import com.sistema.tareas_domesticas.model.Usuario;
import com.sistema.tareas_domesticas.repository.TareaRepository;
import com.sistema.tareas_domesticas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * HU-06: Crear una nueva tarea.
     * Valida que el creador sea ADMINISTRADOR y pertenezca a un hogar.
     */
    public Tarea crearTarea(Long usuarioId, String nombre, String descripcion, String prioridad, LocalDate fechaLimite) {
        // 1. Validar campos obligatorios
        if (nombre == null || nombre.isBlank()) {
            throw new RuntimeException("El nombre de la tarea es obligatorio");
        }
        if (prioridad == null || prioridad.isBlank()) {
            throw new RuntimeException("La prioridad de la tarea es obligatoria");
        }
        if (fechaLimite == null) {
            throw new RuntimeException("La fecha límite de la tarea es obligatoria");
        }

        // 2. Validar fecha límite no anterior a hoy
        if (fechaLimite.isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha límite no puede ser anterior a hoy");
        }

        // 3. Validar que el usuario exista y tenga permisos
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!"ADMINISTRADOR".equals(usuario.getRol())) {
            throw new RuntimeException("Solo los administradores pueden crear tareas");
        }

        if (usuario.getFamiliaId() == null) {
            throw new RuntimeException("El usuario no pertenece a ningún hogar");
        }

        // 4. Mapear y guardar tarea
        Tarea tarea = new Tarea();
        tarea.setNombre(nombre);
        tarea.setDescripcion(descripcion);
        tarea.setPrioridad(prioridad.toUpperCase());
        tarea.setFechaLimite(fechaLimite);
        tarea.setEstado("PENDIENTE"); // Estado inicial
        tarea.setHogarId(usuario.getFamiliaId()); // Vinculación automática al hogar

        return tareaRepository.save(tarea);
    }

    /**
     * HU-07: Listar tareas del hogar.
     * Este método es indispensable para el endpoint en TareaController.
     */
    public List<Tarea> listarTareasPorHogar(Long hogarId) {
        if (hogarId == null) {
            throw new RuntimeException("El ID del hogar es obligatorio para listar tareas");
        }
        return tareaRepository.findByHogarId(hogarId);
    }

    /**
     * HU-10: Eliminar una tarea.
     * Solo el ADMINISTRADOR del hogar al que pertenece la tarea puede eliminarla.
     */
    public void eliminarTarea(Long tareaId, Long usuarioId) {
        // 1. Validar que la tarea existe
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        // 2. Validar que el usuario existe y es ADMINISTRADOR
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!"ADMINISTRADOR".equals(usuario.getRol())) {
            throw new RuntimeException("Solo los administradores pueden eliminar tareas");
        }

        // 3. Validar que la tarea pertenece al mismo hogar del administrador
        if (!tarea.getHogarId().equals(usuario.getFamiliaId())) {
            throw new RuntimeException("No tienes permisos para eliminar esta tarea");
        }

        // 4. Eliminar
        tareaRepository.deleteById(tareaId);
    }

    /**
     * HU-11: Cambiar el estado de una tarea.
     * Transiciones permitidas:
     *   PENDIENTE -> EN_PROGRESO
     *   EN_PROGRESO -> COMPLETADA
     *   EN_PROGRESO -> PENDIENTE  (revertir)
     * Al completar se registra automáticamente la fecha y hora de finalización.
     * Solo el usuario asignado a la tarea puede cambiar su estado.
     */
    public Tarea cambiarEstado(Long tareaId, Long usuarioId, String nuevoEstado) {
        // 1. Validar que la tarea existe
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        // 2. Validar que el usuario existe
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Validar que el usuario es responsable de la tarea
        if (!usuarioId.equals(tarea.getUsuarioAsignadoId())) {
            throw new RuntimeException("Solo el responsable de la tarea puede cambiar su estado");
        }

        // 4. Validar transición de estados
        String estadoActual = tarea.getEstado();
        validarTransicion(estadoActual, nuevoEstado);

        // 5. Aplicar cambio de estado
        tarea.setEstado(nuevoEstado);

        // 6. Si se completa, registrar fecha y hora de finalización
        if ("COMPLETADA".equals(nuevoEstado)) {
            tarea.setFechaCompletada(LocalDateTime.now());
        } else {
            // Si se revierte a otro estado, limpiar la fecha
            tarea.setFechaCompletada(null);
        }

        return tareaRepository.save(tarea);
    }

    /**
     * Valida que la transición de estado sea permitida por las reglas de negocio.
     */
    private void validarTransicion(String estadoActual, String nuevoEstado) {
        boolean valida = switch (estadoActual) {
            case "PENDIENTE"    -> "EN_PROGRESO".equals(nuevoEstado);
            case "EN_PROGRESO"  -> "COMPLETADA".equals(nuevoEstado) || "PENDIENTE".equals(nuevoEstado);
            case "COMPLETADA"   -> false; // Una tarea completada no puede cambiar de estado
            default -> false;
        };

        if (!valida) {
            throw new RuntimeException(
                "Transición de estado no permitida: " + estadoActual + " -> " + nuevoEstado
            );
        }
    }
}