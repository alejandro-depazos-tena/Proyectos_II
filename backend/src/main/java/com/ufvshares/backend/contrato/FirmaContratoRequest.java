package com.ufvshares.backend.contrato;

import jakarta.validation.constraints.AssertTrue;

public class FirmaContratoRequest {

  @AssertTrue(message = "DEBES_ACEPTAR_LOS_TERMINOS")
  private boolean aceptaTerminos;

  public boolean isAceptaTerminos() {
    return aceptaTerminos;
  }

  public void setAceptaTerminos(boolean aceptaTerminos) {
    this.aceptaTerminos = aceptaTerminos;
  }
}
