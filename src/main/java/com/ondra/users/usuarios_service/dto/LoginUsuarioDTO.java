package com.ondra.users.usuarios_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para login tradicional con email y contraseña.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginUsuarioDTO {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    private String emailUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    private String passwordUsuario;
}
