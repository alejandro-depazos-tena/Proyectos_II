package com.ufvshares.backend.usuario;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ufvshares.backend.common.NotFoundException;

@Service
public class UsuarioService {

  private final UsuarioRepository repository;

  public UsuarioService(UsuarioRepository repository) {
    this.repository = repository;
  }

  public List<Usuario> findAll() {
    return repository.findAll();
  }

  public Usuario findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("USUARIO_NOT_FOUND"));
  }

  public Usuario create(Usuario usuario) {
    return repository.save(usuario);
  }

  public Usuario update(Long id, Usuario data) {
    Usuario existing = findById(id);
    existing.setNombre(data.getNombre());
    existing.setApellidos(data.getApellidos());
    existing.setCorreo(data.getCorreo());
    existing.setTelefono(data.getTelefono());
    existing.setDni(data.getDni());
    return repository.save(existing);
  }

  public void delete(Long id) {
    Usuario existing = findById(id);
    repository.delete(existing);
  }
}
