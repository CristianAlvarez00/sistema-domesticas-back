package com.sistema.tareas_domesticas.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.tareas_domesticas.model.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    Page<Tarea> findByHogarId(Long hogarId, Pageable pageable);

    Page<Tarea> findByHogarIdAndEstado(Long hogarId, String estado, Pageable pageable);

    Page<Tarea> findByHogarIdAndMiembroAsignadoId(Long hogarId, Long miembroId, Pageable pageable);

    Page<Tarea> findByHogarIdAndEstadoAndMiembroAsignadoId(Long hogarId, String estado, Long miembroId, Pageable pageable);
}