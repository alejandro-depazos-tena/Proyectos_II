package com.ufvshares.backend.solicitud;

import java.time.LocalDate;

public class ReservaSolicitudRequest {

  private LocalDate fechaFin;

  public LocalDate getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(LocalDate fechaFin) {
    this.fechaFin = fechaFin;
  }
}
