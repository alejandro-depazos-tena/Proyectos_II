package com.ufvshares.backend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @Email
    @Pattern(
        regexp = "(?i)^[a-z0-9._%+-]+@(ufv\\.es|alumnos\\.ufv\\.es)$",
        message = "Solo se permiten correos con dominio ufv.es o alumnos.ufv.es")
    @NotBlank String email,
    @NotBlank @Size(min = 8, max = 100) String password,
    @NotBlank @Size(min = 2, max = 100) String nombre,
    @NotBlank @Size(min = 2, max = 150) String apellidos,
    @NotBlank @Pattern(regexp = "^[6789]\\d{8}$", message = "Teléfono español no válido") String telefono,
    @NotBlank @Pattern(regexp = "^(\\d{8}[A-Za-z]|[XYZxyz]\\d{7}[A-Za-z])$", message = "DNI o NIE no válido") String dni
) {}
