package com.ufvshares.backend.appreview;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "app_review",
    uniqueConstraints = @UniqueConstraint(name = "uk_app_review_usuario", columnNames = { "id_usuario" })
)
public class AppReview {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_review")
  private Long idReview;

  @Column(name = "id_usuario", nullable = false)
  private Long idUsuario;

  @Column(name = "nombre_usuario", nullable = false, length = 120)
  private String nombreUsuario;

  @Column(name = "puntuacion", nullable = false)
  private Integer puntuacion;

  @Column(name = "comentario", nullable = false, length = 280)
  private String comentario;

  @Column(name = "fecha_actualizacion", nullable = false)
  private LocalDateTime fechaActualizacion;

  @PrePersist
  @PreUpdate
  protected void onWrite() {
    fechaActualizacion = LocalDateTime.now();
  }

  public Long getIdReview() {
    return idReview;
  }

  public void setIdReview(Long idReview) {
    this.idReview = idReview;
  }

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public String getNombreUsuario() {
    return nombreUsuario;
  }

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
  }

  public Integer getPuntuacion() {
    return puntuacion;
  }

  public void setPuntuacion(Integer puntuacion) {
    this.puntuacion = puntuacion;
  }

  public String getComentario() {
    return comentario;
  }

  public void setComentario(String comentario) {
    this.comentario = comentario;
  }

  public LocalDateTime getFechaActualizacion() {
    return fechaActualizacion;
  }

  public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
    this.fechaActualizacion = fechaActualizacion;
  }
}
