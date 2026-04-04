package com.ufvshares.backend.solicitud;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ufvshares.backend.auth.SessionRepository;
import com.ufvshares.backend.producto.Producto;
import com.ufvshares.backend.producto.ProductoRepository;
import com.ufvshares.backend.usuario.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

  private final SolicitudService service;
  private final SessionRepository sessions;
  private final UsuarioRepository usuarios;
  private final ProductoRepository productos;

  public SolicitudController(SolicitudService service, SessionRepository sessions, UsuarioRepository usuarios,
      ProductoRepository productos) {
    this.service = service;
    this.sessions = sessions;
    this.usuarios = usuarios;
    this.productos = productos;
  }

  private Long resolveUserId(String auth) {
    if (auth == null || !auth.startsWith("Bearer ")) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token requerido");
    }
    String token = auth.substring(7);
    String email = sessions.findEmailByToken(token)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesión inválida"));
    return usuarios.findByCorreoIgnoreCase(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"))
        .getIdUsuario();
  }

  @GetMapping
  public List<Solicitud> getAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Solicitud getById(@PathVariable Long id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Solicitud create(@Valid @RequestBody Solicitud solicitud) {
    return service.create(solicitud);
  }

  @PostMapping("/reservar/{idProducto}")
  @ResponseStatus(HttpStatus.CREATED)
  public Solicitud reservar(
      @RequestHeader("Authorization") String auth,
      @PathVariable Long idProducto,
      @RequestBody(required = false) ReservaSolicitudRequest body) {
    Long idSolicitante = resolveUserId(auth);
    return service.reservar(idProducto, idSolicitante, body != null ? body.getFechaFin() : null);
  }

  @PutMapping("/{id}")
  public Solicitud update(@PathVariable Long id, @Valid @RequestBody Solicitud solicitud) {
    return service.update(id, solicitud);
  }

  /** Acepta una solicitud: actualiza el estado del producto y crea la transacción. */
  @PutMapping("/{id}/aceptar")
  public Solicitud aceptar(@RequestHeader("Authorization") String auth, @PathVariable Long id) {
    Long actorId = resolveUserId(auth);
    Solicitud solicitud = service.findById(id);
    Producto producto = productos.findById(solicitud.getIdProducto())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PRODUCTO_NOT_FOUND"));
    if (!producto.getIdPropietario().equals(actorId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO_PUEDES_GESTIONAR_ESTA_SOLICITUD");
    }
    return service.aceptar(id);
  }

  /** Rechaza una solicitud pendiente. */
  @PutMapping("/{id}/rechazar")
  public Solicitud rechazar(@RequestHeader("Authorization") String auth, @PathVariable Long id) {
    Long actorId = resolveUserId(auth);
    Solicitud solicitud = service.findById(id);
    Producto producto = productos.findById(solicitud.getIdProducto())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PRODUCTO_NOT_FOUND"));
    if (!producto.getIdPropietario().equals(actorId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "NO_PUEDES_GESTIONAR_ESTA_SOLICITUD");
    }
    return service.rechazar(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}

