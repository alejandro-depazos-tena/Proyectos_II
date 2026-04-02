package com.ufvshares.backend.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ufvshares.backend.chat.Conversacion;
import com.ufvshares.backend.chat.ConversacionRepository;
import com.ufvshares.backend.chat.Mensaje;
import com.ufvshares.backend.chat.MensajeRepository;
import com.ufvshares.backend.producto.CategoriaProducto;
import com.ufvshares.backend.producto.Producto;
import com.ufvshares.backend.producto.ProductoRepository;
import com.ufvshares.backend.producto.TipoTransaccion;
import com.ufvshares.backend.solicitud.EstadoSolicitud;
import com.ufvshares.backend.solicitud.Solicitud;
import com.ufvshares.backend.solicitud.SolicitudRepository;
import com.ufvshares.backend.transaccion.EstadoTransaccion;
import com.ufvshares.backend.transaccion.Transaccion;
import com.ufvshares.backend.transaccion.TransaccionRepository;
import com.ufvshares.backend.usuario.UsuarioRepository;

@Service
public class AdminMetricsService {

  private final UsuarioRepository usuarios;
  private final ProductoRepository productos;
  private final SolicitudRepository solicitudes;
  private final TransaccionRepository transacciones;
  private final ConversacionRepository conversaciones;
  private final MensajeRepository mensajes;

  public AdminMetricsService(
      UsuarioRepository usuarios,
      ProductoRepository productos,
      SolicitudRepository solicitudes,
      TransaccionRepository transacciones,
      ConversacionRepository conversaciones,
      MensajeRepository mensajes) {
    this.usuarios = usuarios;
    this.productos = productos;
    this.solicitudes = solicitudes;
    this.transacciones = transacciones;
    this.conversaciones = conversaciones;
    this.mensajes = mensajes;
  }

  public Map<String, Object> buildDashboard() {
    List<Producto> allProductos = productos.findAll();
    List<Solicitud> allSolicitudes = solicitudes.findAll();
    List<Transaccion> allTransacciones = transacciones.findAll();
    List<Conversacion> allConvs = conversaciones.findAll();
    List<Mensaje> allMsgs = mensajes.findAll();

    long solicitudesAceptadas = allSolicitudes.stream()
        .filter(s -> s.getEstadoSolicitud() == EstadoSolicitud.ACEPTADA)
        .count();
    long solicitudesRechazadas = allSolicitudes.stream()
        .filter(s -> s.getEstadoSolicitud() == EstadoSolicitud.RECHAZADA)
        .count();
    long solicitudesPendientes = allSolicitudes.stream()
        .filter(s -> s.getEstadoSolicitud() == EstadoSolicitud.PENDIENTE)
        .count();

    Map<Long, Solicitud> solicitudById = allSolicitudes.stream()
        .collect(Collectors.toMap(Solicitud::getIdSolicitud, s -> s, (a, b) -> a));

    long comprasRealizadas = allTransacciones.stream()
        .filter(t -> t.getEstadoTransaccion() == EstadoTransaccion.COMPLETADA)
        .filter(t -> {
          Solicitud s = solicitudById.get(t.getIdSolicitud());
          return s != null && s.getTipoTransaccion() == TipoTransaccion.VENTA;
        })
        .count();

    long alquileresActivos = allTransacciones.stream()
        .filter(t -> t.getEstadoTransaccion() == EstadoTransaccion.EN_CURSO)
        .filter(t -> {
          Solicitud s = solicitudById.get(t.getIdSolicitud());
          return s != null && s.getTipoTransaccion() == TipoTransaccion.ALQUILER;
        })
        .count();

    LocalDate today = LocalDate.now();
    List<Map<String, Object>> productosPorDia = buildDailySeriesFromProductos(allProductos, today.minusDays(13), today);
    List<Map<String, Object>> solicitudesPorDia = buildDailySeriesFromSolicitudes(allSolicitudes, today.minusDays(13), today);
    List<Map<String, Object>> mensajesPorDia = buildDailySeriesFromMensajes(allMsgs, today.minusDays(13), today);

    Map<String, Object> response = new LinkedHashMap<>();

    Map<String, Object> overview = new LinkedHashMap<>();
    overview.put("totalUsuarios", usuarios.count());
    overview.put("totalProductos", allProductos.size());
    overview.put("solicitudesPendientes", solicitudesPendientes);
    overview.put("solicitudesAceptadas", solicitudesAceptadas);
    overview.put("solicitudesRechazadas", solicitudesRechazadas);
    overview.put("comprasRealizadas", comprasRealizadas);
    overview.put("alquileresActivos", alquileresActivos);
    overview.put("conversacionesActivas", allConvs.size());
    overview.put("mensajesTotales", allMsgs.size());
    response.put("overview", overview);

    Map<String, Object> charts = new LinkedHashMap<>();
    charts.put("productosPorDia", productosPorDia);
    charts.put("solicitudesPorDia", solicitudesPorDia);
    charts.put("mensajesPorDia", mensajesPorDia);
    charts.put("productosPorCategoria", countProductosByCategoria(allProductos));
    charts.put("productosPorTipoTransaccion", countProductosByTipo(allProductos));
    charts.put("solicitudesPorEstado", countSolicitudesByEstado(allSolicitudes));
    charts.put("transaccionesPorEstado", countTransaccionesByEstado(allTransacciones));
    charts.put("topProductosSolicitados", topProductosSolicitados(allSolicitudes, allProductos));
    response.put("charts", charts);

    return response;
  }

  private List<Map<String, Object>> buildDailySeriesFromProductos(List<Producto> items, LocalDate start, LocalDate end) {
    Map<LocalDate, Long> grouped = items.stream()
        .collect(Collectors.groupingBy(
            p -> {
              LocalDateTime fecha = p.getFechaPublicacion();
              return fecha == null ? LocalDate.now() : fecha.toLocalDate();
            },
            Collectors.counting()));
    return toDailySeries(start, end, grouped);
  }

  private List<Map<String, Object>> buildDailySeriesFromSolicitudes(List<Solicitud> items, LocalDate start, LocalDate end) {
    Map<LocalDate, Long> grouped = items.stream()
        .collect(Collectors.groupingBy(
            s -> {
              LocalDateTime fecha = s.getFechaSolicitud();
              return fecha == null ? LocalDate.now() : fecha.toLocalDate();
            },
            Collectors.counting()));
    return toDailySeries(start, end, grouped);
  }

  private List<Map<String, Object>> buildDailySeriesFromMensajes(List<Mensaje> items, LocalDate start, LocalDate end) {
    Map<LocalDate, Long> grouped = items.stream()
        .collect(Collectors.groupingBy(
            m -> {
              LocalDateTime fecha = m.getFechaEnvio();
              return fecha == null ? LocalDate.now() : fecha.toLocalDate();
            },
            Collectors.counting()));
    return toDailySeries(start, end, grouped);
  }

  private List<Map<String, Object>> toDailySeries(LocalDate start, LocalDate end, Map<LocalDate, Long> grouped) {
    List<Map<String, Object>> out = new ArrayList<>();
    LocalDate cursor = start;
    while (!cursor.isAfter(end)) {
      Map<String, Object> row = new LinkedHashMap<>();
      row.put("date", cursor.toString());
      row.put("count", grouped.getOrDefault(cursor, 0L));
      out.add(row);
      cursor = cursor.plusDays(1);
    }
    return out;
  }

  private List<Map<String, Object>> countProductosByCategoria(List<Producto> items) {
    Map<CategoriaProducto, Long> counts = items.stream()
        .collect(Collectors.groupingBy(Producto::getCategoria, Collectors.counting()));
    List<Map<String, Object>> out = new ArrayList<>();
    for (CategoriaProducto c : CategoriaProducto.values()) {
      Map<String, Object> row = new LinkedHashMap<>();
      row.put("label", c.name());
      row.put("count", counts.getOrDefault(c, 0L));
      out.add(row);
    }
    return out;
  }

  private List<Map<String, Object>> countProductosByTipo(List<Producto> items) {
    Map<TipoTransaccion, Long> counts = items.stream()
        .collect(Collectors.groupingBy(Producto::getTipoTransaccion, Collectors.counting()));
    List<Map<String, Object>> out = new ArrayList<>();
    for (TipoTransaccion t : TipoTransaccion.values()) {
      Map<String, Object> row = new LinkedHashMap<>();
      row.put("label", t.name());
      row.put("count", counts.getOrDefault(t, 0L));
      out.add(row);
    }
    return out;
  }

  private List<Map<String, Object>> countSolicitudesByEstado(List<Solicitud> items) {
    Map<EstadoSolicitud, Long> counts = items.stream()
        .collect(Collectors.groupingBy(Solicitud::getEstadoSolicitud, Collectors.counting()));
    List<Map<String, Object>> out = new ArrayList<>();
    for (EstadoSolicitud s : EstadoSolicitud.values()) {
      Map<String, Object> row = new LinkedHashMap<>();
      row.put("label", s.name());
      row.put("count", counts.getOrDefault(s, 0L));
      out.add(row);
    }
    return out;
  }

  private List<Map<String, Object>> countTransaccionesByEstado(List<Transaccion> items) {
    Map<EstadoTransaccion, Long> counts = items.stream()
        .collect(Collectors.groupingBy(Transaccion::getEstadoTransaccion, Collectors.counting()));
    List<Map<String, Object>> out = new ArrayList<>();
    for (EstadoTransaccion t : EstadoTransaccion.values()) {
      Map<String, Object> row = new LinkedHashMap<>();
      row.put("label", t.name());
      row.put("count", counts.getOrDefault(t, 0L));
      out.add(row);
    }
    return out;
  }

  private List<Map<String, Object>> topProductosSolicitados(List<Solicitud> allSolicitudes, List<Producto> allProductos) {
    Map<Long, String> tituloByProducto = allProductos.stream()
        .collect(Collectors.toMap(Producto::getIdProducto, Producto::getTitulo, (a, b) -> a));

    Map<Long, Long> counts = allSolicitudes.stream()
        .collect(Collectors.groupingBy(Solicitud::getIdProducto, Collectors.counting()));

    return counts.entrySet().stream()
        .sorted(Map.Entry.<Long, Long>comparingByValue(Comparator.reverseOrder()))
        .limit(6)
        .map(entry -> {
          Map<String, Object> row = new LinkedHashMap<>();
          row.put("label", tituloByProducto.getOrDefault(entry.getKey(), "Producto " + entry.getKey()));
          row.put("count", entry.getValue());
          return row;
        })
        .collect(Collectors.toList());
  }
}
