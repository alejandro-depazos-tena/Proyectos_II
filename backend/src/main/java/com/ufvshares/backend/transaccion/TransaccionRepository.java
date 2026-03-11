package com.ufvshares.backend.transaccion;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

  Optional<Transaccion> findByIdSolicitud(Long idSolicitud);

  @Query("SELECT t FROM Transaccion t WHERE t.idSolicitud IN " +
      "(SELECT s.idSolicitud FROM Solicitud s WHERE s.idProducto = :idProducto) " +
      "AND t.estadoTransaccion = com.ufvshares.backend.transaccion.EstadoTransaccion.EN_CURSO")
  Optional<Transaccion> findActiveByProductoId(@Param("idProducto") Long idProducto);

  @Query("SELECT t FROM Transaccion t WHERE t.idSolicitud IN " +
      "(SELECT s.idSolicitud FROM Solicitud s WHERE s.idProducto = :idProducto) " +
      "ORDER BY t.fechaCreacion DESC")
  List<Transaccion> findAllByProductoId(@Param("idProducto") Long idProducto);
}
