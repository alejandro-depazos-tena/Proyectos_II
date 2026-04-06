package com.ufvshares.backend.auth;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {

  Optional<PasswordResetToken> findByTokenAndUsedAtIsNullAndExpiresAtAfter(String token, LocalDateTime now);

  List<PasswordResetToken> findByEmailIgnoreCase(String email);

  @Transactional
  void deleteByEmailIgnoreCase(String email);
}