package com.ondra.users.usuarios_service.models.dao;

import com.ondra.users.usuarios_service.models.enums.EstadoPago;
import com.ondra.users.usuarios_service.models.enums.MetodoPago;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PagosArtistas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoArtista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPagoArtista;

    @ManyToOne
    @JoinColumn(name = "id_artista", nullable = false)
    private Artista artista;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPagoArtista;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String tokenPagoArtista;

    private String propietarioPagoArtista;
    private String ibanPagoArtista;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaPagoArtista = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estadoPagoArtista = EstadoPago.PENDIENTE;
}