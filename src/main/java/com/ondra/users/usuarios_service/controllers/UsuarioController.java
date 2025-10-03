package com.ondra.users.usuarios_service.controllers;

import com.ondra.users.usuarios_service.services.UsuarioService;
import com.ondra.users.usuarios_service.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Controlador encargado de manejar las solicitudes relacionadas con los usuarios.
 * Proporciona una API REST para realizar operaciones sobre usuarios, incluyendo
 * registro, autenticación y gestión de perfil.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UsuarioController {

    /** Servicio del controlador que gestiona los usuarios */
    private final UsuarioService usuarioService;

    // ==================== AUTENTICACIÓN ====================

    /**
     * Registra un nuevo usuario en el sistema con email y contraseña.
     *
     * @param registroDTO DTO que contiene los datos del nuevo usuario (email, password, nombre, apellidos, tipo).
     * @return ResponseEntity con el usuario creado y código 201 (CREATED).
     * @throws Exception Si el email ya existe o los datos son inválidos.
     */
    @PostMapping(value = "/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDTO> registrarUsuario(@Valid @RequestBody RegistroUsuarioDTO registroDTO) throws Exception {
        UsuarioDTO usuario = usuarioService.registrarUsuario(registroDTO);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    /**
     * Autentica un usuario con email y contraseña, retornando un JWT.
     *
     * @param loginDTO DTO con las credenciales de login (email y password).
     * @return ResponseEntity con el token JWT y los datos del usuario.
     * @throws Exception Si las credenciales son incorrectas o la cuenta está inactiva.
     */
    @PostMapping(value = "/usuarios/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDTO> loginUsuario(@Valid @RequestBody LoginUsuarioDTO loginDTO) throws Exception {
        AuthResponseDTO authResponse = usuarioService.loginUsuario(loginDTO);
        return ResponseEntity.ok(authResponse);
    }

    /**
     * Autentica o registra un usuario mediante token de Google/Firebase.
     *
     * @param loginGoogleDTO DTO que contiene el token de Google.
     * @return ResponseEntity con el token JWT y los datos del usuario.
     * @throws Exception Si el token de Google es inválido o ha expirado.
     */
    @PostMapping(value = "/usuarios/login/google", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponseDTO> loginGoogle(@Valid @RequestBody LoginGoogleDTO loginGoogleDTO) throws Exception {
        AuthResponseDTO authResponse = usuarioService.loginGoogle(loginGoogleDTO);
        return ResponseEntity.ok(authResponse);
    }

    // ==================== GESTIÓN DE USUARIOS ====================

    /**
     * Obtiene el perfil completo de un usuario por su ID.
     *
     * @param id El ID del usuario a obtener.
     * @return ResponseEntity con los datos del usuario.
     * @throws Exception Si el usuario no es encontrado.
     */
    @GetMapping(value = "/usuarios/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id, Authentication user) throws Exception {
        UsuarioDTO usuario = usuarioService.obtenerUsuario(id, user.getName());
        return ResponseEntity.ok(usuario);
    }

    /**
     * Actualiza los datos del perfil de un usuario.
     *
     * @param id El ID del usuario a actualizar.
     * @param editarDTO DTO con los datos a actualizar (nombre, apellidos, foto_perfil).
     * @return ResponseEntity con el usuario actualizado.
     * @throws Exception Si el usuario no es encontrado o los datos son inválidos.
     */
    @PutMapping(value = "/usuarios/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDTO> editarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody EditarUsuarioDTO editarDTO,
            Authentication user) throws Exception {
        UsuarioDTO usuario = usuarioService.editarUsuario(id, editarDTO, user.getName());
        return ResponseEntity.ok(usuario);
    }

    /**
     * Desactiva o elimina la cuenta de un usuario.
     *
     * @param id El ID del usuario a eliminar.
     * @return ResponseEntity con un mensaje de éxito.
     * @throws Exception Si el usuario no es encontrado.
     */
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<SuccessfulResponseDTO> eliminarUsuario(@PathVariable Long id, Authentication user) throws Exception {
        usuarioService.eliminarUsuario(id, user.getName());

        SuccessfulResponseDTO response = SuccessfulResponseDTO.builder()
                .successful("successful_user_deletion")
                .message("The user account was deleted successfully")
                .statusCode(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now().toString())
                .build();

        return ResponseEntity.ok(response);
    }
}