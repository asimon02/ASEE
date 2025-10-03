package com.ondra.users.usuarios_service.models.dao;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Artistas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idArtista;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false)
    private String nombreArtistico;

    @Column(columnDefinition = "TEXT")
    private String biografiaArtistico;

    private String fotoPerfilArtistico;

    @Column(nullable = false)
    private LocalDateTime fechaInicioArtistico = LocalDateTime.now();

    @Column(nullable = false)
    private boolean esTendencia = false;
}