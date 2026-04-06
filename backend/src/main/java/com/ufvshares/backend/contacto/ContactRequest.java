package com.ufvshares.backend.contacto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContactRequest(
    @NotBlank @Size(min = 2, max = 100) String nombre,
    @NotBlank @Size(min = 2, max = 150) String apellido,
    @NotBlank
    @Email
    @Pattern(
        regexp = "(?i)^[a-z0-9._%+-]+@(ufv\\.es|alumnos\\.ufv\\.es)$",
        message = "Solo se permiten correos con dominio ufv.es o alumnos.ufv.es") String correo,
    @NotBlank @Size(min = 3, max = 150) String motivo,
    @NotBlank @Size(min = 10, max = 4000) String mensaje
) {}