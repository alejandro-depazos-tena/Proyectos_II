package com.ufvshares.backend.favorito;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "favorito",
    uniqueConstraints = @UniqueConstraint(name = "uk_favorito_usuario_producto", columnNames = { "id_usuario", "id_producto" })
)
public class Favorito {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_favorito")
  private Long idFavorito;

  @Column(name = "id_usuario", nullable = false)
  private Long idUsuario;

  @Column(name = "id_producto", nullable = false)
  private Long idProducto;

  @Column(name = "fecha_creacion", nullable = false, updatable = false)
  private LocalDateTime fechaCreacion;

  @PrePersist
  protected void onPersist() {
    if (fechaCreacion == null) {
      fechaCreacion = LocalDateTime.now();
    }
  }

  public Long getIdFavorito() {
    return idFavorito;
  }

  public void setIdFavorito(Long idFavorito) {
    this.idFavorito = idFavorito;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public Long getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(Long idProducto) {
    this.idProducto = idProducto;
  }

  public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDateTime fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }
}
