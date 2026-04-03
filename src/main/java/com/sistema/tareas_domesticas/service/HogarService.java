package com.sistema.tareas_domesticas.service;

import com.sistema.tareas_domesticas.model.Hogar;
import com.sistema.tareas_domesticas.model.Usuario;
import com.sistema.tareas_domesticas.repository.HogarRepository;
import com.sistema.tareas_domesticas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HogarService {

    @Autowired
    private HogarRepository hogarRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Hogar crearHogar(Long usuarioId, String nombreHogar) {
        if (nombreHogar == null || nombreHogar.isBlank()) {
            throw new RuntimeException("Nombre de hogar inválido");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getFamiliaId() != null) {
            throw new RuntimeException("Ya perteneces a un hogar");
        }

        Hogar hogar = new Hogar();
        hogar.setNombre(nombreHogar);
        hogar.setCodigoInvitacion(generarCodigoUnico());

        Hogar hogarGuardado = hogarRepository.save(hogar);

        usuario.setFamiliaId(hogarGuardado.getId());
        usuario.setRol("ADMINISTRADOR");
        usuarioRepository.save(usuario);

        return hogarGuardado;
    }

    private String generarCodigoUnico() {
        String codigo;
        do {
            codigo = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        } while (hogarRepository.findByCodigoInvitacion(codigo).isPresent());
        return codigo;
    }
}
