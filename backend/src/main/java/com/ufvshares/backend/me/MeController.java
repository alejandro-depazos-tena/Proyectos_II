package com.ufvshares.backend.me;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ufvshares.backend.auth.SessionRepository;
import com.ufvshares.backend.fotoproducto.FotoProducto;
import com.ufvshares.backend.fotoproducto.FotoProductoRepository;
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
  private final FotoProductoRepository fotosRepo;

  @Value("${app.upload.dir:./uploads}")
  private String uploadDir;

  public MeController(SessionRepository sessions, UsuarioRepository usuarios,
      ProductoRepository productos, FotoProductoRepository fotosRepo) {
    this.sessions = sessions;
    this.usuarios = usuarios;
    this.productos = productos;
    this.fotosRepo = fotosRepo;
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
      List<FotoProducto> fotos = fotosRepo.findByIdProductoOrderByEsPrincipalDesc(p.getIdProducto());
      if (!fotos.isEmpty()) {
        m.put("fotoUrl", fotos.get(0).getUrlFoto());
      }
      result.add(m);
    }
    return result;
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
}
