package com.sistema.tareas_domesticas.repository;

import com.sistema.tareas_domesticas.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    /**
     * HU-07: Listar todas las tareas asociadas a un hogar específico.
     * SELECT * FROM tareas WHERE hogar_id = ?
     */
    List<Tarea> findByHogarId(Long hogarId);

    /**
     * HU-09: Listar tareas asignadas a un usuario específico.
     * Ahora que agregamos 'usuarioAsignadoId' a Tarea.java,
     * Spring podrá generar esta consulta sin errores.
     */
    List<Tarea> findByUsuarioAsignadoId(Long usuarioAsignadoId);

    /**
     * HU-07: Buscar tareas por estado dentro de un hogar.
     * Útil para filtrar en el Dashboard entre PENDIENTE y COMPLETADA.
     */
    List<Tarea> findByHogarIdAndEstado(Long hogarId, String estado);

    /**
     * Opcional: Útil para validar si un usuario tiene tareas pendientes
     * antes de ser eliminado del hogar.
     */
    List<Tarea> findByUsuarioAsignadoIdAndEstado(Long usuarioAsignadoId, String estado);

    List<Tarea> findByHogarIdAndEstadoOrderByFechaLimiteDesc(Long hogarId, String estado);
}
