package com.sistema.tareas_domesticas.controller;

import com.sistema.tareas_domesticas.model.CreateHogarRequest;
import com.sistema.tareas_domesticas.model.Hogar;
import com.sistema.tareas_domesticas.model.HogarResponse;
import com.sistema.tareas_domesticas.service.HogarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sistema.tareas_domesticas.model.UnirseHogarRequest;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/hogares")
@CrossOrigin(origins = "*")
public class HogarController {

    @Autowired
    private HogarService hogarService;

    @PostMapping("/crear")
    public HogarResponse crearHogar(@RequestBody CreateHogarRequest request) {
        Hogar hogar = hogarService.crearHogar(request.getUsuarioId(), request.getNombre());
        return new HogarResponse(hogar.getId(), hogar.getNombre(), hogar.getCodigoInvitacion());
    }
    @PostMapping("/unirse")
    public ResponseEntity<String> unirse(@RequestBody UnirseHogarRequest request){
        try {
            hogarService.unirseHogar(request.getCodigo(), request.getUsuarioId());
            return ResponseEntity.ok("Te has unido al hogar exitosamente");
        } catch(RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
