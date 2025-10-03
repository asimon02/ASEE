package com.ondra.users.usuarios_service.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Servicio para la generación y validación de tokens JWT.
 */
@Service
public class JwtService {

    @Value("${jwt.secret:TuClaveSecretaSuperSeguraDeAlMenos256BitsParaHS256Algorithm}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}") // 24 horas por defecto
    private Long jwtExpiration;

    /**
     * Genera un token JWT para un usuario.
     *
     * @param email Email del usuario
     * @param userId ID del usuario
     * @return Token JWT generado
     */
    public String generarToken(String email, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);

        return crearToken(claims, email);
    }

    /**
     * Crea el token JWT con los claims proporcionados.
     */
    private String crearToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Obtiene la clave de firma para el token.
     */
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Extrae el email (subject) del token.
     */
    public String extraerEmail(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    /**
     * Extrae el ID de usuario del token.
     */
    public Long extraerUserId(String token) {
        Claims claims = extraerTodosClaims(token);
        return claims.get("userId", Long.class);
    }

    /**
     * Extrae la fecha de expiración del token.
     */
    public Date extraerExpiracion(String token) {
        return extraerClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae un claim específico del token.
     */
    public <T> T extraerClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extraerTodosClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token.
     */
    private Claims extraerTodosClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verifica si el token ha expirado.
     */
    public Boolean isTokenExpirado(String token) {
        return extraerExpiracion(token).before(new Date());
    }

    /**
     * Valida que el token sea correcto y no haya expirado.
     */
    public Boolean validarToken(String token, String email) {
        final String tokenEmail = extraerEmail(token);
        return (tokenEmail.equals(email) && !isTokenExpirado(token));
    }
}