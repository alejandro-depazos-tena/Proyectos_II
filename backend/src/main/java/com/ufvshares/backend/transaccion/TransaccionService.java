package com.ufvshares.backend.transaccion;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ufvshares.backend.common.NotFoundException;
import com.ufvshares.backend.producto.EstadoProducto;
import com.ufvshares.backend.producto.Producto;
import com.ufvshares.backend.producto.ProductoRepository;
import com.ufvshares.backend.producto.TipoTransaccion;
import com.ufvshares.backend.solicitud.EstadoSolicitud;
import com.ufvshares.backend.solicitud.Solicitud;
import com.ufvshares.backend.solicitud.SolicitudRepository;

@Service
public class TransaccionService {

  private final TransaccionRepository repository;
  private final SolicitudRepository solicitudRepository;
  private final ProductoRepository productoRepository;

  public TransaccionService(TransaccionRepository repository,
      SolicitudRepository solicitudRepository,
      ProductoRepository productoRepository) {
    this.repository = repository;
    this.solicitudRepository = solicitudRepository;
    this.productoRepository = productoRepository;
  }

  public List<Transaccion> findAll() {
    return repository.findAll();
  }

  public Transaccion findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("TRANSACCION_NOT_FOUND"));
  }

  public Transaccion create(Transaccion transaccion) {
    return repository.save(transaccion);
  }

  public Transaccion update(Long id, Transaccion data) {
    Transaccion existing = findById(id);
    existing.setIdSolicitud(data.getIdSolicitud());
    existing.setFechaCreacion(data.getFechaCreacion());
    existing.setFechaInicioReal(data.getFechaInicioReal());
    existing.setFechaFinReal(data.getFechaFinReal());
    existing.setEstadoTransaccion(data.getEstadoTransaccion());
    return repository.save(existing);
  }

  /** Completa la transacción y libera el producto (salvo ventas que quedan VENDIDO). */
  @Transactional
  public Transaccion completar(Long id) {
    Transaccion transaccion = findById(id);
    transaccion.setEstadoTransaccion(EstadoTransaccion.COMPLETADA);
    transaccion = repository.save(transaccion);

    Solicitud solicitud = solicitudRepository.findById(transaccion.getIdSolicitud())
        .orElseThrow(() -> new NotFoundException("SOLICITUD_NOT_FOUND"));

    Producto producto = productoRepository.findById(solicitud.getIdProducto())
        .orElseThrow(() -> new NotFoundException("PRODUCTO_NOT_FOUND"));

    // Las ventas se quedan VENDIDO; alquileres/préstamos vuelven a DISPONIBLE
    if (solicitud.getTipoTransaccion() != TipoTransaccion.VENTA) {
      producto.setEstadoProducto(EstadoProducto.DISPONIBLE);
      productoRepository.save(producto);
    }
    return transaccion;
  }

  /** Cancela la transacción y devuelve el producto a DISPONIBLE. */
  @Transactional
  public Transaccion cancelar(Long id) {
    Transaccion transaccion = findById(id);
    transaccion.setEstadoTransaccion(EstadoTransaccion.CANCELADA);
    transaccion = repository.save(transaccion);

    Solicitud solicitud = solicitudRepository.findById(transaccion.getIdSolicitud())
        .orElseThrow(() -> new NotFoundException("SOLICITUD_NOT_FOUND"));

    solicitud.setEstadoSolicitud(EstadoSolicitud.CANCELADA);
    solicitudRepository.save(solicitud);

    Producto producto = productoRepository.findById(solicitud.getIdProducto())
        .orElseThrow(() -> new NotFoundException("PRODUCTO_NOT_FOUND"));
    producto.setEstadoProducto(EstadoProducto.DISPONIBLE);
    productoRepository.save(producto);

    return transaccion;
  }

  /**
   * Devuelve información detallada de la transacción activa para un producto,
   * incluyendo las fechas de inicio y devolución. Retorna null si no hay transacción activa.
   */
  public Map<String, Object> getDetalleActivo(Long idProducto) {
    Optional<Transaccion> opt = repository.findActiveByProductoId(idProducto);
    if (opt.isEmpty()) return null;

    Transaccion t = opt.get();
    Solicitud s = solicitudRepository.findById(t.getIdSolicitud()).orElse(null);

    Map<String, Object> detalle = new LinkedHashMap<>();
    detalle.put("idTransaccion", t.getIdTransaccion());
    detalle.put("estadoTransaccion", t.getEstadoTransaccion());
    detalle.put("fechaCreacion", t.getFechaCreacion());
    detalle.put("fechaInicio", t.getFechaInicioReal());
    detalle.put("fechaDevolucion", t.getFechaFinReal());
    if (s != null) {
      detalle.put("tipoTransaccion", s.getTipoTransaccion());
      detalle.put("idSolicitante", s.getIdSolicitante());
      detalle.put("idProducto", s.getIdProducto());
    }
    return detalle;
  }

  public void delete(Long id) {
    Transaccion existing = findById(id);
    repository.delete(existing);
  }
}

