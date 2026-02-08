package com.ufvshares.backend.auth;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
  private final Map<String, User> byEmail = new ConcurrentHashMap<>();

  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(byEmail.get(normalize(email)));
  }

  public User save(User user) {
    byEmail.put(normalize(user.email()), user);
    return user;
  }

  private String normalize(String email) {
    return email == null ? null : email.trim().toLowerCase();
  }
}
