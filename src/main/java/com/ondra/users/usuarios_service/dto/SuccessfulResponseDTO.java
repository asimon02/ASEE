package com.ondra.users.usuarios_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuestas exitosas genéricas (ya existe en tu código).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuccessfulResponseDTO {

    private String successful;

    private String message;

    private int statusCode;

    private String timestamp;
}