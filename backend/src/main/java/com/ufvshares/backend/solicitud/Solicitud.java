package com.ufvshares.backend.solicitud;

import java.time.LocalDateTime;

import com.ufvshares.backend.producto.TipoTransaccion;

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
@Table(name = "solicitud")
public class Solicitud {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_solicitud")
  private Long idSolicitud;

  @NotNull
  @Column(name = "id_producto", nullable = false)
  private Long idProducto;

  @NotNull
  @Column(name = "id_solicitante", nullable = false)
  private Long idSolicitante;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_transaccion", nullable = false)
  private TipoTransaccion tipoTransaccion;

  @Column(name = "fecha_solicitud", nullable = false)
  private LocalDateTime fechaSolicitud = LocalDateTime.now();

  @Column(name = "fecha_inicio")
  private LocalDateTime fechaInicio;

  @Column(name = "fecha_fin")
  private LocalDateTime fechaFin;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "estado_solicitud", nullable = false)
  private EstadoSolicitud estadoSolicitud = EstadoSolicitud.PENDIENTE;

  public Long getIdSolicitud() {
    return idSolicitud;
  }

  public void setIdSolicitud(Long idSolicitud) {
    this.idSolicitud = idSolicitud;
  }

  public Long getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(Long idProducto) {
    this.idProducto = idProducto;
  }

  public Long getIdSolicitante() {
    return idSolicitante;
  }

  public void setIdSolicitante(Long idSolicitante) {
    this.idSolicitante = idSolicitante;
  }

  public TipoTransaccion getTipoTransaccion() {
    return tipoTransaccion;
  }

  public void setTipoTransaccion(TipoTransaccion tipoTransaccion) {
    this.tipoTransaccion = tipoTransaccion;
  }

  public LocalDateTime getFechaSolicitud() {
    return fechaSolicitud;
  }

  public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
    this.fechaSolicitud = fechaSolicitud;
  }

  public LocalDateTime getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(LocalDateTime fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  public LocalDateTime getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(LocalDateTime fechaFin) {
    this.fechaFin = fechaFin;
  }

  public EstadoSolicitud getEstadoSolicitud() {
    return estadoSolicitud;
  }

  public void setEstadoSolicitud(EstadoSolicitud estadoSolicitud) {
    this.estadoSolicitud = estadoSolicitud;
  }
}
