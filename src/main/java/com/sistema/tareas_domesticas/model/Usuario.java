package com.sistema.tareas_domesticas.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity // Indica que esto es una tabla de base de datos
@Table(name = "usuarios")
@Data // Genera Getters y Setters automáticamente (Lombok)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Cuando el servidor responda, enviara todo menos la contrasena
    private String password;

    @Column(nullable = true)
    private String rol = "MIEMBRO";       // Para saber si es ADMIN o MIEMBRO

    @Column(nullable = true)
    private Long familiaId; //

}
