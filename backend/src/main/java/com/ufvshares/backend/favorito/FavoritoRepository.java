package com.ufvshares.backend.favorito;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

  List<Favorito> findByIdUsuarioOrderByFechaCreacionDesc(Long idUsuario);

  boolean existsByIdUsuarioAndIdProducto(Long idUsuario, Long idProducto);

  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("delete from Favorito f where f.idUsuario = :idUsuario and f.idProducto = :idProducto")
  int deleteOwnedFavorite(@Param("idUsuario") Long idUsuario, @Param("idProducto") Long idProducto);

  void deleteByIdProducto(Long idProducto);
}
