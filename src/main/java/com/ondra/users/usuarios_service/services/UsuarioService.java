package com.ondra.users.usuarios_service.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import com.ondra.users.usuarios_service.dto.*;
import com.ondra.users.usuarios_service.models.dao.Usuario;
import com.ondra.users.usuarios_service.models.enums.TipoUsuario;
import com.ondra.users.usuarios_service.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Servicio encargado de la lógica de negocio relacionada con los usuarios.
 * Gestiona registro, autenticación, obtención y edición de perfiles.
 */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final FirebaseAuth firebaseAuth;

    /**
     * Registra un nuevo usuario en el sistema con email y contraseña.
     *
     * @param registroDTO Datos del nuevo usuario
     * @return UsuarioDTO con los datos del usuario creado
     * @throws Exception Si el email ya existe
     */
    @Transactional
    public UsuarioDTO registrarUsuario(RegistroUsuarioDTO registroDTO) throws Exception {
        // Verificar si el email ya existe
        if (usuarioRepository.findByEmailUsuario(registroDTO.getEmailUsuario()).isPresent()) {
            throw new Exception("El email " + registroDTO.getEmailUsuario() + " ya está registrado");
        }

        // Crear nuevo usuario
        Usuario usuario = Usuario.builder()
                .emailUsuario(registroDTO.getEmailUsuario())
                .passwordUsuario(passwordEncoder.encode(registroDTO.getPasswordUsuario()))
                .nombreUsuario(registroDTO.getNombreUsuario())
                .apellidosUsuario(registroDTO.getApellidosUsuario())
                .tipoUsuario(TipoUsuario.valueOf(registroDTO.getTipoUsuario().toString().toUpperCase()))
                .fechaRegistro(LocalDateTime.now())
                .activo(true)
                .permiteGoogle(false)
                .build();

        // Guardar en base de datos
        usuario = usuarioRepository.save(usuario);

        // Convertir a DTO y retornar
        return convertirAUsuarioDTO(usuario);
    }

    /**
     * Autentica un usuario con email y contraseña.
     *
     * @param loginDTO Credenciales del usuario
     * @return AuthResponseDTO con token JWT y datos del usuario
     * @throws Exception Si las credenciales son incorrectas o la cuenta está inactiva
     */
    @Transactional(readOnly = true)
    public AuthResponseDTO loginUsuario(LoginUsuarioDTO loginDTO) throws Exception {
        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmailUsuario(loginDTO.getEmailUsuario())
                .orElseThrow(() -> new Exception("Email o contraseña incorrectos"));

        // Verificar que la cuenta esté activa
        if (!usuario.isActivo()) {
            throw new Exception("La cuenta está inactiva. Contacta con soporte");
        }

        // Verificar que tenga contraseña (no es cuenta de Google exclusivamente)
        if (usuario.getPasswordUsuario() == null || usuario.getPasswordUsuario().isEmpty()) {
            throw new Exception("Esta cuenta solo permite login con Google");
        }

        // Verificar la contraseña
        if (!passwordEncoder.matches(loginDTO.getPasswordUsuario(), usuario.getPasswordUsuario())) {
            throw new Exception("Email o contraseña incorrectos");
        }

        // Generar token JWT
        String token = jwtService.generarToken(usuario.getEmailUsuario(), usuario.getIdUsuario());

        // Retornar respuesta con token y datos del usuario
        return AuthResponseDTO.builder()
                .token(token)
                .usuario(convertirAUsuarioDTO(usuario))
                .build();
    }

    /**
     * Autentica o registra un usuario mediante token de Google/Firebase.
     *
     * @param loginGoogleDTO Token de Google
     * @return AuthResponseDTO con token JWT y datos del usuario
     * @throws Exception Si el token es inválido
     */
    @Transactional
    public AuthResponseDTO loginGoogle(LoginGoogleDTO loginGoogleDTO) throws Exception {
        FirebaseToken decodedToken;

        try {
            // Verificar el token de Google con Firebase
            decodedToken = firebaseAuth.verifyIdToken(loginGoogleDTO.getIdToken());
        } catch (FirebaseAuthException e) {
            throw new Exception("El token de Google es inválido o ha expirado");
        }

        // Extraer información del token
        String googleUid = decodedToken.getUid();
        String email = decodedToken.getEmail();
        String nombre = decodedToken.getName();

        // Buscar si el usuario ya existe (por googleUid o por email)
        Optional<Usuario> usuarioOpt = usuarioRepository.findByGoogleUid(googleUid);

        if (usuarioOpt.isEmpty()) {
            usuarioOpt = usuarioRepository.findByEmailUsuario(email);
        }

        Usuario usuario;

        if (usuarioOpt.isPresent()) {
            // Usuario existe
            usuario = usuarioOpt.get();

            // Verificar que permita login con Google
            if (!usuario.isPermiteGoogle()) {
                throw new Exception("Esta cuenta no tiene habilitado el login con Google");
            }

            // Verificar que esté activa
            if (!usuario.isActivo()) {
                throw new Exception("La cuenta está inactiva. Contacta con soporte");
            }

            // Actualizar googleUid si no lo tenía
            if (usuario.getGoogleUid() == null || usuario.getGoogleUid().isEmpty()) {
                usuario.setGoogleUid(googleUid);
                usuario = usuarioRepository.save(usuario);
            }

        } else {
            // Usuario no existe, crear uno nuevo
            String[] nombreCompleto = separarNombreCompleto(nombre);

            usuario = Usuario.builder()
                    .emailUsuario(email)
                    .googleUid(googleUid)
                    .nombreUsuario(nombreCompleto[0])
                    .apellidosUsuario(nombreCompleto[1])
                    .tipoUsuario(TipoUsuario.NORMAL)
                    .fechaRegistro(LocalDateTime.now())
                    .activo(true)
                    .permiteGoogle(true)
                    .fotoPerfil(decodedToken.getPicture())
                    .build();

            usuario = usuarioRepository.save(usuario);
        }

        // Generar token JWT
        String token = jwtService.generarToken(usuario.getEmailUsuario(), usuario.getIdUsuario());

        // Retornar respuesta
        return AuthResponseDTO.builder()
                .token(token)
                .usuario(convertirAUsuarioDTO(usuario))
                .build();
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario
     * @param emailAuth Email del usuario autenticado
     * @return UsuarioDTO con los datos del usuario
     * @throws Exception Si el usuario no existe o no tiene permisos
     */
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuario(Long id, String emailAuth) throws Exception {
        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new Exception("El usuario con ID " + id + " no fue encontrado"));

        // Verificar que el usuario autenticado pueda acceder (solo puede ver su propio perfil)
        if (!usuario.getEmailUsuario().equals(emailAuth)) {
            throw new Exception("No tienes permisos para acceder a este usuario");
        }

        return convertirAUsuarioDTO(usuario);
    }

    /**
     * Edita los datos de un usuario.
     *
     * @param id ID del usuario
     * @param editarDTO Datos a actualizar
     * @param emailAuth Email del usuario autenticado
     * @return UsuarioDTO con los datos actualizados
     * @throws Exception Si el usuario no existe o no tiene permisos
     */
    @Transactional
    public UsuarioDTO editarUsuario(Long id, EditarUsuarioDTO editarDTO, String emailAuth) throws Exception {
        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new Exception("El usuario con ID " + id + " no fue encontrado"));

        // Verificar permisos
        if (!usuario.getEmailUsuario().equals(emailAuth)) {
            throw new Exception("No tienes permisos para editar este usuario");
        }

        // Actualizar campos si están presentes
        if (editarDTO.getNombreUsuario() != null && !editarDTO.getNombreUsuario().isEmpty()) {
            usuario.setNombreUsuario(editarDTO.getNombreUsuario());
        }

        if (editarDTO.getApellidosUsuario() != null && !editarDTO.getApellidosUsuario().isEmpty()) {
            usuario.setApellidosUsuario(editarDTO.getApellidosUsuario());
        }

        if (editarDTO.getFotoPerfil() != null && !editarDTO.getFotoPerfil().isEmpty()) {
            usuario.setFotoPerfil(editarDTO.getFotoPerfil());
        }

        // Guardar cambios
        usuario = usuarioRepository.save(usuario);

        return convertirAUsuarioDTO(usuario);
    }

    /**
     * Elimina (desactiva) un usuario.
     *
     * @param id ID del usuario
     * @param emailAuth Email del usuario autenticado
     * @throws Exception Si el usuario no existe o no tiene permisos
     */
    @Transactional
    public void eliminarUsuario(Long id, String emailAuth) throws Exception {
        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new Exception("El usuario con ID " + id + " no fue encontrado"));

        // Verificar permisos
        if (!usuario.getEmailUsuario().equals(emailAuth)) {
            throw new Exception("No tienes permisos para eliminar este usuario");
        }

        // Desactivar usuario (soft delete)
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Convierte una entidad Usuario a UsuarioDTO.
     */
    private UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .emailUsuario(usuario.getEmailUsuario())
                .nombreUsuario(usuario.getNombreUsuario())
                .apellidosUsuario(usuario.getApellidosUsuario())
                .tipoUsuario(usuario.getTipoUsuario())
                .fotoPerfil(usuario.getFotoPerfil())
                .activo(usuario.isActivo())
                .permiteGoogle(usuario.isPermiteGoogle())
                .build();
    }

    /**
     * Separa un nombre completo en nombre y apellidos.
     */
    private String[] separarNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.isEmpty()) {
            return new String[]{"Usuario", ""};
        }

        String[] partes = nombreCompleto.trim().split("\\s+", 2);
        String nombre = partes[0];
        String apellidos = partes.length > 1 ? partes[1] : "";

        return new String[]{nombre, apellidos};
    }
}