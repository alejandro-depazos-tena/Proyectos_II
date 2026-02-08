package com.ufvshares.backend.auth;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository users;
  private final SessionRepository sessions;
  private final PasswordEncoder encoder = new BCryptPasswordEncoder();

  public AuthService(UserRepository users, SessionRepository sessions) {
    this.users = users;
    this.sessions = sessions;
  }

  public AuthResponse register(String email, String password) {
    var normalized = normalize(email);
    if (users.findByEmail(normalized).isPresent()) {
      throw new IllegalArgumentException("EMAIL_ALREADY_EXISTS");
    }

    var user = new User(UUID.randomUUID(), normalized, encoder.encode(password));
    users.save(user);

    var token = UUID.randomUUID().toString();
    sessions.put(token, user.email());
    return new AuthResponse(token, user.email());
  }

  public AuthResponse login(String email, String password) {
    var normalized = normalize(email);
    var user = users.findByEmail(normalized).orElseThrow(() -> new IllegalArgumentException("INVALID_CREDENTIALS"));

    if (!encoder.matches(password, user.passwordHash())) {
      throw new IllegalArgumentException("INVALID_CREDENTIALS");
    }

    var token = UUID.randomUUID().toString();
    sessions.put(token, user.email());
    return new AuthResponse(token, user.email());
  }

  private String normalize(String email) {
    return email == null ? null : email.trim().toLowerCase();
  }
}
