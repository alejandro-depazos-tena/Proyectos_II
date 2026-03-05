package com.ufvshares.backend.producto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
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

import com.ufvshares.backend.auth.SessionRepository;
import com.ufvshares.backend.fotoproducto.FotoProducto;
import com.ufvshares.backend.fotoproducto.FotoProductoRepository;
import com.ufvshares.backend.usuario.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

  private final ProductoService service;
  private final SessionRepository sessions;
  private final UsuarioRepository usuarios;
  private final FotoProductoRepository fotosRepo;

  public ProductoController(ProductoService service,
      SessionRepository sessions,
      UsuarioRepository usuarios,
      FotoProductoRepository fotosRepo) {
    this.service = service;
    this.sessions = sessions;
    this.usuarios = usuarios;
    this.fotosRepo = fotosRepo;
  }

  /**
   * GET /api/productos
   * Devuelve productos DISPONIBLES con su foto principal.
   * Si se envía Authorization, se excluyen los productos del usuario autenticado.
   * El parámetro opcional ?categoria=LIBROS filtra por categoría.
   */
  @GetMapping
  public List<Map<String, Object>> getAll(
      @RequestHeader(value = "Authorization", required = false) String auth,
      @RequestParam(value = "categoria", required = false) String categoria) {

    Long excludeOwnerId = resolveUserIdFromToken(auth);
    List<Producto> productos = service.findDisponibles(excludeOwnerId, categoria);

    List<Map<String, Object>> result = new ArrayList<>();
    for (Producto p : productos) {
      Map<String, Object> m = new LinkedHashMap<>();
      m.put("idProducto", p.getIdProducto());
      m.put("titulo", p.getTitulo());
      m.put("descripcion", p.getDescripcion());
      m.put("categoria", p.getCategoria());
      m.put("tipoTransaccion", p.getTipoTransaccion());
      m.put("estadoProducto", p.getEstadoProducto());
      m.put("condicion", p.getCondicion());
      m.put("ubicacion", p.getUbicacion());
      m.put("precio", p.getPrecio());
      m.put("imagenUrl", p.getImagenUrl());
      m.put("vistas", p.getVistas());
      m.put("fechaPublicacion", p.getFechaPublicacion());
      m.put("idPropietario", p.getIdPropietario());

      // Primera foto disponible (principal primero)
      List<FotoProducto> fotos = fotosRepo.findByIdProductoOrderByEsPrincipalDesc(p.getIdProducto());
      String fotoUrl = fotos.isEmpty() ? null : fotos.get(0).getUrlFoto();
      m.put("fotoUrl", fotoUrl);

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

  /** Intenta extraer el idUsuario del token Bearer; retorna null si no hay token o es inválido. */
  private Long resolveUserIdFromToken(String auth) {
    if (auth == null || !auth.startsWith("Bearer ")) return null;
    String token = auth.substring(7);
    return sessions.findEmailByToken(token)
        .flatMap(email -> usuarios.findByCorreoIgnoreCase(email))
        .map(u -> u.getIdUsuario())
        .orElse(null);
  }

  @GetMapping("/{id}")
  public Map<String, Object> getById(@PathVariable Long id) {
    Producto p = service.findById(id);
    Map<String, Object> m = new LinkedHashMap<>();
    m.put("idProducto", p.getIdProducto());
    m.put("titulo", p.getTitulo());
    m.put("descripcion", p.getDescripcion());
    m.put("categoria", p.getCategoria());
    m.put("tipoTransaccion", p.getTipoTransaccion());
    m.put("estadoProducto", p.getEstadoProducto());
    m.put("condicion", p.getCondicion());
    m.put("ubicacion", p.getUbicacion());
    m.put("precio", p.getPrecio());
    m.put("imagenUrl", p.getImagenUrl());
    m.put("vistas", p.getVistas());
    m.put("fechaPublicacion", p.getFechaPublicacion());
    m.put("idPropietario", p.getIdPropietario());

    List<FotoProducto> fotos = fotosRepo.findByIdProductoOrderByEsPrincipalDesc(p.getIdProducto());
    String fotoUrl = fotos.isEmpty() ? null : fotos.get(0).getUrlFoto();
    m.put("fotoUrl", fotoUrl);

    List<Map<String, Object>> fotosInfo = new ArrayList<>();
    for (FotoProducto f : fotos) {
      Map<String, Object> fm = new LinkedHashMap<>();
      fm.put("id", f.getIdFoto());
      fm.put("url", f.getUrlFoto());
      fm.put("esPrincipal", f.getEsPrincipal());
      fotosInfo.add(fm);
    }
    m.put("fotos", fotosInfo);
    return m;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Producto create(@Valid @RequestBody Producto producto) {
    return service.create(producto);
  }

  @PutMapping("/{id}")
  public Producto update(@PathVariable Long id, @Valid @RequestBody Producto producto) {
    return service.update(id, producto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}
