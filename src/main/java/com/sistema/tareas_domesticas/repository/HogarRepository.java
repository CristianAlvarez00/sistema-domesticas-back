package com.sistema.tareas_domesticas.repository;

import com.sistema.tareas_domesticas.model.Hogar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HogarRepository extends JpaRepository<Hogar, Long> {
    Optional<Hogar> findByCodigoInvitacion(String codigoInvitacion);
}
