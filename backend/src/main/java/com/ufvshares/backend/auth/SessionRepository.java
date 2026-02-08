package com.ufvshares.backend.auth;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class SessionRepository {
  private final Map<String, String> tokenToEmail = new ConcurrentHashMap<>();

  public void put(String token, String email) {
    tokenToEmail.put(token, email);
  }

  public Optional<String> findEmailByToken(String token) {
    return Optional.ofNullable(tokenToEmail.get(token));
  }
}
