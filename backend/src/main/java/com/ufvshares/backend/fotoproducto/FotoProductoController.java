package com.ufvshares.backend.fotoProducto;

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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fotos-producto")
public class FotoProductoController {

  private final FotoProductoService service;

  public FotoProductoController(FotoProductoService service) {
    this.service = service;
  }

  @GetMapping
  public List<FotoProducto> getAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public FotoProducto getById(@PathVariable Long id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public FotoProducto create(@Valid @RequestBody FotoProducto fotoProducto) {
    return service.create(fotoProducto);
  }

  @PutMapping("/{id}")
  public FotoProducto update(@PathVariable Long id, @Valid @RequestBody FotoProducto fotoProducto) {
    return service.update(id, fotoProducto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}
