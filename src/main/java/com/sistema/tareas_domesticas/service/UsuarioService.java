package com.sistema.tareas_domesticas.service;

import com.sistema.tareas_domesticas.model.Usuario;
import com.sistema.tareas_domesticas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {

    @Autowired // Inyecta el repositorio automáticamente
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario registrarUsuario(Usuario usuario) {

        String passwordHasheada = passwordEncoder.encode(usuario.getPassword()); //Encriptar contraseña
        usuario.setPassword(passwordHasheada);
        return usuarioRepository.save(usuario);
    }
    public Usuario login(String email, String password) {
        // 1. Buscamos al usuario por su email
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        // 2. ¿Existe el usuario?
        if (usuario != null) {
            // 3. Usamos el encriptador para ver si la clave "encaja" con el hash
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                return usuario; // ¡Éxito!
            }
        }

        // Si no existe o la clave no coincide, devolvemos null
        return null;
    }
}