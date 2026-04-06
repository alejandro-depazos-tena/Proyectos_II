package com.ufvshares.backend.auth;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

  @Id
  @Column(length = 80, nullable = false)
  private String token;

  @Column(nullable = false, length = 150)
  private String email;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime expiresAt;

  @Column
  private LocalDateTime usedAt;

  protected PasswordResetToken() {}

  public PasswordResetToken(String token, String email, LocalDateTime expiresAt) {
    this.token = token;
    this.email = email;
    this.createdAt = LocalDateTime.now();
    this.expiresAt = expiresAt;
  }

  public String getToken() {
    return token;
  }

  public String getEmail() {
    return email;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  public LocalDateTime getUsedAt() {
    return usedAt;
  }

  public void setUsedAt(LocalDateTime usedAt) {
    this.usedAt = usedAt;
  }
}