package com.ondra.users.usuarios_service.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para editar datos del perfil de usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditarUsuarioDTO {

    @Size(min = 1, message = "El nombre no puede estar vacío")
    private String nombreUsuario;

    private String apellidosUsuario;

    private String fotoPerfil;
}