package com.sistema.tareas_domesticas.repository;

import com.sistema.tareas_domesticas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Ya existía: buscar usuario por email (login)
    Optional<Usuario> findByEmail(String email);

    // NUEVO para HU-05: listar todos los miembros de un hogar
    List<Usuario> findByFamiliaId(Long familiaId);
}
