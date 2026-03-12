package com.ufvshares.backend.chat;

import java.time.LocalDateTime;

import com.ufvshares.backend.usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "mensaje")
public class Mensaje {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_mensaje")
  private Long idMensaje;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_conversacion", nullable = false)
  private Conversacion conversacion;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_remitente", nullable = false)
  private Usuario remitente;

  @Column(name = "contenido", nullable = false, columnDefinition = "TEXT")
  private String contenido;

  @Column(name = "fecha_envio", nullable = false)
  private LocalDateTime fechaEnvio;

  @Column(name = "leido", nullable = false)
  private boolean leido = false;

  @PrePersist
  protected void prePersist() {
    if (fechaEnvio == null) fechaEnvio = LocalDateTime.now();
  }

  public Long getIdMensaje() { return idMensaje; }
  public void setIdMensaje(Long id) { this.idMensaje = id; }

  public Conversacion getConversacion() { return conversacion; }
  public void setConversacion(Conversacion c) { this.conversacion = c; }

  public Usuario getRemitente() { return remitente; }
  public void setRemitente(Usuario u) { this.remitente = u; }

  public String getContenido() { return contenido; }
  public void setContenido(String s) { this.contenido = s; }

  public LocalDateTime getFechaEnvio() { return fechaEnvio; }
  public void setFechaEnvio(LocalDateTime dt) { this.fechaEnvio = dt; }

  public boolean isLeido() { return leido; }
  public void setLeido(boolean b) { this.leido = b; }
}
