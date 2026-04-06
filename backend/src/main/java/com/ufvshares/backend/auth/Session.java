package com.ufvshares.backend.auth;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @Column(length = 36, nullable = false)
    private String token;

    @Column(nullable = false)
    private String email;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    protected Session() {}

    public Session(String token, String email, LocalDateTime expiresAt) {
        this.token = token;
        this.email = email;
        this.expiresAt = expiresAt;
    }

    public String getToken()  { return token; }
    public String getEmail()  { return email; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
}
