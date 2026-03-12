package com.ufvshares.backend.chat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ufvshares.backend.auth.SessionRepository;
import com.ufvshares.backend.producto.Producto;
import com.ufvshares.backend.producto.ProductoRepository;
import com.ufvshares.backend.usuario.Usuario;
import com.ufvshares.backend.usuario.UsuarioRepository;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

  private final SessionRepository sessions;
  private final UsuarioRepository usuarios;
  private final ProductoRepository productos;
  private final ConversacionRepository conversaciones;
  private final MensajeRepository mensajes;

  public ChatController(SessionRepository sessions, UsuarioRepository usuarios,
      ProductoRepository productos, ConversacionRepository conversaciones,
      MensajeRepository mensajes) {
    this.sessions = sessions;
    this.usuarios = usuarios;
    this.productos = productos;
    this.conversaciones = conversaciones;
    this.mensajes = mensajes;
  }

  // ── Auth helper ─────────────────────────────────────────────────────────────

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

  private void checkParticipant(Conversacion conv, Long uid) {
    boolean isParticipant = conv.getUsuario1().getIdUsuario().equals(uid)
        || conv.getUsuario2().getIdUsuario().equals(uid);
    if (!isParticipant) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a esta conversación");
    }
  }

  // ── Serialization helpers ────────────────────────────────────────────────────

  private Map<String, Object> convToMap(Conversacion c, Long myId) {
    // Decide who is "the other" user
    Usuario other = c.getUsuario1().getIdUsuario().equals(myId)
        ? c.getUsuario2() : c.getUsuario1();

    String initials = "";
    if (other.getNombre() != null && !other.getNombre().isBlank())
      initials += other.getNombre().trim().substring(0, 1).toUpperCase();
    if (other.getApellidos() != null && !other.getApellidos().isBlank())
      initials += other.getApellidos().trim().substring(0, 1).toUpperCase();

    long unread = mensajes.countUnread(c, myId);

    Map<String, Object> m = new LinkedHashMap<>();
    m.put("idConversacion", c.getIdConversacion());
    m.put("otroUsuarioId", other.getIdUsuario());
    m.put("otroUsuarioNombre", other.getNombre() + " " + other.getApellidos());
    m.put("otroUsuarioIniciales", initials.isEmpty() ? "?" : initials);
    m.put("otroUsuarioFoto", other.getFotoPerfil());
    m.put("idProducto", c.getProducto().getIdProducto());
    m.put("productoTitulo", c.getProducto().getTitulo());
    m.put("ultimoMensaje", c.getUltimoMensaje());
    m.put("fechaUltimoMsg", c.getFechaUltimoMsg());
    m.put("noLeidos", unread);
    return m;
  }

  private Map<String, Object> msgToMap(Mensaje msg, Long myId) {
    Map<String, Object> m = new LinkedHashMap<>();
    m.put("idMensaje", msg.getIdMensaje());
    m.put("from", msg.getRemitente().getIdUsuario().equals(myId) ? "me" : "other");
    m.put("contenido", msg.getContenido());
    m.put("fechaEnvio", msg.getFechaEnvio());
    m.put("leido", msg.isLeido());
    return m;
  }

  // ── Endpoints ────────────────────────────────────────────────────────────────

  /**
   * GET /api/chats
   * Returns all conversations for the authenticated user.
   */
  @GetMapping
  @Transactional(readOnly = true)
  public List<Map<String, Object>> listConversations(@RequestHeader("Authorization") String auth) {
    Usuario me = resolveUser(auth);
    List<Conversacion> convs = conversaciones.findByParticipante(me.getIdUsuario());
    List<Map<String, Object>> result = new ArrayList<>();
    for (Conversacion c : convs) {
      result.add(convToMap(c, me.getIdUsuario()));
    }
    return result;
  }

  /**
   * POST /api/chats
   * Body: { idProducto: Long, idOtroUsuario: Long }
   * Creates a conversation or returns the existing one.
   */
  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  @Transactional
  public Map<String, Object> getOrCreateConversation(
      @RequestHeader("Authorization") String auth,
      @RequestBody Map<String, Long> body) {

    Usuario me = resolveUser(auth);

    Long idProducto = body.get("idProducto");
    Long idOtroUsuario = body.get("idOtroUsuario");

    if (idProducto == null || idOtroUsuario == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "idProducto e idOtroUsuario son requeridos");
    }
    if (me.getIdUsuario().equals(idOtroUsuario)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes chatear contigo mismo");
    }

    Producto producto = productos.findById(idProducto)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    Usuario otro = usuarios.findById(idOtroUsuario)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    // Return existing conversation if it exists
    Conversacion conv = conversaciones
        .findExisting(idProducto, me.getIdUsuario(), idOtroUsuario)
        .orElseGet(() -> {
          Conversacion c = new Conversacion();
          c.setUsuario1(me);
          c.setUsuario2(otro);
          c.setProducto(producto);
          return conversaciones.save(c);
        });

    return convToMap(conv, me.getIdUsuario());
  }

  /**
   * GET /api/chats/{id}/messages?since={lastMsgId}
   * Returns all messages (or only new ones when since > 0).
   * Also marks received messages as read.
   */
  @GetMapping("/{id}/messages")
  @Transactional
  public List<Map<String, Object>> getMessages(
      @RequestHeader("Authorization") String auth,
      @PathVariable Long id,
      @RequestParam(name = "since", defaultValue = "0") Long since) {

    Usuario me = resolveUser(auth);
    Conversacion conv = conversaciones.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversación no encontrada"));
    checkParticipant(conv, me.getIdUsuario());

    // Mark incoming messages as read
    mensajes.markAllRead(conv, me.getIdUsuario());

    List<Mensaje> msgs = since > 0
        ? mensajes.findNewMessages(conv, since)
        : mensajes.findByConversacionOrderByFechaEnvioAsc(conv);

    List<Map<String, Object>> result = new ArrayList<>();
    for (Mensaje msg : msgs) {
      result.add(msgToMap(msg, me.getIdUsuario()));
    }
    return result;
  }

  /**
   * POST /api/chats/{id}/messages
   * Body: { contenido: String }
   * Sends a message in the conversation.
   */
  @PostMapping("/{id}/messages")
  @ResponseStatus(HttpStatus.CREATED)
  @Transactional
  public Map<String, Object> sendMessage(
      @RequestHeader("Authorization") String auth,
      @PathVariable Long id,
      @RequestBody Map<String, String> body) {

    Usuario me = resolveUser(auth);
    Conversacion conv = conversaciones.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conversación no encontrada"));
    checkParticipant(conv, me.getIdUsuario());

    String contenido = body.get("contenido");
    if (contenido == null || contenido.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El contenido no puede estar vacío");
    }
    contenido = contenido.trim();

    Mensaje msg = new Mensaje();
    msg.setConversacion(conv);
    msg.setRemitente(me);
    msg.setContenido(contenido);
    msg = mensajes.save(msg);

    // Update conversation summary
    String preview = contenido.length() > 200 ? contenido.substring(0, 200) + "…" : contenido;
    conv.setUltimoMensaje(preview);
    conv.setFechaUltimoMsg(msg.getFechaEnvio());
    conversaciones.save(conv);

    return msgToMap(msg, me.getIdUsuario());
  }
}
