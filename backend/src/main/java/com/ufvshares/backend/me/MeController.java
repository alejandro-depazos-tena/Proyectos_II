package com.ufvshares.backend.me;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
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
import com.ufvshares.backend.fotoproducto.FotoProducto;
import com.ufvshares.backend.fotoproducto.FotoProductoRepository;
import com.ufvshares.backend.producto.Producto;
import com.ufvshares.backend.producto.ProductoRepository;
import com.ufvshares.backend.solicitud.EstadoSolicitud;
import com.ufvshares.backend.solicitud.Solicitud;
import com.ufvshares.backend.solicitud.SolicitudRepository;
import com.ufvshares.backend.transaccion.EstadoTransaccion;
import com.ufvshares.backend.transaccion.Transaccion;
import com.ufvshares.backend.transaccion.TransaccionRepository;
import com.ufvshares.backend.usuario.Usuario;
import com.ufvshares.backend.usuario.UsuarioRepository;

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
  private final FotoProductoRepository fotosRepo;
  private final SolicitudRepository solicitudes;
  private final TransaccionRepository transacciones;
  private final PendingCambioRepository pendingRepo;
  private final EmailService emailService;

  @Value("${app.upload.dir:./uploads}")
  private String uploadDir;

  @Value("${app.frontend.url:http://localhost:4321}")
  private String frontendUrl;

  public MeController(SessionRepository sessions, UsuarioRepository usuarios,
      ProductoRepository productos, FotoProductoRepository fotosRepo,
      SolicitudRepository solicitudes,
      TransaccionRepository transacciones,
      PendingCambioRepository pendingRepo, EmailService emailService) {
    this.sessions = sessions;
    this.usuarios = usuarios;
    this.productos = productos;
    this.fotosRepo = fotosRepo;
    this.solicitudes = solicitudes;
    this.transacciones = transacciones;
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
    res.put("fotoPerfil", u.getFotoPerfil());
    return res;
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

        result.add(m);
      }
    }

    result.sort(Comparator.comparing(
        m -> (LocalDateTime) m.get("fechaTransaccion"),
        Comparator.nullsLast(Comparator.reverseOrder())));

    return result;
  }

  // ── Solicitar cambio de dato de perfil (envía email) ──────────
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

    // Borrar solicitudes anteriores pendientes para el mismo campo
    pendingRepo.deleteByIdUsuarioAndCampo(u.getIdUsuario(), campo);

    String token = UUID.randomUUID().toString().replace("-", "");
    PendingCambio pc = new PendingCambio();
    pc.setIdUsuario(u.getIdUsuario());
    pc.setCampo(campo);
    pc.setValorNuevo(valor);
    pc.setToken(token);
    pc.setExpiracion(LocalDateTime.now().plusMinutes(30));
    pendingRepo.save(pc);

    try {
      emailService.enviarConfirmacionCambio(
          u.getCorreo(),
          u.getNombre() + " " + u.getApellidos(),
          CAMPO_LABEL.getOrDefault(campo, campo),
          valor,
          token
      );
    } catch (Exception e) {
      // Si el correo falla, eliminar la solicitud y devolver error claro
      pendingRepo.deleteByIdUsuarioAndCampo(u.getIdUsuario(), campo);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "No se pudo enviar el correo de confirmación: " + e.getMessage());
    }

    return Map.of("ok", "true", "correo", u.getCorreo());
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
    if (!p.getIdPropietario().equals(u.getIdUsuario())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No es tu producto");
    }
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

