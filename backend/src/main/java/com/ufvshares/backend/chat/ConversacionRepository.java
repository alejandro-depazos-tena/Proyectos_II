package com.ufvshares.backend.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversacionRepository extends JpaRepository<Conversacion, Long> {

  /** All conversations where the user is either participant, most recent first. */
  @Query("""
      SELECT c FROM Conversacion c
      WHERE c.usuario1.idUsuario = :uid OR c.usuario2.idUsuario = :uid
      ORDER BY COALESCE(c.fechaUltimoMsg, c.fechaCreacion) DESC
      """)
  List<Conversacion> findByParticipante(@Param("uid") Long uid);

  /** Find an existing conversation between two users about a product (order-agnostic). */
  @Query("""
      SELECT c FROM Conversacion c
      WHERE c.producto.idProducto = :pid
        AND ((c.usuario1.idUsuario = :uid1 AND c.usuario2.idUsuario = :uid2)
          OR (c.usuario1.idUsuario = :uid2 AND c.usuario2.idUsuario = :uid1))
      """)
  Optional<Conversacion> findExisting(
      @Param("pid") Long productId,
      @Param("uid1") Long uid1,
      @Param("uid2") Long uid2);
}
