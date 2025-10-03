package com.ondra.users.usuarios_service.models.dao;

import com.ondra.users.usuarios_service.models.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false)
    private String nombreUsuario;

    private String apellidosUsuario;

    @Column(nullable = false, unique = true)
    private String emailUsuario;

    @Column
    private String passwordUsuario; // Aquí guardaremos el hash

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario; // normal o artista

    private String fotoPerfil;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(nullable = false)
    private boolean activo = true;

    // Google OAuth
    @Column(unique = true)
    private String googleUid;

    @Column(nullable = false)
    private boolean permiteGoogle = true;
}