package com.ufvshares.backend.config;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufvshares.backend.auth.SessionJpaRepository;
import com.ufvshares.backend.auth.PasswordResetTokenRepository;
import com.ufvshares.backend.cambioperfil.PendingCambioRepository;
import com.ufvshares.backend.chat.Conversacion;
import com.ufvshares.backend.chat.ConversacionRepository;
import com.ufvshares.backend.chat.Mensaje;
import com.ufvshares.backend.chat.MensajeRepository;
import com.ufvshares.backend.contrato.ContratoRepository;
import com.ufvshares.backend.favorito.Favorito;
import com.ufvshares.backend.favorito.FavoritoRepository;
import com.ufvshares.backend.fotoproducto.FotoProductoRepository;
import com.ufvshares.backend.producto.CategoriaProducto;
import com.ufvshares.backend.producto.CondicionProducto;
import com.ufvshares.backend.producto.EstadoProducto;
import com.ufvshares.backend.producto.Producto;
import com.ufvshares.backend.producto.ProductoRepository;
import com.ufvshares.backend.producto.TipoTransaccion;
import com.ufvshares.backend.reporteproducto.ReporteProductoRepository;
import com.ufvshares.backend.reporteusuario.ReporteUsuarioRepository;
import com.ufvshares.backend.solicitud.EstadoSolicitud;
import com.ufvshares.backend.solicitud.Solicitud;
import com.ufvshares.backend.solicitud.SolicitudRepository;
import com.ufvshares.backend.transaccion.EstadoTransaccion;
import com.ufvshares.backend.transaccion.Transaccion;
import com.ufvshares.backend.transaccion.TransaccionRepository;
import com.ufvshares.backend.usuario.Usuario;
import com.ufvshares.backend.usuario.UsuarioRepository;

import jakarta.persistence.EntityManager;

@Component
@Order(1)
public class JsonSeedDataLoader implements CommandLineRunner {

  private enum SeedMode {
    IF_EMPTY,
    ALWAYS,
    NEVER
  }

  private static final Logger log = LoggerFactory.getLogger(JsonSeedDataLoader.class);

  private final ObjectMapper objectMapper;
  private final ResourceLoader resourceLoader;
  private final EntityManager entityManager;
  private final PasswordEncoder passwordEncoder;
  private final UsuarioRepository usuarioRepository;
  private final ProductoRepository productoRepository;
  private final SessionJpaRepository sessionRepository;
  private final SolicitudRepository solicitudRepository;
  private final TransaccionRepository transaccionRepository;
  private final FavoritoRepository favoritoRepository;
  private final ConversacionRepository conversacionRepository;
  private final MensajeRepository mensajeRepository;
  private final FotoProductoRepository fotoProductoRepository;
  private final ContratoRepository contratoRepository;
  private final ReporteProductoRepository reporteProductoRepository;
  private final ReporteUsuarioRepository reporteUsuarioRepository;
  private final PendingCambioRepository pendingCambioRepository;
  private final PasswordResetTokenRepository passwordResetTokenRepository;

  @Value("${app.seed.json.enabled:true}")
  private boolean seedEnabled;

  @Value("${app.seed.json.location:classpath:dev-h2-data.json}")
  private String seedLocation;

  @Value("${app.seed.json.mode:if_empty}")
  private String seedMode;

  @Value("${app.seed.bootstrap-admin.enabled:true}")
  private boolean bootstrapAdminEnabled;

  @Value("${app.seed.bootstrap-admin.email:admin@ufv.es}")
  private String bootstrapAdminEmail;

  @Value("${app.seed.bootstrap-admin.password:Admin1234!}")
  private String bootstrapAdminPassword;

  @Value("${app.seed.bootstrap-mario.enabled:true}")
  private boolean bootstrapMarioEnabled;

  @Value("${app.seed.bootstrap-mario.email:9300322@ALUMNOS.UFV.ES}")
  private String bootstrapMarioEmail;

  @Value("${app.seed.bootstrap-mario.password:1234ASDF}")
  private String bootstrapMarioPassword;

  public JsonSeedDataLoader(
      ObjectMapper objectMapper,
      ResourceLoader resourceLoader,
      EntityManager entityManager,
      PasswordEncoder passwordEncoder,
      UsuarioRepository usuarioRepository,
      ProductoRepository productoRepository,
      SessionJpaRepository sessionRepository,
      SolicitudRepository solicitudRepository,
      TransaccionRepository transaccionRepository,
      FavoritoRepository favoritoRepository,
      ConversacionRepository conversacionRepository,
      MensajeRepository mensajeRepository,
      FotoProductoRepository fotoProductoRepository,
      ContratoRepository contratoRepository,
      ReporteProductoRepository reporteProductoRepository,
      ReporteUsuarioRepository reporteUsuarioRepository,
      PendingCambioRepository pendingCambioRepository,
      PasswordResetTokenRepository passwordResetTokenRepository) {
    this.objectMapper = objectMapper;
    this.resourceLoader = resourceLoader;
    this.entityManager = entityManager;
    this.passwordEncoder = passwordEncoder;
    this.usuarioRepository = usuarioRepository;
    this.productoRepository = productoRepository;
    this.sessionRepository = sessionRepository;
    this.solicitudRepository = solicitudRepository;
    this.transaccionRepository = transaccionRepository;
    this.favoritoRepository = favoritoRepository;
    this.conversacionRepository = conversacionRepository;
    this.mensajeRepository = mensajeRepository;
    this.fotoProductoRepository = fotoProductoRepository;
    this.contratoRepository = contratoRepository;
    this.reporteProductoRepository = reporteProductoRepository;
    this.reporteUsuarioRepository = reporteUsuarioRepository;
    this.pendingCambioRepository = pendingCambioRepository;
    this.passwordResetTokenRepository = passwordResetTokenRepository;
  }

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    // Siempre limpiar sesiones al arrancar: el reinicio del backend invalida los tokens.
    long sessionsBorradas = sessionRepository.count();
    sessionRepository.deleteAll();
    if (sessionsBorradas > 0) {
      log.info("Sesiones anteriores eliminadas al arrancar: {} tokens invalidados.", sessionsBorradas);
    }

    SeedMode mode = resolveSeedMode();
    if (!seedEnabled || mode == SeedMode.NEVER) {
      log.info("Seed JSON desactivado (enabled={}, mode={}).", seedEnabled, mode);
      ensureBootstrapAdmin();
      ensureBootstrapMario();
      return;
    }

    if (mode == SeedMode.IF_EMPTY && (usuarioRepository.count() > 0 || productoRepository.count() > 0)) {
      log.info("Seed JSON omitido: ya existen datos en la base de datos.");
      ensureBootstrapAdmin();
      ensureBootstrapMario();
      return;
    }

    if (mode == SeedMode.ALWAYS) {
      clearFunctionalData();
      log.info("Seed JSON en modo ALWAYS: datos funcionales limpiados antes de recargar.");
    }

    Resource resource = resourceLoader.getResource(seedLocation);
    if (!resource.exists()) {
      log.warn("Seed JSON no encontrado en {}", seedLocation);
      return;
    }

    JsonNode root;
    try (InputStream inputStream = resource.getInputStream()) {
      root = objectMapper.readTree(inputStream);
    }

    List<Usuario> usuarios = parseUsuarios(root.path("usuario"));
    if (!usuarios.isEmpty()) {
      usuarioRepository.saveAll(usuarios);
    }

    List<Producto> productos = parseProductos(root.path("producto"));
    if (!productos.isEmpty()) {
      productoRepository.saveAll(productos);
    }

    Map<Long, Usuario> usuariosById = new HashMap<>();
    for (Usuario usuario : usuarioRepository.findAll()) {
      usuariosById.put(usuario.getIdUsuario(), usuario);
    }

    Map<Long, Producto> productosById = new HashMap<>();
    for (Producto producto : productoRepository.findAll()) {
      productosById.put(producto.getIdProducto(), producto);
    }

    List<Solicitud> solicitudes = parseSolicitudes(root.path("solicitud"));
    if (!solicitudes.isEmpty()) {
      solicitudRepository.saveAll(solicitudes);
    }

    List<Transaccion> transacciones = parseTransacciones(root.path("transaccion"));
    if (!transacciones.isEmpty()) {
      transaccionRepository.saveAll(transacciones);
    }

    List<Favorito> favoritos = parseFavoritos(root.path("favorito"));
    if (!favoritos.isEmpty()) {
      favoritoRepository.saveAll(favoritos);
    }

    List<Conversacion> conversaciones = parseConversaciones(
        root.path("conversacion"),
        usuariosById,
        productosById);
    if (!conversaciones.isEmpty()) {
      conversacionRepository.saveAll(conversaciones);
    }

    Map<Long, Conversacion> conversacionesById = new HashMap<>();
    for (Conversacion conversacion : conversacionRepository.findAll()) {
      conversacionesById.put(conversacion.getIdConversacion(), conversacion);
    }

    List<Mensaje> mensajes = parseMensajes(root.path("mensaje"), conversacionesById, usuariosById);
    mensajes = enrichMensajes(mensajes, conversacionesById);
    if (!mensajes.isEmpty()) {
      mensajeRepository.saveAll(mensajes);
    }

    ensureBootstrapAdmin();
    ensureBootstrapMario();

    log.info(
        "Seed JSON aplicado: {} usuarios, {} productos, {} solicitudes, {} transacciones, {} favoritos, {} conversaciones y {} mensajes.",
        usuarios.size(),
        productos.size(),
        solicitudes.size(),
        transacciones.size(),
        favoritos.size(),
        conversaciones.size(),
        mensajes.size());
  }

  private void ensureBootstrapAdmin() {
    if (!bootstrapAdminEnabled) {
      return;
    }

    if (bootstrapAdminEmail == null || bootstrapAdminEmail.isBlank()) {
      return;
    }

    String normalizedEmail = bootstrapAdminEmail.trim().toLowerCase();
    Usuario admin = usuarioRepository.findByCorreoIgnoreCase(normalizedEmail).orElseGet(Usuario::new);

    if (admin.getIdUsuario() == null) {
      admin.setNombre("Admin");
      admin.setApellidos("UFV Shares");
      admin.setDni("00000000A");
      admin.setTelefono("600000000");
      admin.setCorreo(normalizedEmail);
    }

    admin.setEsAdmin(true);
    admin.setPasswordHash(passwordEncoder.encode(bootstrapAdminPassword));
    usuarioRepository.save(admin);
    log.info("Usuario admin de arranque garantizado: {}", normalizedEmail);
  }

  private void ensureBootstrapMario() {
    if (!bootstrapMarioEnabled) {
      return;
    }

    if (bootstrapMarioEmail == null || bootstrapMarioEmail.isBlank()) {
      return;
    }

    String normalizedEmail = bootstrapMarioEmail.trim().toLowerCase();
    Usuario mario = usuarioRepository.findByCorreoIgnoreCase(normalizedEmail).orElseGet(Usuario::new);

    if (mario.getIdUsuario() == null) {
      mario.setNombre("Mario");
      mario.setApellidos("UFV");
      mario.setDni("00000021Y");
      mario.setTelefono("600000021");
      mario.setCorreo(normalizedEmail);
    }

    mario.setPasswordHash(passwordEncoder.encode(bootstrapMarioPassword));
    usuarioRepository.save(mario);
    log.info("Usuario Mario de arranque garantizado: {}", normalizedEmail);
  }

  private SeedMode resolveSeedMode() {
    if (seedMode == null || seedMode.isBlank()) {
      return SeedMode.IF_EMPTY;
    }
    try {
      return SeedMode.valueOf(seedMode.trim().toUpperCase());
    } catch (IllegalArgumentException ex) {
      log.warn("Valor invalido para app.seed.json.mode='{}'. Se usara IF_EMPTY.", seedMode);
      return SeedMode.IF_EMPTY;
    }
  }

  private void clearFunctionalData() {
    try {
      // Postgres: reinicia secuencias para mantener IDs deterministas y coherentes
      // con las referencias del dataset de ejemplo.
      entityManager.createNativeQuery(
          "TRUNCATE TABLE "
              + "mensaje, conversacion, favorito, foto_producto, transaccion, solicitud, "
              + "reporte_producto, reporte_usuario, pending_cambio, sessions, password_reset_tokens, "
              + "contrato, app_review, producto, usuario "
              + "RESTART IDENTITY CASCADE")
          .executeUpdate();
    } catch (Exception ex) {
      // Fallback para motores donde TRUNCATE ... RESTART IDENTITY no aplique.
      mensajeRepository.deleteAll();
      conversacionRepository.deleteAll();
      favoritoRepository.deleteAll();
      transaccionRepository.deleteAll();
      solicitudRepository.deleteAll();
      contratoRepository.deleteAll();
      reporteProductoRepository.deleteAll();
      reporteUsuarioRepository.deleteAll();
      fotoProductoRepository.deleteAll();
      pendingCambioRepository.deleteAll();
      passwordResetTokenRepository.deleteAll();
      sessionRepository.deleteAll();
      productoRepository.deleteAll();
      usuarioRepository.deleteAll();
    }

    entityManager.flush();
    entityManager.clear();
  }

  private List<Usuario> parseUsuarios(JsonNode usuariosNode) {
    List<Usuario> usuarios = new ArrayList<>();
    if (!usuariosNode.isArray()) {
      return usuarios;
    }

    for (JsonNode node : usuariosNode) {
      Usuario usuario = new Usuario();
      usuario.setIdUsuario(node.path("id_usuario").asLong());
      usuario.setNombre(node.path("nombre").asText());
      usuario.setApellidos(node.path("apellidos").asText());
      usuario.setCorreo(node.path("correo").asText());
      usuario.setTelefono(node.path("telefono").asText());
      usuario.setDni(node.path("dni").asText());
      usuario.setPasswordHash(node.path("password").asText());
      usuario.setEsAdmin(node.path("es_admin").asBoolean(false));
      usuarios.add(usuario);
    }

    return usuarios;
  }

  private List<Producto> parseProductos(JsonNode productosNode) {
    List<Producto> productos = new ArrayList<>();
    if (!productosNode.isArray()) {
      return productos;
    }

    for (JsonNode node : productosNode) {
      Producto producto = new Producto();
      producto.setIdProducto(node.path("id_producto").asLong());
      producto.setTitulo(node.path("titulo").asText());
      producto.setDescripcion(node.path("descripcion").asText());
      producto.setCategoria(CategoriaProducto.valueOf(node.path("categoria").asText()));
      producto.setTipoTransaccion(TipoTransaccion.valueOf(node.path("tipo_transaccion").asText()));
      producto.setEstadoProducto(EstadoProducto.valueOf(node.path("estado_producto").asText()));
      producto.setIdPropietario(node.path("id_propietario").asLong());

      if (!node.path("precio").isNull()) {
        producto.setPrecio(BigDecimal.valueOf(node.path("precio").asDouble()));
      }

      if (!node.path("condicion").isMissingNode() && !node.path("condicion").isNull()) {
        producto.setCondicion(CondicionProducto.valueOf(node.path("condicion").asText()));
      }

      if (!node.path("ubicacion").isMissingNode() && !node.path("ubicacion").isNull()) {
        producto.setUbicacion(node.path("ubicacion").asText());
      }

      if (!node.path("imagen_url").isMissingNode() && !node.path("imagen_url").isNull()) {
        producto.setImagenUrl(node.path("imagen_url").asText());
      }

      if (!node.path("vistas").isMissingNode()) {
        producto.setVistas(node.path("vistas").asInt(0));
      }

      LocalDateTime explicitFecha = parseDateTime(node.path("fecha_publicacion"));
      producto.setFechaPublicacion(explicitFecha != null ? explicitFecha : inferFechaPublicacion(producto.getIdProducto()));

      productos.add(producto);
    }

    return productos;
  }

  private List<Solicitud> parseSolicitudes(JsonNode solicitudesNode) {
    List<Solicitud> solicitudes = new ArrayList<>();
    if (!solicitudesNode.isArray()) {
      return solicitudes;
    }

    for (JsonNode node : solicitudesNode) {
      Solicitud solicitud = new Solicitud();
      solicitud.setIdSolicitud(node.path("id_solicitud").asLong());
      solicitud.setIdProducto(node.path("id_producto").asLong());
      solicitud.setIdSolicitante(node.path("id_solicitante").asLong());
      solicitud.setTipoTransaccion(TipoTransaccion.valueOf(node.path("tipo_transaccion").asText()));
      solicitud.setEstadoSolicitud(EstadoSolicitud.valueOf(node.path("estado_solicitud").asText()));
      solicitud.setFechaSolicitud(parseDateTime(node.path("fecha_solicitud")));
      solicitud.setFechaInicio(parseDateTime(node.path("fecha_inicio")));
      solicitud.setFechaFin(parseDateTime(node.path("fecha_fin")));
      solicitudes.add(solicitud);
    }

    return solicitudes;
  }

  private List<Transaccion> parseTransacciones(JsonNode transaccionesNode) {
    List<Transaccion> transacciones = new ArrayList<>();
    if (!transaccionesNode.isArray()) {
      return transacciones;
    }

    for (JsonNode node : transaccionesNode) {
      Transaccion transaccion = new Transaccion();
      transaccion.setIdTransaccion(node.path("id_transaccion").asLong());
      transaccion.setIdSolicitud(node.path("id_solicitud").asLong());
      transaccion.setEstadoTransaccion(EstadoTransaccion.valueOf(node.path("estado_transaccion").asText()));
      transaccion.setFechaCreacion(parseDateTime(node.path("fecha_creacion")));
      transaccion.setFechaInicioReal(parseDateTime(node.path("fecha_inicio_real")));
      transaccion.setFechaFinReal(parseDateTime(node.path("fecha_fin_real")));
      transacciones.add(transaccion);
    }

    return transacciones;
  }

  private List<Favorito> parseFavoritos(JsonNode favoritosNode) {
    List<Favorito> favoritos = new ArrayList<>();
    if (!favoritosNode.isArray()) {
      return favoritos;
    }

    for (JsonNode node : favoritosNode) {
      Favorito favorito = new Favorito();
      favorito.setIdFavorito(node.path("id_favorito").asLong());
      favorito.setIdUsuario(node.path("id_usuario").asLong());
      favorito.setIdProducto(node.path("id_producto").asLong());
      favorito.setFechaCreacion(parseDateTime(node.path("fecha_creacion")));
      favoritos.add(favorito);
    }

    return favoritos;
  }

  private List<Conversacion> parseConversaciones(
      JsonNode conversacionesNode,
      Map<Long, Usuario> usuariosById,
      Map<Long, Producto> productosById) {
    List<Conversacion> conversaciones = new ArrayList<>();
    if (!conversacionesNode.isArray()) {
      return conversaciones;
    }

    for (JsonNode node : conversacionesNode) {
      Usuario usuario1 = usuariosById.get(node.path("id_usuario1").asLong());
      Usuario usuario2 = usuariosById.get(node.path("id_usuario2").asLong());
      Producto producto = productosById.get(node.path("id_producto").asLong());
      if (usuario1 == null || usuario2 == null || producto == null) {
        continue;
      }

      Conversacion conversacion = new Conversacion();
      conversacion.setIdConversacion(node.path("id_conversacion").asLong());
      conversacion.setUsuario1(usuario1);
      conversacion.setUsuario2(usuario2);
      conversacion.setProducto(producto);
      conversacion.setFechaCreacion(parseDateTime(node.path("fecha_creacion")));
      conversacion.setUltimoMensaje(node.path("ultimo_mensaje").asText(null));
      conversacion.setFechaUltimoMsg(parseDateTime(node.path("fecha_ultimo_msg")));
      conversaciones.add(conversacion);
    }

    return conversaciones;
  }

  private List<Mensaje> parseMensajes(
      JsonNode mensajesNode,
      Map<Long, Conversacion> conversacionesById,
      Map<Long, Usuario> usuariosById) {
    List<Mensaje> mensajes = new ArrayList<>();
    if (!mensajesNode.isArray()) {
      return mensajes;
    }

    for (JsonNode node : mensajesNode) {
      Conversacion conversacion = conversacionesById.get(node.path("id_conversacion").asLong());
      Usuario remitente = usuariosById.get(node.path("id_remitente").asLong());
      if (conversacion == null || remitente == null) {
        continue;
      }

      Mensaje mensaje = new Mensaje();
      mensaje.setIdMensaje(node.path("id_mensaje").asLong());
      mensaje.setConversacion(conversacion);
      mensaje.setRemitente(remitente);
      mensaje.setContenido(node.path("contenido").asText());
      mensaje.setFechaEnvio(parseDateTime(node.path("fecha_envio")));
      mensaje.setLeido(node.path("leido").asBoolean(false));
      mensajes.add(mensaje);
    }

    return mensajes;
  }

  private List<Mensaje> enrichMensajes(List<Mensaje> mensajes, Map<Long, Conversacion> conversacionesById) {
    if (conversacionesById.isEmpty()) {
      return mensajes;
    }

    final int targetMessages = 120;
    if (mensajes.size() >= targetMessages) {
      return mensajes;
    }

    List<Mensaje> result = new ArrayList<>(mensajes);
    result.sort(Comparator.comparing(Mensaje::getFechaEnvio, Comparator.nullsLast(Comparator.naturalOrder())));

    Map<Long, LocalDateTime> lastByConversation = new HashMap<>();
    Map<Long, Integer> countByConversation = new HashMap<>();
    long maxId = 0L;

    for (Mensaje m : result) {
      if (m.getIdMensaje() != null && m.getIdMensaje() > maxId) {
        maxId = m.getIdMensaje();
      }
      Long convId = (m.getConversacion() == null) ? null : m.getConversacion().getIdConversacion();
      if (convId == null) {
        continue;
      }
      LocalDateTime ts = m.getFechaEnvio();
      if (ts != null) {
        LocalDateTime current = lastByConversation.get(convId);
        if (current == null || ts.isAfter(current)) {
          lastByConversation.put(convId, ts);
        }
      }
      countByConversation.put(convId, countByConversation.getOrDefault(convId, 0) + 1);
    }

    List<Conversacion> conversaciones = new ArrayList<>(conversacionesById.values());
    conversaciones.sort(Comparator.comparing(Conversacion::getIdConversacion));

    String[] plantillas = new String[] {
        "Sigue disponible? Me viene bien esta semana.",
        "Perfecto, puedo acercarme al campus esta tarde.",
        "Te confirmo en 10 minutos cuando salga de clase.",
        "Gracias! Si te parece cerramos por aqui.",
        "He actualizado los detalles, te encaja asi?",
        "Genial, lo dejo reservado para ti.",
        "Te paso ubicacion exacta cuando llegue.",
        "Sin problema, lo vemos manana por la manana."
    };

    int cursor = 0;
    while (result.size() < targetMessages && !conversaciones.isEmpty()) {
      Conversacion conv = conversaciones.get(cursor % conversaciones.size());
      cursor++;

      Long convId = conv.getIdConversacion();
      LocalDateTime base = lastByConversation.get(convId);
      if (base == null) {
        base = conv.getFechaCreacion() != null ? conv.getFechaCreacion() : LocalDateTime.now().minusDays(90);
      }

      int convCount = countByConversation.getOrDefault(convId, 0);
      boolean fromUser1 = (convCount % 2 == 0);
      Usuario remitente = fromUser1 ? conv.getUsuario1() : conv.getUsuario2();
      if (remitente == null) {
        continue;
      }

      LocalDateTime ts = base.plusDays(2 + (convCount % 4)).plusHours(3 + (convCount % 5));
      if (ts.isAfter(LocalDateTime.now().minusHours(2))) {
        ts = LocalDateTime.now().minusHours(2 + (convCount % 24));
      }

      Mensaje extra = new Mensaje();
      extra.setIdMensaje(++maxId);
      extra.setConversacion(conv);
      extra.setRemitente(remitente);
      extra.setContenido(plantillas[(convCount + cursor) % plantillas.length]);
      extra.setFechaEnvio(ts);
      extra.setLeido((convCount % 3) != 0);

      result.add(extra);
      lastByConversation.put(convId, ts);
      countByConversation.put(convId, convCount + 1);

      conv.setUltimoMensaje(extra.getContenido());
      conv.setFechaUltimoMsg(ts);
    }

    return result;
  }

  private LocalDateTime inferFechaPublicacion(Long productId) {
    long idx = (productId == null || productId <= 0) ? 1 : productId;
    LocalDateTime start = LocalDateTime.now().minusDays(220);
    LocalDateTime candidate = start.plusDays(idx * 3).plusHours((idx % 5) * 2);
    LocalDateTime latestAllowed = LocalDateTime.now().minusDays(1);
    if (candidate.isAfter(latestAllowed)) {
      return latestAllowed.minusHours(idx % 12);
    }
    return candidate;
  }

  private LocalDateTime parseDateTime(JsonNode node) {
    if (node == null || node.isMissingNode() || node.isNull()) {
      return null;
    }
    String value = node.asText();
    if (value == null || value.isBlank()) {
      return null;
    }
    return LocalDateTime.parse(value);
  }
}
