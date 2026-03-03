package com.ufvshares.backend.auth;

import java.util.Optional;

import org.springframework.stereotype.Repository;

/**
 * Thin facade over {@link SessionJpaRepository} so the rest of the codebase
 * keeps the same API while sessions are now persisted in the database and
 * survive backend restarts.
 */
@Repository
public class SessionRepository {

  private final SessionJpaRepository jpa;

  public SessionRepository(SessionJpaRepository jpa) {
    this.jpa = jpa;
  }

  public void put(String token, String email) {
    jpa.save(new Session(token, email));
  }

  public Optional<String> findEmailByToken(String token) {
    return jpa.findByToken(token).map(Session::getEmail);
  }
}
