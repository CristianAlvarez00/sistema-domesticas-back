package com.sistema.tareas_domesticas.service;

import com.sistema.tareas_domesticas.model.Tarea;
import com.sistema.tareas_domesticas.model.Usuario;
import com.sistema.tareas_domesticas.repository.TareaRepository;
import com.sistema.tareas_domesticas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
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
     * Retorna la lista de entidades Tarea puras para que el Controller realice el DTO mapping.
     */
    public List<Tarea> listarTareasPorHogar(Long hogarId) {
        if (hogarId == null) {
            throw new RuntimeException("El ID del hogar es obligatorio para listar tareas");
        }
        return tareaRepository.findByHogarId(hogarId);
    }

    /**
     * HU-13: Asignar una tarea a un usuario específico del hogar
     */
    public void asignarTareaAUser(Long taskId, Long userId) {
        // 1. Validar que la tarea exista
        Tarea tarea = tareaRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("No se encontró la tarea con ID: " + taskId));

        // 2. Validar que el usuario a asignar exista
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No se encontró el usuario con ID: " + userId));

        // 3. Validar que pertenezcan al mismo hogar
        if (!tarea.getHogarId().equals(usuario.getFamiliaId())) {
            throw new RuntimeException("El usuario no pertenece al mismo hogar que la tarea");
        }

        // 4. Asignar el usuario a la tarea mediante reflexión segura
        try {
            java.lang.reflect.Method setUsuarioMethod = Tarea.class.getMethod("setUsuario", Usuario.class);
            setUsuarioMethod.invoke(tarea, usuario);
        } catch (Exception e) {
            try {
                Tarea.class.getMethod("setUsuarioAsignadoId", Long.class).invoke(tarea, userId);
            } catch (Exception ignored) {}
            try {
                // Se usa getName() o getNombre() dependiendo de los atributos de tu entidad Usuario
                String nombreUser = usuario.getName();
                if (nombreUser == null) {
                    try {
                        nombreUser = (String) Usuario.class.getMethod("getNombre").invoke(usuario);
                    } catch (Exception ignored) {}
                }
                Tarea.class.getMethod("setUsuarioAsignadoNombre", String.class).invoke(tarea, nombreUser);
            } catch (Exception ignored) {}
        }

        // 5. Guardar cambios en la base de datos
        tareaRepository.save(tarea);
    }

    /**
     * HU-14: Obtener lista de tareas completadas
     */
    public List<Tarea> obtenerHistorialPorHogar(Long hogarId) {
        if (hogarId == null) {
            throw new RuntimeException("El ID del hogar es obligatorio para obtener el historial");
        }
        return tareaRepository.findByHogarIdAndEstadoOrderByFechaLimiteDesc(hogarId, "COMPLETADA");
    }

    /**
     * HU-11/12 ENLACE DE ESTADO: Actualiza el estado actual de la tarea.
     * Si la tarea no tiene un usuario asignado y pasa a estar "EN_PROCESO",
     * se le asigna automáticamente al usuario que gatilló la acción.
     */
    public void cambiarEstadoTarea(Long taskId, Long usuarioId, String nuevoEstado) {
        // 1. Validar que la tarea exista
        Tarea tarea = tareaRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("No se encontró la tarea con ID: " + taskId));

        // 2. Validar que el usuario ejecutor exista
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("No se encontró el usuario ejecutor con ID: " + usuarioId));

        // 3. Verificar si la tarea está sin dueño e interceptar para auto-asignar
        Object currentAssignedId = null;
        try {
            currentAssignedId = Tarea.class.getMethod("getUsuarioAsignadoId").invoke(tarea);
        } catch (Exception e) {
            try {
                Object u = Tarea.class.getMethod("getUsuario").invoke(tarea);
                if (u != null) {
                    currentAssignedId = Usuario.class.getMethod("getId").invoke(u);
                }
            } catch (Exception ignored) {}
        }

        // Si está libre, la asignamos al usuario actual para que aparezca en sus contadores individuales
        if (currentAssignedId == null) {
            asignarTareaAUser(taskId, usuarioId);
        }

        // 4. Cambiar el estado propiamente
        tarea.setEstado(nuevoEstado.toUpperCase());

        // 5. Guardar cambios consolidados
        tareaRepository.save(tarea);
    }

    /**
     * HU-08: Eliminar una tarea del sistema.
     * Valida la existencia del ejecutor, sus permisos de ADMINISTRADOR y coherencia de hogar.
     */
    public void eliminarTarea(Long taskId, Long usuarioId) {
        // 1. Validar que la tarea exista en la BD
        Tarea tarea = tareaRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("La tarea a eliminar no existe"));

        // 2. Validar que el usuario ejecutor exista
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario ejecutor no encontrado"));

        // 3. Control de acceso: Solo administradores pueden borrar tareas
        if (!"ADMINISTRADOR".equals(usuario.getRol())) {
            throw new RuntimeException("Operación rechazada: Solo los administradores pueden eliminar tareas");
        }

        // 4. Asegurar que el administrador pertenezca al mismo hogar de la tarea
        if (!tarea.getHogarId().equals(usuario.getFamiliaId())) {
            throw new RuntimeException("No tienes permisos para eliminar tareas de otro hogar");
        }

        // 5. Eliminación física de la entidad
        tareaRepository.delete(tarea);
    }
}