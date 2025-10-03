package com.ondra.users.usuarios_service.repositories;

import com.ondra.users.usuarios_service.models.dao.PagoArtista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoArtistaRepository extends JpaRepository<PagoArtista, Long> {
    List<PagoArtista> findByArtistaIdArtista(Long idArtista);
}