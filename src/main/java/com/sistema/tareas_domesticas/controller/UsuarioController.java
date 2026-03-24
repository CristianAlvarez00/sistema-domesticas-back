package com.sistema.tareas_domesticas.controller;

import com.sistema.tareas_domesticas.model.Usuario;
import com.sistema.tareas_domesticas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController // Define que esto es una API REST y convierte lo que salga en JSON.
@RequestMapping("/api/usuarios") // Ruta base del controller
@CrossOrigin(origins = "*") // Permite que el React se conecte (CORS) dandole acceso desde cualquier servidor
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService; //inyectamos una instancia de UsuarioService,
                                            // la cual dentro de ella tiene otra instancia de UsuarioRepository

    @PostMapping("/registro") // Atiende peticiones POST de registro. la funcion se ejecuta cuando llegue
                                //peticion post desde api/usuarios/registro
    public Usuario crearUsuario(@RequestBody Usuario usuario) { //Convierte el JSON que viene en el cuerpo
                                                        // en un objeto tipo usuario y lo ingresa como parametro en la func
        return usuarioService.registrarUsuario(usuario);
    }

    @PostMapping("/login")  //Atiende peticiones post. Se ejecuta cuando llegue peticion post a /api/usuarios/login
    public Usuario login(@RequestBody Usuario datosLogin) { //Lo que reciba del react lo convierte en un objeto usuario
        Usuario usuario = usuarioService.login(datosLogin.getEmail(), datosLogin.getPassword()); //Se le pasan el email y pw obtenidas del react

        if (usuario == null) { //Si no se encontro usuario
            throw new RuntimeException("Credenciales inválidas");
        }

        return usuario; // Si todo está bien, devolvemos el usuario (JSON).
    }
}