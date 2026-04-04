package com.ufvshares.backend.solicitud;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ufvshares.backend.common.NotFoundException;
import com.ufvshares.backend.contrato.ContratoService;
import com.ufvshares.backend.producto.EstadoProducto;
import com.ufvshares.backend.producto.Producto;
import com.ufvshares.backend.producto.ProductoRepository;
import com.ufvshares.backend.producto.TipoTransaccion;
import com.ufvshares.backend.transaccion.EstadoTransaccion;
import com.ufvshares.backend.transaccion.Transaccion;
import com.ufvshares.backend.transaccion.TransaccionRepository;
import com.ufvshares.backend.usuario.Usuario;
import com.ufvshares.backend.usuario.UsuarioRepository;

@Service
public class SolicitudService {

  private final SolicitudRepository repository;
  private final ProductoRepository productoRepository;
  private final TransaccionRepository transaccionRepository;
  private final ContratoService contratoService;
  private final UsuarioRepository usuarioRepository;

  public SolicitudService(SolicitudRepository repository,
      ProductoRepository productoRepository,
      TransaccionRepository transaccionRepository,
      ContratoService contratoService,
      UsuarioRepository usuarioRepository) {
    this.repository = repository;
    this.productoRepository = productoRepository;
    this.transaccionRepository = transaccionRepository;
    this.contratoService = contratoService;
    this.usuarioRepository = usuarioRepository;
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
  public Solicitud reservar(Long idProducto, Long idSolicitante, LocalDate fechaFin) {
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
      throw new ResponseStatusException(HttpStatus.CONFLICT, "YA_TIENES_UNA_SOLICITUD_ACTIVA_PARA_ESTE_PRODUCTO");
    }

    Solicitud solicitud = new Solicitud();
    LocalDateTime ahora = LocalDateTime.now();
    solicitud.setIdProducto(idProducto);
    solicitud.setIdSolicitante(idSolicitante);
    solicitud.setTipoTransaccion(producto.getTipoTransaccion());
    solicitud.setEstadoSolicitud(EstadoSolicitud.PENDIENTE);
    solicitud.setFechaSolicitud(ahora);

    if (producto.getTipoTransaccion() == TipoTransaccion.ALQUILER) {
      if (fechaFin == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DEBES_INDICAR_FECHA_FIN_ALQUILER");
      }
      LocalDateTime fechaFinDateTime = fechaFin.atTime(LocalTime.MAX.withNano(0));
      if (!fechaFinDateTime.isAfter(ahora)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "FECHA_FIN_INVALIDA");
      }
      solicitud.setFechaInicio(ahora);
      solicitud.setFechaFin(fechaFinDateTime);
    }

    return repository.save(solicitud);
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
      // ALQUILER o PRESTAMO quedan reservados hasta firma del arrendatario
      producto.setEstadoProducto(EstadoProducto.RESERVADO);
    }
    productoRepository.save(producto);

    // 4. Crear la transacción pendiente de firma (o en curso para ventas)
    Transaccion transaccion = new Transaccion();
    transaccion.setIdSolicitud(solicitud.getIdSolicitud());
    transaccion.setFechaCreacion(LocalDateTime.now());
    transaccion.setFechaInicioReal(
        solicitud.getFechaInicio() != null ? solicitud.getFechaInicio() : LocalDateTime.now());
    transaccion.setFechaFinReal(solicitud.getFechaFin());
    transaccion.setEstadoTransaccion(EstadoTransaccion.EN_CURSO);
    transaccion = transaccionRepository.save(transaccion);

    if (tipo != TipoTransaccion.VENTA) {
      Usuario arrendador = usuarioRepository.findById(producto.getIdPropietario())
          .orElseThrow(() -> new NotFoundException("USUARIO_NOT_FOUND"));
      Usuario arrendatario = usuarioRepository.findById(solicitud.getIdSolicitante())
          .orElseThrow(() -> new NotFoundException("USUARIO_NOT_FOUND"));
      contratoService.crearDesdeSolicitud(solicitud, producto, transaccion, arrendador, arrendatario);
    }

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

