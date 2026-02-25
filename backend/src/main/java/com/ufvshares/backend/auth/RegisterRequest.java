package com.ufvshares.backend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @Email @NotBlank String email,
    @NotBlank String password,
    @NotBlank @Size(max = 100) String nombre,
    @NotBlank @Size(max = 150) String apellidos,
    @NotBlank @Size(max = 20)  String telefono,
    @NotBlank @Size(max = 20)  String dni
) {}
