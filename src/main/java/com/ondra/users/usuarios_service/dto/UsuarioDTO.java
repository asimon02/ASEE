package com.ondra.users.usuarios_service.dto;

import com.ondra.users.usuarios_service.models.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para enviar información completa del usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private Long idUsuario;

    private String emailUsuario;

    private String nombreUsuario;

    private String apellidosUsuario;

    private TipoUsuario tipoUsuario;

    private String fotoPerfil;

    private LocalDateTime fechaRegistro;

    private boolean activo;

    private boolean permiteGoogle;
}