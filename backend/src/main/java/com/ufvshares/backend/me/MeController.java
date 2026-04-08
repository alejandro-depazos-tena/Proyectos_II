package com.ufvshares.backend.me;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.view.RedirectView;

import com.ufvshares.backend.auth.SessionRepository;
import com.ufvshares.backend.cambioperfil.EmailService;
import com.ufvshares.backend.cambioperfil.PendingCambio;
import com.ufvshares.backend.cambioperfil.PendingCambioRepository;
import com.ufvshares.backend.contrato.Contrato;
import com.ufvshares.backend.contrato.EstadoContrato;
import com.ufvshares.backend.contrato.FirmaContratoRequest;
import com.ufvshares.backend.contrato.ContratoRepository;
import com.ufvshares.backend.contrato.ContratoService;
import com.ufvshares.backend.favorito.Favorito;
import com.ufvshares.backend.favorito.FavoritoRepository;
import com.ufvshares.backend.fotoproducto.FotoProducto;
import com.ufvshares.backend.fotoproducto.FotoProductoRepository;
import com.ufvshares.backend.producto.Producto;
import com.ufvshares.backend.producto.ProductoRepository;
import com.ufvshares.backend.solicitud.EstadoSolicitud;
import com.ufvshares.backend.solicitud.Solicitud;
import com.ufvshares.backend.solicitud.SolicitudRepository;
import com.ufvshares.backend.solicitud.SolicitudService;
import com.ufvshares.backend.transaccion.EstadoTransaccion;
import com.ufvshares.backend.transaccion.Transaccion;
import com.ufvshares.backend.transaccion.TransaccionRepository;
import com.ufvshares.backend.usuario.Usuario;
import com.ufvshares.backend.usuario.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/me")
public class MeController {

  private static final Set<String> CAMPOS_PERMITIDOS = Set.of("nombre", "apellidos", "telefono");
  private static final Map<String, String> CAMPO_LABEL = Map.of(
      "nombre", "nombre",
      "apellidos", "apellidos",
      "telefono", "teléfono"
  );

  private final SessionRepository sessions;
  private final UsuarioRepository usuarios;
  private final ProductoRepository productos;
  private final FavoritoRepository favoritos;
  private final FotoProductoRepository fotosRepo;
  private final SolicitudRepository solicitudes;
  private final SolicitudService solicitudService;
  private final TransaccionRepository transacciones;
  private final ContratoRepository contratosRepo;
  private final ContratoService contratos;
  private final PendingCambioRepository pendingRepo;
  private final EmailService emailService;

  @Value("${app.upload.dir:./uploads}")
  private String uploadDir;

  @Value("${app.frontend.url:http://localhost:4321}")
  private String frontendUrl;

  public MeController(SessionRepository sessions, UsuarioRepository usuarios,
      ProductoRepository productos, FavoritoRepository favoritos, FotoProductoRepository fotosRepo,
      SolicitudRepository solicitudes,
      SolicitudService solicitudService,
      TransaccionRepository transacciones,
      ContratoRepository contratosRepo,
      ContratoService contratos,
      PendingCambioRepository pendingRepo, EmailService emailService) {
    this.sessions = sessions;
    this.usuarios = usuarios;
    this.productos = productos;
    this.favoritos = favoritos;
    this.fotosRepo = fotosRepo;
    this.solicitudes = solicitudes;
    this.solicitudService = solicitudService;
    this.transacciones = transacciones;
    this.contratosRepo = contratosRepo;
    this.contratos = contratos;
    this.pendingRepo = pendingRepo;
    this.emailService = emailService;
  }

  private Usuario resolveUser(String auth) {
    if (auth == null || !auth.startsWith("Bearer ")) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token requerido");
    }
    String token = auth.substring(7);
    String email = sessions.findEmailByToken(token)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesión inválida"));
    return usuarios.findByCorreoIgnoreCase(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));
  }

  private Map<String, Object> buildSolicitudResumen(Solicitud s, Producto p) {
    Map<String, Object> m = new LinkedHashMap<>();
    m.put("idSolicitud", s.getIdSolicitud());
    m.put("fechaSolicitud", s.getFechaSolicitud());
    m.put("tipoTransaccion", s.getTipoTransaccion());
    m.put("estadoSolicitud", s.getEstadoSolicitud());
    m.put("fechaInicio", s.getFechaInicio());
    m.put("fechaFin", s.getFechaFin());

    Map<String, Object> prod = new LinkedHashMap<>();
    prod.put("idProducto", p.getIdProducto());
    prod.put("titulo", p.getTitulo());
    prod.put("categoria", p.getCategoria());
    prod.put("precio", p.getPrecio());
    prod.put("estadoProducto", p.getEstadoProducto());
    prod.put("imagenUrl", p.getImagenUrl());
    List<FotoProducto> fotos = fotosRepo.findByIdProductoOrderByEsPrincipalDesc(p.getIdProducto());
    if (!fotos.isEmpty()) prod.put("fotoUrl", fotos.get(0).getUrlFoto());
    m.put("producto", prod);

    return m;
  }

  @GetMapping
  public Map<String, Object> getMe(@RequestHeader("Authorization") String auth) {
    Usuario u = resolveUser(auth);
    Map<String, Object> res = new LinkedHashMap<>();
    res.put("idUsuario", u.getIdUsuario());
    res.put("nombre", u.getNombre());
    res.put("apellidos", u.getApellidos());
    res.put("correo", u.getCorreo());
    res.put("telefono", u.getTelefono());
    res.put("dni", u.getDni());
    res.put("esAdmin", u.isEsAdmin());
    res.put("fotoPerfil", u.getFotoPerfil());
    res.put("preguntaSeguridad", u.getPreguntaSeguridad());
    return res;
  }

  @GetMapping("/favoritos")
  @Transactional
  public List<Map<String, Object>> getMisFavoritos(@RequestHeader("Authorization") String auth) {
    Usuario u = resolveUser(auth);
    List<Favorito> items = favoritos.findByIdUsuarioOrderByFechaCreacionDesc(u.getIdUsuario());
    List<Map<String, Object>> result = new ArrayList<>();

    for (Favorito f : items) {
      Producto p = productos.findById(f.getIdProducto()).orElse(null);
      if (p == null) {
        favoritos.delete(f);
        continue;
      }

      Map<String, Object> m = new LinkedHashMap<>();
      m.put("idFavorito", f.getIdFavorito());
      m.put("fechaCreacion", f.getFechaCreacion());
      m.put("idProducto", p.getIdProducto());
      m.put("titulo", p.getTitulo());
      m.put("precio", p.getPrecio());
      m.put("imagenUrl", p.getImagenUrl());
      m.put("categoria", p.getCategoria());
      m.put("tipoTransaccion", p.getTipoTransaccion());
      m.put("estadoProducto", p.getEstadoProducto());

      List<FotoProducto> fotos = fotosRepo.findByIdProductoOrderByEsPrincipalDesc(p.getIdProducto());
      if (!fotos.isEmpty()) {
        m.put("fotoUrl", fotos.get(0).getUrlFoto());
      }

      result.add(m);
    }

    return result;
  }

  @PostMapping("/favoritos/{idProducto}")
  @Transactional
  public Map<String, Object> anadirFavorito(
      @RequestHeader("Authorization") String auth,
      @PathVariable Long idProducto) {
    Usuario u = resolveUser(auth);

    Producto p = productos.findById(idProducto)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PRODUCTO_NOT_FOUND"));

    if (favoritos.existsByIdUsuarioAndIdProducto(u.getIdUsuario(), idProducto)) {
      return Map.of("ok", true, "alreadyExists", true, "idProducto", idProducto);
    }

    Favorito favorito = new Favorito();
    favorito.setIdUsuario(u.getIdUsuario());
    favorito.setIdProducto(p.getIdProducto());
    favoritos.save(favorito);

    return Map.of("ok", true, "alreadyExists", false, "idProducto", idProducto);
  }

  @DeleteMapping("/favoritos/{idProducto}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminarFavorito(
      @RequestHeader("Authorization") String auth,
      @PathVariable Long idProducto) {
    Usuario u = resolveUser(auth);
    int deleted = favoritos.deleteOwnedFavorite(u.getIdUsuario(), idProducto);
    if (deleted == 0) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "FAVORITO_NOT_FOUND");
    }
  }

  @PostMapping("/seguridad")
  public Map<String, Object> actualizarSeguridad(
      @RequestHeader("Authorization") String auth,
      @RequestBody Map<String, String> body) {

    String pregunta = body.get("preguntaSeguridad");
    String respuesta = body.get("respuestaSeguridad");

    if (pregunta == null || pregunta.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La pregunta de seguridad es obligatoria");
    }
    if (respuesta == null || respuesta.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La respuesta de seguridad es obligatoria");
    }

    Usuario u = resolveUser(auth);
    u.setPreguntaSeguridad(pregunta.trim());
    u.setRespuestaSeguridadHash(hashSha256(respuesta.trim().toLowerCase(Locale.ROOT)));
    usuarios.save(u);

    Map<String, Object> res = new LinkedHashMap<>();
    res.put("ok", true);
    res.put("preguntaSeguridad", u.getPreguntaSeguridad());
    return res;
  }

  private String hashSha256(String rawValue) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(rawValue.getBytes(StandardCharsets.UTF_8));
      StringBuilder hex = new StringBuilder(hashBytes.length * 2);
      for (byte hashByte : hashBytes) {
        hex.append(String.format("%02x", hashByte));
      }
      return hex.toString();
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("SHA-256 no disponible", ex);
    }
  }

  @GetMapping("/productos")
  public List<Map<String, Object>> getMisProductos(@RequestHeader("Authorization") String auth) {
    Usuario u = resolveUser(auth);
    List<Producto> prods = productos.findByIdPropietario(u.getIdUsuario());
    List<Map<String, Object>> result = new ArrayList<>();
    for (Producto p : prods) {
      Map<String, Object> m = new LinkedHashMap<>();
      m.put("idProducto", p.getIdProducto());
      m.put("titulo", p.getTitulo());
      m.put("descripcion", p.getDescripcion());
      m.put("categoria", p.getCategoria());
      m.put("tipoTransaccion", p.getTipoTransaccion());
      m.put("precio", p.getPrecio());
      m.put("estadoProducto", p.getEstadoProducto());
      m.put("condicion", p.getCondicion());
      m.put("ubicacion", p.getUbicacion());
      m.put("imagenUrl", p.getImagenUrl());
      m.put("vistas", p.getVistas());
      m.put("fechaPublicacion", p.getFechaPublicacion());
      List<FotoProducto> fotos = fotosRepo.findByIdProductoOrderByEsPrincipalDesc(p.getIdProducto());
      if (!fotos.isEmpty()) {
        m.put("fotoUrl", fotos.get(0).getUrlFoto());
      }
      List<Map<String, Object>> fotosInfo = new ArrayList<>();
      for (FotoProducto f : fotos) {
        Map<String, Object> fm = new LinkedHashMap<>();
        fm.put("id", f.getIdFoto());
        fm.put("url", f.getUrlFoto());
        fm.put("esPrincipal", f.getEsPrincipal());
        fotosInfo.add(fm);
      }
      m.put("fotos", fotosInfo);
      result.add(m);
    }
    return result;
  }

  @GetMapping("/reservas")
  public List<Map<String, Object>> getMisReservas(@RequestHeader("Authorization") String auth) {
    Usuario u = resolveUser(auth);
    List<Solicitud> reservas = solicitudes.findByIdSolicitanteAndEstadoSolicitud(
        u.getIdUsuario(),
        EstadoSolicitud.ACEPTADA);

    List<Map<String, Object>> result = new ArrayList<>();
    for (Solicitud s : reservas) {
      Producto p = productos.findById(s.getIdProducto()).orElse(null);
      if (p == null) continue;
      Transaccion t = transacciones.findByIdSolicitud(s.getIdSolicitud()).orElse(null);
      if (t != null && t.getEstadoTransaccion() == EstadoTransaccion.CANCELADA) continue;

      LocalDateTime inicio = s.getFechaInicio() != null ? s.getFechaInicio() : (t != null ? t.getFechaInicioReal() : null);
      LocalDateTime fin = s.getFechaFin() != null ? s.getFechaFin() : (t != null ? t.getFechaFinReal() : null);

      Map<String, Object> m = new LinkedHashMap<>();
      m.put("idSolicitud", s.getIdSolicitud());
      m.put("fechaSolicitud", s.getFechaSolicitud());
      m.put("tipoTransaccion", s.getTipoTransaccion());
      m.put("estadoSolicitud", s.getEstadoSolicitud());
      m.put("fechaInicio", inicio);
      m.put("fechaFin", fin);
      if (t != null) {
        m.put("idTransaccion", t.getIdTransaccion());
        m.put("estadoTransaccion", t.getEstadoTransaccion());
      }
      if (s.getTipoTransaccion() != null && s.getTipoTransaccion().name().equals("ALQUILER")
          && inicio != null && fin != null) {
        long dias = ChronoUnit.DAYS.between(inicio.toLocalDate(), fin.toLocalDate());
        m.put("diasAlquiler", Math.max(dias, 0));
      }

      m.put("idProducto", p.getIdProducto());
      m.put("titulo", p.getTitulo());
      m.put("descripcion", p.getDescripcion());
      m.put("categoria", p.getCategoria());
      m.put("precio", p.getPrecio());
      m.put("estadoProducto", p.getEstadoProducto());
      m.put("condicion", p.getCondicion());
      m.put("ubicacion", p.getUbicacion());
      m.put("imagenUrl", p.getImagenUrl());

      List<FotoProducto> fotos = fotosRepo.findByIdProductoOrderByEsPrincipalDesc(p.getIdProducto());
      if (!fotos.isEmpty()) {
        m.put("fotoUrl", fotos.get(0).getUrlFoto());
      }

      result.add(m);
    }

    return result;
  }

  @GetMapping("/solicitudes/enviadas")
  public List<Map<String, Object>> getSolicitudesEnviadas(@RequestHeader("Authorization") String auth) {
    Usuario u = resolveUser(auth);
    List<Solicitud> list = solicitudes.findByIdSolicitanteOrderByFechaSolicitudDesc(u.getIdUsuario());

    List<Map<String, Object>> result = new ArrayList<>();
    for (Solicitud s : list) {
      Producto p = productos.findById(s.getIdProducto()).orElse(null);
      if (p == null) continue;

      Map<String, Object> m = buildSolicitudResumen(s, p);

      Usuario propietario = usuarios.findById(p.getIdPropietario()).orElse(null);
      if (propietario != null) {
        Map<String, Object> persona = new LinkedHashMap<>();
        persona.put("idUsuario", propietario.getIdUsuario());
        persona.put("nombre", propietario.getNombre());
        persona.put("apellidos", propietario.getApellidos());
        persona.put("correo", propietario.getCorreo());
        m.put("propietario", persona);
      }

      result.add(m);
    }
    return result;
  }

  @GetMapping("/solicitudes/recibidas")
  public List<Map<String, Object>> getSolicitudesRecibidas(@RequestHeader("Authorization") String auth) {
    Usuario u = resolveUser(auth);
    List<Producto> prods = productos.findByIdPropietario(u.getIdUsuario());
    List<Map<String, Object>> result = new ArrayList<>();

    for (Producto p : prods) {
      List<Solicitud> list = solicitudes.findByIdProducto(p.getIdProducto());
      for (Solicitud s : list) {
        Map<String, Object> m = buildSolicitudResumen(s, p);

        Usuario solicitante = usuarios.findById(s.getIdSolicitante()).orElse(null);
        if (solicitante != null) {
          Map<String, Object> persona = new LinkedHashMap<>();
          persona.put("idUsuario", solicitante.getIdUsuario());
          persona.put("nombre", solicitante.getNombre());
          persona.put("apellidos", solicitante.getApellidos());
          persona.put("correo", solicitante.getCorreo());
          m.put("solicitante", persona);
        }

        result.add(m);
      }
    }

    result.sort(Comparator.comparing(
        m -> (LocalDateTime) m.get("fechaSolicitud"),
        Comparator.nullsLast(Comparator.reverseOrder())));

    return result;
  }

  @PutMapping("/solicitudes/{id}/aceptar")
  public Map<String, Object> aceptarSolicitud(@RequestHeader("Authorization") String auth, @PathVariable Long id) {
    Usuario u = resolveUser(auth);
    Solicitud s = solicitudes.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SOLICITUD_NOT_FOUND"));
    Producto p = productos.findById(s.getIdProducto())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PRODUCTO_NOT_FOUND"));

    if (!u.getIdUsuario().equals(p.getIdPropietario())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO_PUEDES_GESTIONAR_ESTA_SOLICITUD");
    }
    if (s.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "SOLICITUD_YA_PROCESADA");
    }

    Solicitud updated = solicitudService.aceptar(id);
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("idSolicitud", updated.getIdSolicitud());
    response.put("estadoSolicitud", updated.getEstadoSolicitud());
    Transaccion transaccion = transacciones.findByIdSolicitud(updated.getIdSolicitud()).orElse(null);
    if (transaccion != null) {
      contratos.findByTransaccion(transaccion.getIdTransaccion()).ifPresent(c -> {
        response.put("contrato", toContratoMap(c));
      });
    }
    return response;
  }

  @PutMapping("/solicitudes/{id}/rechazar")
  public Map<String, Object> rechazarSolicitud(@RequestHeader("Authorization") String auth, @PathVariable Long id) {
    Usuario u = resolveUser(auth);
    Solicitud s = solicitudes.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SOLICITUD_NOT_FOUND"));
    Producto p = productos.findById(s.getIdProducto())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PRODUCTO_NOT_FOUND"));

    if (!u.getIdUsuario().equals(p.getIdPropietario())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO_PUEDES_GESTIONAR_ESTA_SOLICITUD");
    }
    if (s.getEstadoSolicitud() != EstadoSolicitud.PENDIENTE) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "SOLICITUD_YA_PROCESADA");
    }

    Solicitud updated = solicitudService.rechazar(id);
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("idSolicitud", updated.getIdSolicitud());
    response.put("estadoSolicitud", updated.getEstadoSolicitud());
    return response;
  }

  @GetMapping("/historial")
  public List<Map<String, Object>> getHistorial(@RequestHeader("Authorization") String auth) {
    Usuario u = resolveUser(auth);
    List<Producto> prods = productos.findByIdPropietario(u.getIdUsuario());
    List<Map<String, Object>> result = new ArrayList<>();

    for (Producto p : prods) {
      List<Solicitud> list = solicitudes.findByIdProductoAndEstadoSolicitud(
          p.getIdProducto(),
          EstadoSolicitud.ACEPTADA);

      for (Solicitud s : list) {
        Transaccion t = transacciones.findByIdSolicitud(s.getIdSolicitud()).orElse(null);
        if (t == null) continue;
        if (t.getEstadoTransaccion() == EstadoTransaccion.CANCELADA) continue;

        Map<String, Object> m = new LinkedHashMap<>();
        m.put("idTransaccion", t.getIdTransaccion());
        m.put("estadoTransaccion", t.getEstadoTransaccion());
        m.put("fechaTransaccion", t.getFechaCreacion());
        m.put("tipoTransaccion", s.getTipoTransaccion());

        LocalDateTime inicio = s.getFechaInicio() != null ? s.getFechaInicio() : t.getFechaInicioReal();
        LocalDateTime fin = s.getFechaFin() != null ? s.getFechaFin() : t.getFechaFinReal();
        m.put("fechaInicio", inicio);
        m.put("fechaFin", fin);
        if (s.getTipoTransaccion() != null && s.getTipoTransaccion().name().equals("ALQUILER")
            && inicio != null && fin != null) {
          long dias = ChronoUnit.DAYS.between(inicio.toLocalDate(), fin.toLocalDate());
          m.put("diasAlquiler", Math.max(dias, 0));
        }

        Map<String, Object> prod = new LinkedHashMap<>();
        prod.put("idProducto", p.getIdProducto());
        prod.put("titulo", p.getTitulo());
        prod.put("categoria", p.getCategoria());
        prod.put("precio", p.getPrecio());
        prod.put("imagenUrl", p.getImagenUrl());
        List<FotoProducto> fotos = fotosRepo.findByIdProductoOrderByEsPrincipalDesc(p.getIdProducto());
        if (!fotos.isEmpty()) prod.put("fotoUrl", fotos.get(0).getUrlFoto());
        m.put("producto", prod);

        Usuario solicitante = usuarios.findById(s.getIdSolicitante()).orElse(null);
        if (solicitante != null) {
          Map<String, Object> persona = new LinkedHashMap<>();
          persona.put("idUsuario", solicitante.getIdUsuario());
          persona.put("nombre", solicitante.getNombre());
          persona.put("apellidos", solicitante.getApellidos());
          persona.put("correo", solicitante.getCorreo());
          m.put("persona", persona);
        }

        contratos.findByTransaccion(t.getIdTransaccion()).ifPresent(c -> {
          m.put("contrato", toContratoMap(c));
        });

        result.add(m);
      }
    }

    result.sort(Comparator.comparing(
        m -> (LocalDateTime) m.get("fechaTransaccion"),
        Comparator.nullsLast(Comparator.reverseOrder())));

    return result;
  }

  @GetMapping("/contratos")
  public List<Map<String, Object>> getContratos(@RequestHeader("Authorization") String auth) {
    Usuario u = resolveUser(auth);
    List<Contrato> list = contratosRepo.findByIdPropietarioOrIdArrendatarioOrderByFechaCreacionDesc(
        u.getIdUsuario(),
        u.getIdUsuario());
    List<Map<String, Object>> result = new ArrayList<>();

    for (Contrato c : list) {
      Map<String, Object> item = toContratoMap(c);
      item.put("rol", c.getIdPropietario().equals(u.getIdUsuario()) ? "ARRENDADOR" : "ARRENDATARIO");

      Transaccion t = transacciones.findById(c.getIdTransaccion()).orElse(null);
      if (t != null) {
        item.put("estadoTransaccion", t.getEstadoTransaccion());
      }

      Producto p = productos.findById(c.getIdProducto()).orElse(null);
      if (p != null) {
        Map<String, Object> prod = new LinkedHashMap<>();
        prod.put("idProducto", p.getIdProducto());
        prod.put("titulo", p.getTitulo());
        prod.put("categoria", p.getCategoria());
        prod.put("tipoTransaccion", p.getTipoTransaccion());
        prod.put("precio", p.getPrecio());
        prod.put("imagenUrl", p.getImagenUrl());
        List<FotoProducto> fotos = fotosRepo.findByIdProductoOrderByEsPrincipalDesc(p.getIdProducto());
        if (!fotos.isEmpty()) prod.put("fotoUrl", fotos.get(0).getUrlFoto());
        item.put("producto", prod);
      }

      Long contraparteId = c.getIdPropietario().equals(u.getIdUsuario()) ? c.getIdArrendatario() : c.getIdPropietario();
      usuarios.findById(contraparteId).ifPresent(persona -> {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("idUsuario", persona.getIdUsuario());
        m.put("nombre", persona.getNombre());
        m.put("apellidos", persona.getApellidos());
        m.put("correo", persona.getCorreo());
        item.put("contraparte", m);
      });

      result.add(item);
    }

    return result;
  }

  @PutMapping("/contratos/{idContrato}/firmar")
  @Transactional
  public Map<String, Object> firmarContrato(
      @RequestHeader("Authorization") String auth,
      @PathVariable Long idContrato,
      @Valid @RequestBody FirmaContratoRequest request,
      HttpServletRequest httpRequest) {
    Usuario u = resolveUser(auth);
    Contrato contrato = contratosRepo.findById(idContrato)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CONTRATO_NOT_FOUND"));

    if (!u.getIdUsuario().equals(contrato.getIdArrendatario())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "SOLO_EL_ARRENDATARIO_PUEDE_FIRMAR");
    }
    if (contrato.getEstadoContrato() != EstadoContrato.PENDIENTE_FIRMA) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "CONTRATO_NO_PENDIENTE_DE_FIRMA");
    }
    if (!request.isAceptaTerminos()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DEBES_ACEPTAR_LOS_TERMINOS");
    }

    contrato = contratos.firmarPorArrendatario(
        contrato,
        httpRequest.getRemoteAddr(),
        httpRequest.getHeader("User-Agent"));

    Transaccion t = transacciones.findById(contrato.getIdTransaccion())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TRANSACCION_NOT_FOUND"));
    t.setEstadoTransaccion(EstadoTransaccion.EN_CURSO);
    transacciones.save(t);

    Producto p = productos.findById(contrato.getIdProducto())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PRODUCTO_NOT_FOUND"));
    p.setEstadoProducto(p.getTipoTransaccion() == null || p.getTipoTransaccion().name().equals("VENTA")
        ? p.getEstadoProducto()
        : com.ufvshares.backend.producto.EstadoProducto.ALQUILADO);
    productos.save(p);

    Map<String, Object> res = new LinkedHashMap<>();
    res.put("idContrato", contrato.getIdContrato());
    res.put("estadoContrato", contrato.getEstadoContrato());
    res.put("fechaFirmaArrendatario", contrato.getFechaFirmaArrendatario());
    return res;
  }

  private Map<String, Object> toContratoMap(Contrato contrato) {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("idContrato", contrato.getIdContrato());
    map.put("idTransaccion", contrato.getIdTransaccion());
    map.put("idProducto", contrato.getIdProducto());
    map.put("idPropietario", contrato.getIdPropietario());
    map.put("idArrendatario", contrato.getIdArrendatario());
    map.put("fechaCreacion", contrato.getFechaCreacion());
    map.put("fechaInicio", contrato.getFechaInicio());
    map.put("fechaFin", contrato.getFechaFin());
    map.put("fechaFirmaArrendatario", contrato.getFechaFirmaArrendatario());
    map.put("aceptaTerminos", contrato.isAceptaTerminos());
    map.put("estadoContrato", contrato.getEstadoContrato());
    map.put("textoCondiciones", contrato.getTextoCondiciones());
    return map;
  }

  // ── Actualizar dato de perfil directamente ─────────────────────
  @PostMapping("/solicitar-cambio")
  @Transactional
  public Map<String, String> solicitarCambio(
      @RequestHeader("Authorization") String auth,
      @RequestBody Map<String, String> body) {

    String campo = body.get("campo");
    String valor = body.get("valor");

    if (campo == null || !CAMPOS_PERMITIDOS.contains(campo)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo no permitido");
    }
    if (valor == null || valor.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El valor no puede estar vacío");
    }
    valor = valor.trim();

    Usuario u = resolveUser(auth);

    switch (campo) {
      case "nombre" -> u.setNombre(valor);
      case "apellidos" -> u.setApellidos(valor);
      case "telefono" -> u.setTelefono(valor);
      default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo no permitido");
    }
    usuarios.save(u);

    return Map.of(
        "ok", "true",
        "campo", campo,
        "valor", valor
    );
  }

  // ── Confirmar cambio desde el enlace del email ─────────────────
  @GetMapping("/confirmar-cambio")
  @Transactional
  public RedirectView confirmarCambio(@RequestParam("token") String token) {
    PendingCambio pc = pendingRepo.findByToken(token)
        .orElse(null);

    if (pc == null || pc.isUsado() || pc.getExpiracion().isBefore(LocalDateTime.now())) {
      return new RedirectView(frontendUrl + "/profile?cambio=error");
    }

    Usuario u = usuarios.findById(pc.getIdUsuario()).orElse(null);
    if (u == null) {
      return new RedirectView(frontendUrl + "/profile?cambio=error");
    }

    switch (pc.getCampo()) {
      case "nombre"    -> u.setNombre(pc.getValorNuevo());
      case "apellidos" -> u.setApellidos(pc.getValorNuevo());
      case "telefono"  -> u.setTelefono(pc.getValorNuevo());
    }
    usuarios.save(u);

    pc.setUsado(true);
    pendingRepo.save(pc);

    return new RedirectView(frontendUrl + "/profile?cambio=ok&campo=" + pc.getCampo());
  }

  @PostMapping(value = "/foto-perfil", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Map<String, Object> subirFotoPerfil(
      @RequestHeader("Authorization") String auth,
      @RequestParam("foto") MultipartFile foto) throws IOException {

    if (foto == null || foto.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se requiere una foto");
    }
    Usuario u = resolveUser(auth);
    Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
    Files.createDirectories(uploadPath);
    String ext = StringUtils.getFilenameExtension(foto.getOriginalFilename());
    String filename = "perfil-" + UUID.randomUUID() + (ext != null ? "." + ext : "");
    Files.copy(foto.getInputStream(), uploadPath.resolve(filename));
    u.setFotoPerfil("/uploads/" + filename);
    usuarios.save(u);
    return Map.of("fotoPerfil", u.getFotoPerfil());
  }

  @PostMapping("/productos")
  @ResponseStatus(HttpStatus.CREATED)
  public Producto crearProducto(
      @RequestHeader("Authorization") String auth,
      @RequestBody Producto producto) {
    Usuario u = resolveUser(auth);
    producto.setIdPropietario(u.getIdUsuario());
    return productos.save(producto);
  }

  @PutMapping("/productos/{id}")
  public Producto actualizarProducto(
      @RequestHeader("Authorization") String auth,
      @PathVariable Long id,
      @RequestBody Producto data) {
    Usuario u = resolveUser(auth);
    Producto p = productos.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    if (!p.getIdPropietario().equals(u.getIdUsuario())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No es tu producto");
    }
    if (data.getTitulo() != null) p.setTitulo(data.getTitulo());
    if (data.getDescripcion() != null) p.setDescripcion(data.getDescripcion());
    if (data.getCategoria() != null) p.setCategoria(data.getCategoria());
    if (data.getTipoTransaccion() != null) p.setTipoTransaccion(data.getTipoTransaccion());
    if (data.getPrecio() != null) p.setPrecio(data.getPrecio());
    if (data.getEstadoProducto() != null) p.setEstadoProducto(data.getEstadoProducto());
    if (data.getCondicion() != null) p.setCondicion(data.getCondicion());
    if (data.getUbicacion() != null) p.setUbicacion(data.getUbicacion());
    return productos.save(p);
  }

  @DeleteMapping("/productos/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminarProducto(
      @RequestHeader("Authorization") String auth,
      @PathVariable Long id) {
    Usuario u = resolveUser(auth);
    Producto p = productos.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    boolean isOwner = p.getIdPropietario() != null && p.getIdPropietario().equals(u.getIdUsuario());
    if (!isOwner && !u.isEsAdmin()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No es tu producto");
    }
    favoritos.deleteByIdProducto(id);
    fotosRepo.deleteByIdProducto(id);
    productos.delete(p);
  }

  @PostMapping(value = "/productos/{id}/fotos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public List<FotoProducto> subirFotos(
      @RequestHeader("Authorization") String auth,
      @PathVariable Long id,
      @RequestParam("fotos") List<MultipartFile> fotos) throws IOException {

    if (fotos == null || fotos.stream().allMatch(MultipartFile::isEmpty)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Se requiere al menos una foto");
    }

    Usuario u = resolveUser(auth);
    Producto p = productos.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    if (!p.getIdPropietario().equals(u.getIdUsuario())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No es tu producto");
    }

    Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
    Files.createDirectories(uploadPath);

    boolean isFirst = fotosRepo.findByIdProductoOrderByEsPrincipalDesc(id).isEmpty();
    List<FotoProducto> result = new ArrayList<>();

    for (MultipartFile file : fotos) {
      if (file.isEmpty()) continue;
      String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
      String filename = UUID.randomUUID() + (ext != null ? "." + ext : "");
      Files.copy(file.getInputStream(), uploadPath.resolve(filename));

      FotoProducto fp = new FotoProducto();
      fp.setIdProducto(id);
      fp.setUrlFoto("/uploads/" + filename);
      fp.setEsPrincipal(isFirst);
      isFirst = false;
      result.add(fotosRepo.save(fp));
    }

    return result;
  }

  @DeleteMapping("/productos/{productoId}/fotos/{fotoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminarFoto(
      @RequestHeader("Authorization") String auth,
      @PathVariable Long productoId,
      @PathVariable Long fotoId) {
    Usuario u = resolveUser(auth);
    Producto p = productos.findById(productoId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    if (!p.getIdPropietario().equals(u.getIdUsuario())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No es tu producto");
    }
    FotoProducto fp = fotosRepo.findById(fotoId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Foto no encontrada"));
    if (!fp.getIdProducto().equals(productoId)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La foto no pertenece a este producto");
    }
    fotosRepo.delete(fp);
    // Si era la principal, marcar la siguiente como principal
    if (Boolean.TRUE.equals(fp.getEsPrincipal())) {
      List<FotoProducto> restantes = fotosRepo.findByIdProductoOrderByEsPrincipalDesc(productoId);
      if (!restantes.isEmpty()) {
        restantes.get(0).setEsPrincipal(true);
        fotosRepo.save(restantes.get(0));
      }
    }
  }
}

