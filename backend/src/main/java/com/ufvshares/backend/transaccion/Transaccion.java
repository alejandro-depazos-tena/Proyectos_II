package com.ufvshares.backend.transaccion;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "transaccion")
public class Transaccion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_transaccion")
  private Long idTransaccion;

  @NotNull
  @Column(name = "id_solicitud", nullable = false, unique = true)
  private Long idSolicitud;

  @Column(name = "fecha_creacion", nullable = false)
  private LocalDateTime fechaCreacion = LocalDateTime.now();

  @Column(name = "fecha_inicio_real")
  private LocalDateTime fechaInicioReal;

  @Column(name = "fecha_fin_real")
  private LocalDateTime fechaFinReal;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "estado_transaccion", nullable = false)
  private EstadoTransaccion estadoTransaccion = EstadoTransaccion.EN_CURSO;

  public Long getIdTransaccion() {
    return idTransaccion;
  }

  public void setIdTransaccion(Long idTransaccion) {
    this.idTransaccion = idTransaccion;
  }

  public Long getIdSolicitud() {
    return idSolicitud;
  }

  public void setIdSolicitud(Long idSolicitud) {
    this.idSolicitud = idSolicitud;
  }

  public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDateTime fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public LocalDateTime getFechaInicioReal() {
    return fechaInicioReal;
  }

  public void setFechaInicioReal(LocalDateTime fechaInicioReal) {
    this.fechaInicioReal = fechaInicioReal;
  }

  public LocalDateTime getFechaFinReal() {
    return fechaFinReal;
  }

  public void setFechaFinReal(LocalDateTime fechaFinReal) {
    this.fechaFinReal = fechaFinReal;
  }

  public EstadoTransaccion getEstadoTransaccion() {
    return estadoTransaccion;
  }

  public void setEstadoTransaccion(EstadoTransaccion estadoTransaccion) {
    this.estadoTransaccion = estadoTransaccion;
  }
}
