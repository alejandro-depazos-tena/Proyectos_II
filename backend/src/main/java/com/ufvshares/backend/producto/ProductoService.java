package com.ufvshares.backend.producto;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ufvshares.backend.common.NotFoundException;

@Service
public class ProductoService {

  private final ProductoRepository repository;

  public ProductoService(ProductoRepository repository) {
    this.repository = repository;
  }

  public List<Producto> findAll() {
    return repository.findAll();
  }

  public Producto findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("PRODUCTO_NOT_FOUND"));
  }

  public Producto create(Producto producto) {
    return repository.save(producto);
  }

  public Producto update(Long id, Producto data) {
    Producto existing = findById(id);
    existing.setTitulo(data.getTitulo());
    existing.setDescripcion(data.getDescripcion());
    existing.setCategoria(data.getCategoria());
    existing.setTipoTransaccion(data.getTipoTransaccion());
    existing.setEstadoProducto(data.getEstadoProducto());
    existing.setPrecio(data.getPrecio());
    existing.setIdPropietario(data.getIdPropietario());
    existing.setCondicion(data.getCondicion());
    existing.setUbicacion(data.getUbicacion());
    existing.setImagenUrl(data.getImagenUrl());
    existing.setVistas(data.getVistas());
    return repository.save(existing);
  }

  public void delete(Long id) {
    Producto existing = findById(id);
    repository.delete(existing);
  }
}
