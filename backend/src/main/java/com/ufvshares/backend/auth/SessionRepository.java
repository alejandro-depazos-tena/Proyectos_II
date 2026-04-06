package com.ufvshares.backend.auth;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Thin facade over {@link SessionJpaRepository} so the rest of the codebase
 * keeps the same API while sessions are now persisted in the database and
 * survive backend restarts.
 */
@Repository
public class SessionRepository {

  private final SessionJpaRepository jpa;

  @Value("${app.auth.session-ttl-hours:720}")
  private long sessionTtlHours;

  public SessionRepository(SessionJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Transactional
  public void put(String token, String email) {
    jpa.save(new Session(token, email, LocalDateTime.now().plusHours(sessionTtlHours)));
  }

  public Optional<String> findEmailByToken(String token) {
    jpa.deleteByExpiresAtBefore(LocalDateTime.now());
    return jpa.findByToken(token)
        .filter(session -> session.getExpiresAt() != null && session.getExpiresAt().isAfter(LocalDateTime.now()))
        .map(Session::getEmail);
  }

  public void deleteByEmail(String email) {
    jpa.deleteByEmail(email);
  }

  public void deleteByToken(String token) {
    jpa.deleteByToken(token);
  }
}
