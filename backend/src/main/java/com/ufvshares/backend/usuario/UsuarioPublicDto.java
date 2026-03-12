package com.ufvshares.backend.usuario;

public class UsuarioPublicDto {

  private final Long idUsuario;
  private final String nombre;
  private final String apellidos;
  private final String telefono;
  private final String fotoPerfil;
  private final int numAnuncios;

  public UsuarioPublicDto(Long idUsuario, String nombre, String apellidos, String telefono, String fotoPerfil, int numAnuncios) {
    this.idUsuario = idUsuario;
    this.nombre = nombre;
    this.apellidos = apellidos;
    this.telefono = telefono;
    this.fotoPerfil = fotoPerfil;
    this.numAnuncios = numAnuncios;
  }

  public Long getIdUsuario()    { return idUsuario; }
  public String getNombre()     { return nombre; }
  public String getApellidos()  { return apellidos; }
  public String getTelefono()   { return telefono; }
  public String getFotoPerfil() { return fotoPerfil; }
  public int getNumAnuncios()   { return numAnuncios; }
}
