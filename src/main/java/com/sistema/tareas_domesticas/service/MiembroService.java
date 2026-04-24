package com.sistema.tareas_domesticas.service;

import com.sistema.tareas_domesticas.model.Usuario;
import com.sistema.tareas_domesticas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiembroService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * HU-05 – Escenario 1: Retorna todos los usuarios que pertenecen al hogar.
     */
    public List<Usuario> listarMiembros(Long hogarId) {
        return usuarioRepository.findByFamiliaId(hogarId);
    }

    /**
     * HU-05 – Escenario 2 y 3: Elimina un miembro del hogar.
     * Reglas:
     *   - El administrador no puede eliminarse a sí mismo.
     *   - El usuario debe pertenecer al hogar indicado.
     */
    public void eliminarMiembro(Long hogarId, Long usuarioId, Long adminId) {
        // Escenario 3: Intento de auto-eliminación
        if (usuarioId.equals(adminId)) {
            throw new RuntimeException("No puedes eliminarte a ti mismo");
        }

        // Verificar que el miembro pertenece a este hogar
        Usuario miembro = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!hogarId.equals(miembro.getFamiliaId())) {
            throw new RuntimeException("El usuario no pertenece a este hogar");
        }

        // Escenario 2: Eliminar — se desvincula del hogar (familiaId = null)
        // y su rol vuelve a MIEMBRO sin hogar asignado.
        miembro.setFamiliaId(null);
        miembro.setRol("MIEMBRO");
        usuarioRepository.save(miembro);
    }
}
