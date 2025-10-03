package com.ondra.users.usuarios_service.dto;

import com.ondra.users.usuarios_service.models.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO para registro de nuevo usuario con email y contraseña.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroUsuarioDTO {

    @Setter
    @Getter
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email es inválido")
    private String emailUsuario;

    @Setter
    @Getter
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String passwordUsuario;

    @Getter
    @Setter
    @NotBlank(message = "El nombre es obligatorio")
    private String nombreUsuario;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidosUsuario;

    @NotNull(message = "El tipo de usuario es obligatorio")
    private TipoUsuario tipoUsuario;

}