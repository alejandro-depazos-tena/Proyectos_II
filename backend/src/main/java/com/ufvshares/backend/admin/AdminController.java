package com.ufvshares.backend.admin;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ufvshares.backend.auth.SessionRepository;
import com.ufvshares.backend.usuario.Usuario;
import com.ufvshares.backend.usuario.UsuarioRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

  private final SessionRepository sessions;
  private final UsuarioRepository usuarios;
  private final AdminMetricsService metricsService;

  public AdminController(SessionRepository sessions, UsuarioRepository usuarios, AdminMetricsService metricsService) {
    this.sessions = sessions;
    this.usuarios = usuarios;
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
}
