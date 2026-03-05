package com.ufvshares.backend.producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
  List<Producto> findByIdPropietario(Long idPropietario);

  List<Producto> findByEstadoProducto(EstadoProducto estado);

  List<Producto> findByEstadoProductoAndIdPropietarioNot(EstadoProducto estado, Long idPropietario);

  List<Producto> findByEstadoProductoAndCategoria(EstadoProducto estado, CategoriaProducto categoria);

  List<Producto> findByEstadoProductoAndCategoriaAndIdPropietarioNot(
      EstadoProducto estado, CategoriaProducto categoria, Long idPropietario);
}
