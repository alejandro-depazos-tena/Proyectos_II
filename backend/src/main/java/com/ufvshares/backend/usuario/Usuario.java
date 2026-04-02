package com.ufvshares.backend.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuario")
public class Usuario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_usuario")
  private Long idUsuario;

  @NotBlank
  @Size(max = 100)
  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @NotBlank
  @Size(max = 150)
  @Column(name = "apellidos", nullable = false, length = 150)
  private String apellidos;

  @NotBlank
  @Size(max = 150)
  @Column(name = "correo", nullable = false, unique = true, length = 150)
  private String correo;

  @NotBlank
  @Size(max = 20)
  @Column(name = "telefono", nullable = false, unique = true, length = 20)
  private String telefono;

  @NotBlank
  @Size(max = 20)
  @Column(name = "dni", nullable = false, unique = true, length = 20)
  private String dni;

  @NotBlank
  @Size(max = 255)
  @Column(name = "password", nullable = false, length = 255)
  private String passwordHash;

  @Column(name = "foto_perfil", length = 512)
  private String fotoPerfil;

  @Column(name = "pregunta_seguridad", length = 255)
  private String preguntaSeguridad;

  @Column(name = "respuesta_seguridad_hash", length = 255)
  private String respuestaSeguridadHash;

  @Column(name = "es_admin", nullable = false)
  private boolean esAdmin = false;

  public Long getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(Long idUsuario) {
    this.idUsuario = idUsuario;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getApellidos() {
    return apellidos;
  }

  public void setApellidos(String apellidos) {
    this.apellidos = apellidos;
  }

  public String getCorreo() {
    return correo;
  }

  public void setCorreo(String correo) {
    this.correo = correo;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getDni() {
    return dni;
  }

  public void setDni(String dni) {
    this.dni = dni;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getFotoPerfil() {
    return fotoPerfil;
  }

  public void setFotoPerfil(String fotoPerfil) {
    this.fotoPerfil = fotoPerfil;
  }

  public String getPreguntaSeguridad() {
    return preguntaSeguridad;
  }

  public void setPreguntaSeguridad(String preguntaSeguridad) {
    this.preguntaSeguridad = preguntaSeguridad;
  }

  public String getRespuestaSeguridadHash() {
    return respuestaSeguridadHash;
  }

  public void setRespuestaSeguridadHash(String respuestaSeguridadHash) {
    this.respuestaSeguridadHash = respuestaSeguridadHash;
  }

  public boolean isEsAdmin() {
    return esAdmin;
  }

  public void setEsAdmin(boolean esAdmin) {
    this.esAdmin = esAdmin;
  }
}
