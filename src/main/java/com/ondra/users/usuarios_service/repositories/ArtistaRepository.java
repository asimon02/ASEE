package com.ondra.users.usuarios_service.repositories;

import com.ondra.users.usuarios_service.models.dao.Artista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    Optional<Artista> findByUsuarioIdUsuario(Long idUsuario);
}