package com.ufvshares.backend.auth;

import java.util.UUID;

public record User(
    UUID id,
    String email,
    String passwordHash
) {}
