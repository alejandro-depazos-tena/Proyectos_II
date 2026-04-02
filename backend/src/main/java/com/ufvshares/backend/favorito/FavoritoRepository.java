package com.ufvshares.backend.favorito;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

  List<Favorito> findByIdUsuarioOrderByFechaCreacionDesc(Long idUsuario);

  boolean existsByIdUsuarioAndIdProducto(Long idUsuario, Long idProducto);

  void deleteByIdUsuarioAndIdProducto(Long idUsuario, Long idProducto);

  void deleteByIdProducto(Long idProducto);
}
