package com.ufvshares.backend.reporteusuario;

import java.time.LocalDateTime;

import com.ufvshares.backend.reporte.EstadoReporte;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "reporte_usuario")
public class ReporteUsuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_reporte")
  private Long idReporte;

  @NotNull
  @Column(name = "id_usuario_reportante", nullable = false)
  private Long idUsuarioReportante;

  @NotNull
  @Column(name = "id_usuario_reportado", nullable = false)
  private Long idUsuarioReportado;

  @NotBlank
  @Size(max = 100)
  @Column(name = "motivo", nullable = false, length = 100)
  private String motivo;

  @Column(name = "comentario", columnDefinition = "TEXT")
  private String comentario;

  @Column(name = "fecha_reporte", nullable = false)
  private LocalDateTime fechaReporte = LocalDateTime.now();

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "estado_reporte", nullable = false)
  private EstadoReporte estadoReporte = EstadoReporte.ABIERTO;

  public Long getIdReporte() {
    return idReporte;
  }

  public void setIdReporte(Long idReporte) {
    this.idReporte = idReporte;
  }

  public Long getIdUsuarioReportante() {
    return idUsuarioReportante;
  }

  public void setIdUsuarioReportante(Long idUsuarioReportante) {
    this.idUsuarioReportante = idUsuarioReportante;
  }

  public Long getIdUsuarioReportado() {
    return idUsuarioReportado;
  }

  public void setIdUsuarioReportado(Long idUsuarioReportado) {
    this.idUsuarioReportado = idUsuarioReportado;
  }

  public String getMotivo() {
    return motivo;
  }

  public void setMotivo(String motivo) {
    this.motivo = motivo;
  }

  public String getComentario() {
    return comentario;
  }

  public void setComentario(String comentario) {
    this.comentario = comentario;
  }

  public LocalDateTime getFechaReporte() {
    return fechaReporte;
  }

  public void setFechaReporte(LocalDateTime fechaReporte) {
    this.fechaReporte = fechaReporte;
  }

  public EstadoReporte getEstadoReporte() {
    return estadoReporte;
  }

  public void setEstadoReporte(EstadoReporte estadoReporte) {
    this.estadoReporte = estadoReporte;
  }
}
