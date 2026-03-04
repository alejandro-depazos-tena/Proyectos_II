package com.ufvshares.backend.cambioperfil;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pending_cambio")
public class PendingCambio {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "id_usuario", nullable = false)
  private Long idUsuario;

  /** nombre | apellidos | telefono */
  @Column(name = "campo", nullable = false, length = 30)
  private String campo;

  @Column(name = "valor_nuevo", nullable = false, length = 255)
  private String valorNuevo;

  @Column(name = "token", nullable = false, unique = true, length = 64)
  private String token;

  @Column(name = "expiracion", nullable = false)
  private LocalDateTime expiracion;

  @Column(name = "usado", nullable = false)
  private boolean usado = false;

  // ── getters / setters ─────────────────────────────────────────

  public Long getId() { return id; }

  public Long getIdUsuario() { return idUsuario; }
  public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

  public String getCampo() { return campo; }
  public void setCampo(String campo) { this.campo = campo; }

  public String getValorNuevo() { return valorNuevo; }
  public void setValorNuevo(String valorNuevo) { this.valorNuevo = valorNuevo; }

  public String getToken() { return token; }
  public void setToken(String token) { this.token = token; }

  public LocalDateTime getExpiracion() { return expiracion; }
  public void setExpiracion(LocalDateTime expiracion) { this.expiracion = expiracion; }

  public boolean isUsado() { return usado; }
  public void setUsado(boolean usado) { this.usado = usado; }
}
