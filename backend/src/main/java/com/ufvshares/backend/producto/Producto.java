package com.ufvshares.backend.producto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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

  @Enumerated(EnumType.STRING)
  @Column(name = "condicion")
  private CondicionProducto condicion;

  @Size(max = 200)
  @Column(name = "ubicacion", length = 200)
  private String ubicacion;

  @Size(max = 500)
  @Column(name = "imagen_url", length = 500)
  private String imagenUrl;

  @Column(name = "fecha_publicacion", updatable = false)
  private LocalDateTime fechaPublicacion;

  @Column(name = "vistas", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
  private int vistas = 0;

  @PrePersist
  protected void onPersist() {
    if (fechaPublicacion == null) {
      fechaPublicacion = LocalDateTime.now();
    }
  }

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

  public CondicionProducto getCondicion() {
    return condicion;
  }

  public void setCondicion(CondicionProducto condicion) {
    this.condicion = condicion;
  }

  public String getUbicacion() {
    return ubicacion;
  }

  public void setUbicacion(String ubicacion) {
    this.ubicacion = ubicacion;
  }

  public String getImagenUrl() {
    return imagenUrl;
  }

  public void setImagenUrl(String imagenUrl) {
    this.imagenUrl = imagenUrl;
  }

  public LocalDateTime getFechaPublicacion() {
    return fechaPublicacion;
  }

  public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
    this.fechaPublicacion = fechaPublicacion;
  }

  public int getVistas() {
    return vistas;
  }

  public void setVistas(int vistas) {
    this.vistas = vistas;
  }
}
