package com.ufvshares.backend.solicitud;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {
  List<Solicitud> findByIdProducto(Long idProducto);
  List<Solicitud> findByIdProductoAndEstadoSolicitud(Long idProducto, EstadoSolicitud estadoSolicitud);
}
