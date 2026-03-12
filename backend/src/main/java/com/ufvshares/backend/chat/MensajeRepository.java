package com.ufvshares.backend.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

  /** All messages in a conversation, oldest first. */
  List<Mensaje> findByConversacionOrderByFechaEnvioAsc(Conversacion conv);

  /** Messages newer than a given message id (for polling). */
  @Query("""
      SELECT m FROM Mensaje m
      WHERE m.conversacion = :conv AND m.idMensaje > :lastId
      ORDER BY m.fechaEnvio ASC
      """)
  List<Mensaje> findNewMessages(@Param("conv") Conversacion conv, @Param("lastId") Long lastId);

  /** Count unread messages sent by others in a conversation. */
  @Query("""
      SELECT COUNT(m) FROM Mensaje m
      WHERE m.conversacion = :conv
        AND m.leido = false
        AND m.remitente.idUsuario != :uid
      """)
  long countUnread(@Param("conv") Conversacion conv, @Param("uid") Long uid);

  /** Mark all messages in a conversation as read (only those not sent by the user). */
  @Modifying
  @Query("""
      UPDATE Mensaje m SET m.leido = true
      WHERE m.conversacion = :conv AND m.remitente.idUsuario != :uid
      """)
  void markAllRead(@Param("conv") Conversacion conv, @Param("uid") Long uid);
}
