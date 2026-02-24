package com.ufvshares.backend.fotoproducto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "foto_producto")
public class FotoProducto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_foto")
  private Long idFoto;

  @NotNull
  @Column(name = "id_producto", nullable = false)
  private Long idProducto;

  @NotBlank
  @Size(max = 500)
  @Column(name = "url_foto", nullable = false, length = 500)
  private String urlFoto;

  @NotNull
  @Column(name = "es_principal", nullable = false)
  private Boolean esPrincipal = Boolean.FALSE;

  public Long getIdFoto() {
    return idFoto;
  }

  public void setIdFoto(Long idFoto) {
    this.idFoto = idFoto;
  }

  public Long getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(Long idProducto) {
    this.idProducto = idProducto;
  }

  public String getUrlFoto() {
    return urlFoto;
  }

  public void setUrlFoto(String urlFoto) {
    this.urlFoto = urlFoto;
  }

  public Boolean getEsPrincipal() {
    return esPrincipal;
  }

  public void setEsPrincipal(Boolean esPrincipal) {
    this.esPrincipal = esPrincipal;
  }
}
