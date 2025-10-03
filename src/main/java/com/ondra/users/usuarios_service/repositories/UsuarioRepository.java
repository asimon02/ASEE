package com.ondra.users.usuarios_service.repositories;

import com.ondra.users.usuarios_service.models.dao.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailUsuario(String emailUsuario);

    Optional<Usuario> findByGoogleUid(String googleUid);
}