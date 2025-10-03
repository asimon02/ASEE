package com.ondra.users.usuarios_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para login con Google OAuth.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginGoogleDTO {

    @NotBlank(message = "El token de Google es obligatorio")
    private String idToken;
}