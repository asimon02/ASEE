package com.ondra.users.usuarios_service.models.dao;

import com.ondra.users.usuarios_service.models.enums.EstadoPago;
import com.ondra.users.usuarios_service.models.enums.MetodoPago;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PagosUsuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPagoUsuario;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPagoUsuario;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String tokenPagoUsuario;

    private String propietarioPago;
    private String ibanPago;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaPago = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estadoPago = EstadoPago.PENDIENTE;
}