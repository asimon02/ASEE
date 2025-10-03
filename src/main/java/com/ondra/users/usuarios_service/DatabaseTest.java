package com.ondra.users.usuarios_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void testConnection() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            System.out.println("✅ Conexión a la base de datos OK! Resultado de SELECT 1: " + result);
        } catch (Exception e) {
            System.err.println("❌ Error al conectarse a la base de datos: " + e.getMessage());
        }
    }
}