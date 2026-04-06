package com.ufvshares.backend.auth;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SessionJpaRepository extends JpaRepository<Session, String> {
    Optional<Session> findByToken(String token);

    @Transactional
    void deleteByEmail(String email);

    @Transactional
    void deleteByToken(String token);

    @Transactional
    void deleteByExpiresAtBefore(LocalDateTime expiresAt);
}
