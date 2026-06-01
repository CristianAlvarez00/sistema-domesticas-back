package com.sistema.tareas_domesticas.controller;

import com.sistema.tareas_domesticas.model.Usuario;
import com.sistema.tareas_domesticas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController // Define que esto es una API REST y convierte lo que salga en JSON.
@RequestMapping("/api/usuarios") // Ruta base del controller
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS
})
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService; // Inyectamos una instancia de UsuarioService

    @PostMapping("/registro") // Atiende peticiones POST de registro
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioService.registrarUsuario(usuario);
    }

    @PostMapping("/login")  // Atiende peticiones POST de login
    public Usuario login(@RequestBody Usuario datosLogin) {
        Usuario usuario = usuarioService.login(datosLogin.getEmail(), datosLogin.getPassword());

        if (usuario == null) { // Si no se encontró usuario
            throw new RuntimeException("Credenciales inválidas");
        }

        return usuario; // Si todo está bien, devolvemos el usuario (JSON).
    }
}