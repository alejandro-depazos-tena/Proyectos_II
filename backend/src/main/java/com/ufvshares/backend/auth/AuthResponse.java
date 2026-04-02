package com.ufvshares.backend.auth;

public record AuthResponse(
    String token,
    String email,
    String nombre,
    boolean esAdmin
) {}
