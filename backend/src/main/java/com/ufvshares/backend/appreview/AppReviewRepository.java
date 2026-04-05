package com.ufvshares.backend.appreview;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppReviewRepository extends JpaRepository<AppReview, Long> {

  List<AppReview> findTop50ByOrderByFechaActualizacionDesc();

  Optional<AppReview> findByIdUsuario(Long idUsuario);

  void deleteByIdUsuario(Long idUsuario);
}
