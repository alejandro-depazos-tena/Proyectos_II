package com.ufvshares.backend.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ufvshares.backend.appreview.AppReview;
import com.ufvshares.backend.appreview.AppReviewRepository;
import com.ufvshares.backend.usuario.Usuario;
import com.ufvshares.backend.usuario.UsuarioRepository;

@Component
@Order(2)
public class AppReviewSeedDataLoader implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(AppReviewSeedDataLoader.class);

  private final AppReviewRepository appReviewRepository;
  private final UsuarioRepository usuarioRepository;

  @Value("${app.seed.app-reviews.enabled:true}")
  private boolean appReviewSeedEnabled;

  @Value("${app.seed.app-reviews.overwrite-existing:false}")
  private boolean overwriteExistingReviews;

  public AppReviewSeedDataLoader(AppReviewRepository appReviewRepository, UsuarioRepository usuarioRepository) {
    this.appReviewRepository = appReviewRepository;
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  @Transactional
  public void run(String... args) {
    if (!appReviewSeedEnabled) {
      return;
    }

    if (usuarioRepository.count() == 0) {
      log.info("Seed de reseñas omitido: no hay usuarios cargados todavia.");
      return;
    }

    List<SeedReview> seeds = defaultSeeds();
    List<AppReview> toSave = new ArrayList<>();
    int created = 0;
    int updated = 0;
    int preserved = 0;

    for (SeedReview seed : seeds) {
      Usuario user = usuarioRepository.findById(seed.userId()).orElse(null);
      if (user == null) {
        continue;
      }

      AppReview review = appReviewRepository.findByIdUsuario(user.getIdUsuario()).orElse(null);
      if (review == null) {
        review = new AppReview();
        review.setIdUsuario(user.getIdUsuario());
        created += 1;
      } else if (overwriteExistingReviews) {
        updated += 1;
      } else {
        preserved += 1;
        continue;
      }

      review.setIdUsuario(user.getIdUsuario());
      review.setNombreUsuario((user.getNombre() == null || user.getNombre().isBlank()) ? "Usuario" : user.getNombre().trim());
      review.setPuntuacion(seed.rating());
      review.setComentario(seed.comment());
      toSave.add(review);
    }

    if (toSave.isEmpty()) {
      if (preserved > 0) {
        log.info("Seed de reseñas omitido: ya existian reseñas y overwrite esta desactivado.");
      } else {
        log.info("Seed de reseñas omitido: no se pudo asociar ninguna reseña a usuarios existentes.");
      }
      return;
    }

    appReviewRepository.saveAll(toSave);
    log.info("Seed automatico de reseñas aplicado: {} creadas, {} actualizadas y {} preservadas.", created, updated, preserved);
  }

  private List<SeedReview> defaultSeeds() {
    return List.of(
        new SeedReview(1L, 5, "La app ayuda mucho a mover material entre estudiantes de forma rapida."),
        new SeedReview(2L, 4, "Muy util para alquilar cosas de clase. La experiencia general es buena."),
        new SeedReview(3L, 5, "Interfaz clara y facil de entender desde el primer uso."),
        new SeedReview(4L, 4, "He vendido varias cosas sin complicaciones. Muy buen flujo."),
        new SeedReview(5L, 5, "La parte de chat funciona genial para cerrar acuerdos."),
        new SeedReview(6L, 4, "Publicar anuncios es rapido y directo. Lo uso bastante."),
        new SeedReview(7L, 5, "Me sorprendio para bien, especialmente en movil."),
        new SeedReview(8L, 4, "Buen proyecto para la comunidad universitaria."),
        new SeedReview(9L, 5, "Favoritos y perfil estan bien resueltos y son utiles."),
        new SeedReview(10L, 3, "Cumple su objetivo, aunque todavia hay detalles por pulir."),
        new SeedReview(11L, 4, "Navegacion intuitiva y rendimiento correcto en general."),
        new SeedReview(12L, 5, "Muy recomendable para estudiantes que comparten piso o carrera."),
        new SeedReview(13L, 4, "Buena base de producto y buena sensacion de uso."),
        new SeedReview(14L, 5, "App muy completa para el contexto UFV, buena experiencia."),
        new SeedReview(15L, 4, "Contactar y cerrar trato es sencillo gracias al chat."),
        new SeedReview(16L, 5, "La he usado varios dias y cumple muy bien."),
        new SeedReview(17L, 4, "Proyecto solido. El buscador y favoritos aportan bastante."),
        new SeedReview(18L, 5, "Rapida, clara y practica para el dia a dia universitario."),
        new SeedReview(19L, 4, "Muy buen punto de partida, con margen para crecer."),
        new SeedReview(20L, 5, "La seguiria usando sin duda, aporta valor real.")
    );
  }

  private record SeedReview(Long userId, int rating, String comment) {
  }
}
