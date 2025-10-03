package com.ondra.users.usuarios_service.repositories;

import com.ondra.users.usuarios_service.models.dao.PagoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoUsuarioRepository extends JpaRepository<PagoUsuario, Long> {
    List<PagoUsuario> findByUsuarioIdUsuario(Long idUsuario);
}