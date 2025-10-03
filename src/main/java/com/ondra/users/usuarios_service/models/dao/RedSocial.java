package com.ondra.users.usuarios_service.models.dao;

import com.ondra.users.usuarios_service.models.enums.TipoRedSocial;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RedesSociales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RedSocial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRedSocial;

    @ManyToOne
    @JoinColumn(name = "id_artista", nullable = false)
    private Artista artista;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoRedSocial tipoRedSocial;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String urlRedSocial;
}