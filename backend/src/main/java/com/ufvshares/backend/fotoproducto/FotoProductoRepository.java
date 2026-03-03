package com.ufvshares.backend.fotoproducto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FotoProductoRepository extends JpaRepository<FotoProducto, Long> {
  List<FotoProducto> findByIdProductoOrderByEsPrincipalDesc(Long idProducto);
}
