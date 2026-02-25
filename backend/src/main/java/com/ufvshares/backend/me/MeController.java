package com.ufvshares.backend.me;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ufvshares.backend.auth.SessionRepository;
import com.ufvshares.backend.producto.Producto;
import com.ufvshares.backend.producto.ProductoRepository;
import com.ufvshares.backend.usuario.Usuario;
import com.ufvshares.backend.usuario.UsuarioRepository;

@RestController
@RequestMapping("/api/me")
public class MeController {

  private final SessionRepository sessions;
  private final UsuarioRepository usuarios;
  private final ProductoRepository productos;

  public MeController(SessionRepository sessions, UsuarioRepository usuarios, ProductoRepository productos) {
    this.sessions = sessions;
    this.usuarios = usuarios;
    this.productos = productos;
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
    return Map.of(
        "idUsuario", u.getIdUsuario(),
        "nombre", u.getNombre(),
        "apellidos", u.getApellidos(),
        "correo", u.getCorreo(),
        "telefono", u.getTelefono(),
        "dni", u.getDni()
    );
  }

  @GetMapping("/productos")
  public List<Producto> getMisProductos(@RequestHeader("Authorization") String auth) {
    Usuario u = resolveUser(auth);
    return productos.findByIdPropietario(u.getIdUsuario());
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
}
