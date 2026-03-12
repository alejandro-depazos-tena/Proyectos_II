package com.ufvshares.backend.chat;

import java.time.LocalDateTime;

import com.ufvshares.backend.producto.Producto;
import com.ufvshares.backend.usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "conversacion")
public class Conversacion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_conversacion")
  private Long idConversacion;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_usuario1", nullable = false)
  private Usuario usuario1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_usuario2", nullable = false)
  private Usuario usuario2;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_producto", nullable = false)
  private Producto producto;

  @Column(name = "fecha_creacion", nullable = false)
  private LocalDateTime fechaCreacion = LocalDateTime.now();

  @Column(name = "ultimo_mensaje", length = 500)
  private String ultimoMensaje;

  @Column(name = "fecha_ultimo_msg")
  private LocalDateTime fechaUltimoMsg;

  public Long getIdConversacion() { return idConversacion; }
  public void setIdConversacion(Long id) { this.idConversacion = id; }

  public Usuario getUsuario1() { return usuario1; }
  public void setUsuario1(Usuario u) { this.usuario1 = u; }

  public Usuario getUsuario2() { return usuario2; }
  public void setUsuario2(Usuario u) { this.usuario2 = u; }

  public Producto getProducto() { return producto; }
  public void setProducto(Producto p) { this.producto = p; }

  public LocalDateTime getFechaCreacion() { return fechaCreacion; }
  public void setFechaCreacion(LocalDateTime dt) { this.fechaCreacion = dt; }

  public String getUltimoMensaje() { return ultimoMensaje; }
  public void setUltimoMensaje(String s) { this.ultimoMensaje = s; }

  public LocalDateTime getFechaUltimoMsg() { return fechaUltimoMsg; }
  public void setFechaUltimoMsg(LocalDateTime dt) { this.fechaUltimoMsg = dt; }
}
