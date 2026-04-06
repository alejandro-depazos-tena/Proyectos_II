package com.ufvshares.backend.admin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ufvshares.backend.auth.SessionRepository;
import com.ufvshares.backend.producto.ProductoRepository;
import com.ufvshares.backend.reporteusuario.ReporteUsuario;
import com.ufvshares.backend.reporteusuario.ReporteUsuarioService;
import com.ufvshares.backend.reporteproducto.ReporteProducto;
import com.ufvshares.backend.reporteproducto.ReporteProductoService;
import com.ufvshares.backend.usuario.Usuario;
import com.ufvshares.backend.usuario.UsuarioRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

  private final SessionRepository sessions;
  private final UsuarioRepository usuarios;
  private final ProductoRepository productos;
  private final ReporteProductoService reporteProductoService;
  private final ReporteUsuarioService reporteUsuarioService;
  private final AdminMetricsService metricsService;

  public AdminController(
      SessionRepository sessions,
      UsuarioRepository usuarios,
      ProductoRepository productos,
      ReporteProductoService reporteProductoService,
      ReporteUsuarioService reporteUsuarioService,
      AdminMetricsService metricsService) {
    this.sessions = sessions;
    this.usuarios = usuarios;
    this.productos = productos;
    this.reporteProductoService = reporteProductoService;
    this.reporteUsuarioService = reporteUsuarioService;
    this.metricsService = metricsService;
  }

  private Usuario resolveAdmin(String auth) {
    if (auth == null || !auth.startsWith("Bearer ")) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token requerido");
    }

    String token = auth.substring(7);
    String email = sessions.findEmailByToken(token)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesion invalida"));

    Usuario user = usuarios.findByCorreoIgnoreCase(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));

    if (!user.isEsAdmin()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
    }

    return user;
  }

  @GetMapping("/metrics")
  public Map<String, Object> getMetrics(@RequestHeader("Authorization") String auth) {
    resolveAdmin(auth);
    return metricsService.buildDashboard();
  }

  @GetMapping("/reportes-producto")
  public List<Map<String, Object>> getReportesProducto(@RequestHeader("Authorization") String auth) {
    resolveAdmin(auth);
    return reporteProductoService.findAll().stream().map(this::buildReporteProductoResumen).toList();
  }

  @GetMapping("/reportes-usuario")
  public List<Map<String, Object>> getReportesUsuario(@RequestHeader("Authorization") String auth) {
    resolveAdmin(auth);
    return reporteUsuarioService.findAll().stream().map(this::buildReporteUsuarioResumen).toList();
  }

  @PutMapping("/reportes-producto/{id}/cerrar")
  public ResponseEntity<Map<String, Object>> cerrarReporteProducto(@RequestHeader("Authorization") String auth, @PathVariable Long id) {
    resolveAdmin(auth);
    ReporteProducto reporte = reporteProductoService.close(id);
    return ResponseEntity.ok(buildReporteProductoResumen(reporte));
  }

  @PutMapping("/reportes-usuario/{id}/cerrar")
  public ResponseEntity<Map<String, Object>> cerrarReporteUsuario(@RequestHeader("Authorization") String auth, @PathVariable Long id) {
    resolveAdmin(auth);
    ReporteUsuario reporte = reporteUsuarioService.close(id);
    return ResponseEntity.ok(buildReporteUsuarioResumen(reporte));
  }

  private Map<String, Object> buildReporteProductoResumen(ReporteProducto reporte) {
    Map<String, Object> data = new LinkedHashMap<>();
    data.put("idReporte", reporte.getIdReporte());
    data.put("motivo", reporte.getMotivo());
    data.put("comentario", reporte.getComentario());
    data.put("estadoReporte", reporte.getEstadoReporte());
    data.put("fechaReporte", reporte.getFechaReporte());
    data.put("reportante", usuarios.findById(reporte.getIdUsuarioReportante()).map(this::safeUserSummary).orElse(null));
    data.put("producto", productos.findById(reporte.getIdProductoReportado()).orElse(null));
    return data;
  }

  private Map<String, Object> buildReporteUsuarioResumen(ReporteUsuario reporte) {
    Map<String, Object> data = new LinkedHashMap<>();
    data.put("idReporte", reporte.getIdReporte());
    data.put("motivo", reporte.getMotivo());
    data.put("comentario", reporte.getComentario());
    data.put("estadoReporte", reporte.getEstadoReporte());
    data.put("fechaReporte", reporte.getFechaReporte());
    data.put("reportante", usuarios.findById(reporte.getIdUsuarioReportante()).map(this::safeUserSummary).orElse(null));
    data.put("reportado", usuarios.findById(reporte.getIdUsuarioReportado()).map(this::safeUserSummary).orElse(null));
    return data;
  }

  private Map<String, Object> safeUserSummary(Usuario user) {
    Map<String, Object> data = new LinkedHashMap<>();
    data.put("idUsuario", user.getIdUsuario());
    data.put("nombre", user.getNombre());
    data.put("apellidos", user.getApellidos());
    data.put("correo", user.getCorreo());
    data.put("fotoPerfil", user.getFotoPerfil());
    data.put("esAdmin", user.isEsAdmin());
    return data;
  }
}
