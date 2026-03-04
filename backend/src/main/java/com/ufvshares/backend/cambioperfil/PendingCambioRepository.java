package com.ufvshares.backend.cambioperfil;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PendingCambioRepository extends JpaRepository<PendingCambio, Long> {
  Optional<PendingCambio> findByToken(String token);

  @Transactional
  void deleteByIdUsuarioAndCampo(Long idUsuario, String campo);
}
