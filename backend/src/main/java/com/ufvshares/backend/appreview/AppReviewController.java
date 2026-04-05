package com.ufvshares.backend.appreview;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ufvshares.backend.auth.SessionRepository;
import com.ufvshares.backend.usuario.Usuario;
import com.ufvshares.backend.usuario.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AppReviewController {

  private final AppReviewRepository reviews;
  private final SessionRepository sessions;
  private final UsuarioRepository usuarios;

  public AppReviewController(AppReviewRepository reviews, SessionRepository sessions, UsuarioRepository usuarios) {
    this.reviews = reviews;
    this.sessions = sessions;
    this.usuarios = usuarios;
  }

  private Usuario resolveUser(String auth) {
    if (auth == null || !auth.startsWith("Bearer ")) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token requerido");
    }
    String token = auth.substring(7);
    String email = sessions.findEmailByToken(token)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesion invalida"));
    return usuarios.findByCorreoIgnoreCase(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));
  }

  @GetMapping("/app-reviews")
  public List<Map<String, Object>> listReviews() {
    return reviews.findTop50ByOrderByFechaActualizacionDesc().stream().map(this::toMap).toList();
  }

  @PostMapping("/me/app-reviews")
  public Map<String, Object> upsertReview(
      @RequestHeader("Authorization") String auth,
      @Valid @RequestBody AppReviewRequest request) {

    Usuario user = resolveUser(auth);
    AppReview review = reviews.findByIdUsuario(user.getIdUsuario()).orElseGet(AppReview::new);

    review.setIdUsuario(user.getIdUsuario());
    review.setNombreUsuario((user.getNombre() == null || user.getNombre().isBlank())
        ? "Usuario"
        : user.getNombre().trim());
    review.setPuntuacion(request.puntuacion());
    review.setComentario(request.comentario().trim());

    AppReview saved = reviews.save(review);

    Map<String, Object> response = new LinkedHashMap<>();
    response.put("ok", true);
    response.put("review", toMap(saved));
    return response;
  }

  @DeleteMapping("/me/app-reviews")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteMyReview(@RequestHeader("Authorization") String auth) {
    Usuario user = resolveUser(auth);
    reviews.deleteByIdUsuario(user.getIdUsuario());
  }

  private Map<String, Object> toMap(AppReview review) {
    Map<String, Object> out = new LinkedHashMap<>();
    out.put("idReview", review.getIdReview());
    out.put("idUsuario", review.getIdUsuario());
    out.put("nombreUsuario", review.getNombreUsuario());
    out.put("puntuacion", review.getPuntuacion());
    out.put("comentario", review.getComentario());
    out.put("fechaActualizacion", review.getFechaActualizacion());
    return out;
  }
}
