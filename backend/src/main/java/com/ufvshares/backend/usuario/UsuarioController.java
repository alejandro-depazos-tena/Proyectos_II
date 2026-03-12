package com.ufvshares.backend.usuario;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ufvshares.backend.producto.ProductoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

  private final UsuarioService service;
  private final ProductoRepository productoRepository;

  public UsuarioController(UsuarioService service, ProductoRepository productoRepository) {
    this.service = service;
    this.productoRepository = productoRepository;
  }

  @GetMapping
  public List<Usuario> getAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Usuario getById(@PathVariable Long id) {
    return service.findById(id);
  }

  @GetMapping("/{id}/public")
  public UsuarioPublicDto getPublicById(@PathVariable Long id) {
    Usuario u = service.findById(id);
    int numAnuncios = productoRepository.findByIdPropietario(u.getIdUsuario()).size();
    return new UsuarioPublicDto(u.getIdUsuario(), u.getNombre(), u.getApellidos(), u.getTelefono(), u.getFotoPerfil(), numAnuncios);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Usuario create(@Valid @RequestBody Usuario usuario) {
    return service.create(usuario);
  }

  @PutMapping("/{id}")
  public Usuario update(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
    return service.update(id, usuario);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}
