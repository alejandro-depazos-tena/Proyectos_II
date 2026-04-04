package com.ufvshares.backend.contrato;

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
@Table(name = "contrato")
public class Contrato {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_contrato")
  private Long idContrato;

  @NotNull
  @Column(name = "id_transaccion", nullable = false, unique = true)
  private Long idTransaccion;

  @NotNull
  @Column(name = "id_producto", nullable = false)
  private Long idProducto;

  @NotNull
  @Column(name = "id_propietario", nullable = false)
  private Long idPropietario;

  @NotNull
  @Column(name = "id_arrendatario", nullable = false)
  private Long idArrendatario;

  @Column(name = "fecha_creacion", nullable = false)
  private LocalDateTime fechaCreacion = LocalDateTime.now();

  @Column(name = "fecha_inicio")
  private LocalDateTime fechaInicio;

  @Column(name = "fecha_fin")
  private LocalDateTime fechaFin;

  @Column(name = "fecha_firma_arrendatario")
  private LocalDateTime fechaFirmaArrendatario;

  @Column(name = "acepta_terminos", nullable = false)
  private boolean aceptaTerminos;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "estado_contrato", nullable = false)
  private EstadoContrato estadoContrato = EstadoContrato.ACTIVO;

  @Column(name = "texto_condiciones", length = 5000)
  private String textoCondiciones;

  public Long getIdContrato() {
    return idContrato;
  }

  public void setIdContrato(Long idContrato) {
    this.idContrato = idContrato;
  }

  public Long getIdTransaccion() {
    return idTransaccion;
  }

  public void setIdTransaccion(Long idTransaccion) {
    this.idTransaccion = idTransaccion;
  }

  public Long getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(Long idProducto) {
    this.idProducto = idProducto;
  }

  public Long getIdPropietario() {
    return idPropietario;
  }

  public void setIdPropietario(Long idPropietario) {
    this.idPropietario = idPropietario;
  }

  public Long getIdArrendatario() {
    return idArrendatario;
  }

  public void setIdArrendatario(Long idArrendatario) {
    this.idArrendatario = idArrendatario;
  }

  public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDateTime fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
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

  public LocalDateTime getFechaFirmaArrendatario() {
    return fechaFirmaArrendatario;
  }

  public void setFechaFirmaArrendatario(LocalDateTime fechaFirmaArrendatario) {
    this.fechaFirmaArrendatario = fechaFirmaArrendatario;
  }

  public boolean isAceptaTerminos() {
    return aceptaTerminos;
  }

  public void setAceptaTerminos(boolean aceptaTerminos) {
    this.aceptaTerminos = aceptaTerminos;
  }

  public EstadoContrato getEstadoContrato() {
    return estadoContrato;
  }

  public void setEstadoContrato(EstadoContrato estadoContrato) {
    this.estadoContrato = estadoContrato;
  }

  public String getTextoCondiciones() {
    return textoCondiciones;
  }

  public void setTextoCondiciones(String textoCondiciones) {
    this.textoCondiciones = textoCondiciones;
  }
}
