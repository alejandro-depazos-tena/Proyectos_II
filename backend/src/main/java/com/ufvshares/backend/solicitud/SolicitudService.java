package com.ufvshares.backend.solicitud;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ufvshares.backend.common.NotFoundException;
import com.ufvshares.backend.producto.EstadoProducto;
import com.ufvshares.backend.producto.Producto;
import com.ufvshares.backend.producto.ProductoRepository;
import com.ufvshares.backend.producto.TipoTransaccion;
import com.ufvshares.backend.transaccion.EstadoTransaccion;
import com.ufvshares.backend.transaccion.Transaccion;
import com.ufvshares.backend.transaccion.TransaccionRepository;

@Service
public class SolicitudService {

  private final SolicitudRepository repository;
  private final ProductoRepository productoRepository;
  private final TransaccionRepository transaccionRepository;

  public SolicitudService(SolicitudRepository repository,
      ProductoRepository productoRepository,
      TransaccionRepository transaccionRepository) {
    this.repository = repository;
    this.productoRepository = productoRepository;
    this.transaccionRepository = transaccionRepository;
  }

  public List<Solicitud> findAll() {
    return repository.findAll();
  }

  public Solicitud findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("SOLICITUD_NOT_FOUND"));
  }

  public Solicitud create(Solicitud solicitud) {
    return repository.save(solicitud);
  }

  public Solicitud update(Long id, Solicitud data) {
    Solicitud existing = findById(id);
    existing.setIdProducto(data.getIdProducto());
    existing.setIdSolicitante(data.getIdSolicitante());
    existing.setTipoTransaccion(data.getTipoTransaccion());
    existing.setFechaSolicitud(data.getFechaSolicitud());
    existing.setFechaInicio(data.getFechaInicio());
    existing.setFechaFin(data.getFechaFin());
    existing.setEstadoSolicitud(data.getEstadoSolicitud());
    return repository.save(existing);
  }

  @Transactional
  public Solicitud reservar(Long idProducto, Long idSolicitante) {
    Producto producto = productoRepository.findById(idProducto)
        .orElseThrow(() -> new NotFoundException("PRODUCTO_NOT_FOUND"));

    if (producto.getIdPropietario().equals(idSolicitante)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "NO_PUEDES_RESERVAR_TU_PROPIO_PRODUCTO");
    }

    if (producto.getEstadoProducto() != EstadoProducto.DISPONIBLE) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "PRODUCTO_NO_DISPONIBLE");
    }

    List<Solicitud> existentes = repository.findByIdProductoAndIdSolicitanteAndEstadoSolicitudIn(
        idProducto,
        idSolicitante,
        Arrays.asList(EstadoSolicitud.PENDIENTE, EstadoSolicitud.ACEPTADA));
    if (!existentes.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "YA_TIENES_UNA_RESERVA_PARA_ESTE_PRODUCTO");
    }

    Solicitud solicitud = new Solicitud();
    solicitud.setIdProducto(idProducto);
    solicitud.setIdSolicitante(idSolicitante);
    solicitud.setTipoTransaccion(producto.getTipoTransaccion());
    solicitud.setEstadoSolicitud(EstadoSolicitud.ACEPTADA);
    solicitud.setFechaSolicitud(LocalDateTime.now());
    solicitud = repository.save(solicitud);

    producto.setEstadoProducto(EstadoProducto.RESERVADO);
    productoRepository.save(producto);

    return solicitud;
  }

  @Transactional
  public Solicitud aceptar(Long id) {
    Solicitud solicitud = findById(id);
    if (solicitud.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
      throw new IllegalStateException("La solicitud ya fue procesada");
    }

    // 1. Marcar esta solicitud como ACEPTADA
    solicitud.setEstadoSolicitud(EstadoSolicitud.ACEPTADA);
    solicitud = repository.save(solicitud);

    // 2. Rechazar las demás solicitudes pendientes del mismo producto
    List<Solicitud> otras = repository.findByIdProducto(solicitud.getIdProducto());
    for (Solicitud otra : otras) {
      if (!otra.getIdSolicitud().equals(id) && otra.getEstadoSolicitud() == EstadoSolicitud.PENDIENTE) {
        otra.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);
        repository.save(otra);
      }
    }

    // 3. Actualizar el estado del producto
    Producto producto = productoRepository.findById(solicitud.getIdProducto())
        .orElseThrow(() -> new NotFoundException("PRODUCTO_NOT_FOUND"));

    TipoTransaccion tipo = solicitud.getTipoTransaccion();
    if (tipo == TipoTransaccion.VENTA) {
      producto.setEstadoProducto(EstadoProducto.VENDIDO);
    } else {
      // ALQUILER o PRESTAMO
      producto.setEstadoProducto(EstadoProducto.ALQUILADO);
    }
    productoRepository.save(producto);

    // 4. Crear la transacción EN_CURSO
    Transaccion transaccion = new Transaccion();
    transaccion.setIdSolicitud(solicitud.getIdSolicitud());
    transaccion.setFechaCreacion(LocalDateTime.now());
    transaccion.setFechaInicioReal(
        solicitud.getFechaInicio() != null ? solicitud.getFechaInicio() : LocalDateTime.now());
    transaccion.setFechaFinReal(solicitud.getFechaFin());
    transaccion.setEstadoTransaccion(EstadoTransaccion.EN_CURSO);
    transaccionRepository.save(transaccion);

    return solicitud;
  }

  @Transactional
  public Solicitud rechazar(Long id) {
    Solicitud solicitud = findById(id);
    if (solicitud.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
      throw new IllegalStateException("La solicitud ya fue procesada");
    }
    solicitud.setEstadoSolicitud(EstadoSolicitud.RECHAZADA);
    return repository.save(solicitud);
  }

  public void delete(Long id) {
    Solicitud existing = findById(id);
    repository.delete(existing);
  }
}

