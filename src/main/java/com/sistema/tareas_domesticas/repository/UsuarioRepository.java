package com.sistema.tareas_domesticas.repository;

import com.sistema.tareas_domesticas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Spring entiende que quieres buscar por el campo "email" automáticamente
    Optional<Usuario> findByEmail(String email);
}