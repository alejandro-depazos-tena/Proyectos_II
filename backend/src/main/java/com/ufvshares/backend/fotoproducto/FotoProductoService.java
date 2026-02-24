package com.ufvshares.backend.fotoproducto;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ufvshares.backend.common.NotFoundException;

@Service
public class FotoProductoService {

  private final FotoProductoRepository repository;

  public FotoProductoService(FotoProductoRepository repository) {
    this.repository = repository;
  }

  public List<FotoProducto> findAll() {
    return repository.findAll();
  }

  public FotoProducto findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("FOTO_PRODUCTO_NOT_FOUND"));
  }

  public FotoProducto create(FotoProducto fotoProducto) {
    return repository.save(fotoProducto);
  }

  public FotoProducto update(Long id, FotoProducto data) {
    FotoProducto existing = findById(id);
    existing.setIdProducto(data.getIdProducto());
    existing.setUrlFoto(data.getUrlFoto());
    existing.setEsPrincipal(data.getEsPrincipal());
    return repository.save(existing);
  }

  public void delete(Long id) {
    FotoProducto existing = findById(id);
    repository.delete(existing);
  }
}
