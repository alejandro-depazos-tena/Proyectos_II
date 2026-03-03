package com.ufvshares.backend.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionJpaRepository extends JpaRepository<Session, String> {
    Optional<Session> findByToken(String token);
}
