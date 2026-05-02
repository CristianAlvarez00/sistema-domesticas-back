package com.sistema.tareas_domesticas.repository;

import com.sistema.tareas_domesticas.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByHogarId(Long hogarId);
}