package com.ufvshares.backend.producto;

import java.math.BigDecimal;

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
@Table(name = "producto")
public class Producto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_producto")
  private Long idProducto;

  @NotBlank
  @Size(max = 150)
  @Column(name = "titulo", nullable = false, length = 150)
  private String titulo;

  @NotBlank
  @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
  private String descripcion;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "categoria", nullable = false)
  private CategoriaProducto categoria;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_transaccion", nullable = false)
  private TipoTransaccion tipoTransaccion;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "estado_producto", nullable = false)
  private EstadoProducto estadoProducto = EstadoProducto.DISPONIBLE;

  @Column(name = "precio", precision = 10, scale = 2)
  private BigDecimal precio;

  @NotNull
  @Column(name = "id_propietario", nullable = false)
  private Long idPropietario;

  public Long getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(Long idProducto) {
    this.idProducto = idProducto;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public CategoriaProducto getCategoria() {
    return categoria;
  }

  public void setCategoria(CategoriaProducto categoria) {
    this.categoria = categoria;
  }

  public TipoTransaccion getTipoTransaccion() {
    return tipoTransaccion;
  }

  public void setTipoTransaccion(TipoTransaccion tipoTransaccion) {
    this.tipoTransaccion = tipoTransaccion;
  }

  public EstadoProducto getEstadoProducto() {
    return estadoProducto;
  }

  public void setEstadoProducto(EstadoProducto estadoProducto) {
    this.estadoProducto = estadoProducto;
  }

  public BigDecimal getPrecio() {
    return precio;
  }

  public void setPrecio(BigDecimal precio) {
    this.precio = precio;
  }

  public Long getIdPropietario() {
    return idPropietario;
  }

  public void setIdPropietario(Long idPropietario) {
    this.idPropietario = idPropietario;
  }
}
