package com.sistema.tareas_domesticas.service;

import com.sistema.tareas_domesticas.model.Hogar;
import com.sistema.tareas_domesticas.model.Usuario;
import com.sistema.tareas_domesticas.repository.HogarRepository;
import com.sistema.tareas_domesticas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

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
    //Anadido para HU4.
    @Transactional //Si ocurre algún error,que la transacción o el proceso se revierta (no quedar incompleto)
    public void unirseHogar(String codigo,long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new RuntimeException("Usuario no encontrado")); //En caso de que el id recibido sea incorrecto o no este dentro de la db.;;
        if (usuario.getFamiliaId() != null) { //Validación en caso de que el usuario ya pertenezca a un hogar.
            throw new RuntimeException("Ya perteneces a un hogar");
        }

        Hogar hogar = hogarRepository.findByCodigoInvitacion(codigo).orElseThrow(() -> new RuntimeException("Código de invitación inválido")); //En caso de que la invitacion digitada no corresponda a ningun hogar en la db.
        usuario.setFamiliaId(hogar.getId()); //Actualización del id de la familia a la que pertenece (antes null)
        usuario.setRol("MIEMBRO");
        usuarioRepository.save(usuario);
        }

}

