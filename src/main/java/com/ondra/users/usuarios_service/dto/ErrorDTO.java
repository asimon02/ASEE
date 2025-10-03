package com.ondra.users.usuarios_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDTO {
    private String codigo;
    private String mensaje;
    private String timestamp;
    private Integer statusCode;
}