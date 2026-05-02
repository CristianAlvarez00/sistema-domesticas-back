package com.sistema.tareas_domesticas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sistema.tareas_domesticas.model.Tarea;
import com.sistema.tareas_domesticas.repository.TareaRepository;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    public Page<Tarea> listarTareas(Long hogarId, String estado, Long miembroId, int pagina) {
        Pageable pageable = PageRequest.of(pagina, 10);

        if (estado != null && miembroId != null) {
            return tareaRepository.findByHogarIdAndEstadoAndMiembroAsignadoId(hogarId, estado, miembroId, pageable);
        } else if (estado != null) {
            return tareaRepository.findByHogarIdAndEstado(hogarId, estado, pageable);
        } else if (miembroId != null) {
            return tareaRepository.findByHogarIdAndMiembroAsignadoId(hogarId, miembroId, pageable);
        } else {
            return tareaRepository.findByHogarId(hogarId, pageable);
        }
    }
}