package com.ufvshares.backend.usuario;

public class UsuarioPublicDto {

  private Long idUsuario;
  private String nombre;
  private String apellidos;
  private String correo;
  private String fotoPerfil;

  public UsuarioPublicDto(Long idUsuario, String nombre, String apellidos, String correo, String fotoPerfil) {
    this.idUsuario = idUsuario;
    this.nombre = nombre;
    this.apellidos = apellidos;
    this.correo = correo;
    this.fotoPerfil = fotoPerfil;
  }

  public Long getIdUsuario() { return idUsuario; }
  public String getNombre() { return nombre; }
  public String getApellidos() { return apellidos; }
  public String getCorreo() { return correo; }
  public String getFotoPerfil() { return fotoPerfil; }
}
