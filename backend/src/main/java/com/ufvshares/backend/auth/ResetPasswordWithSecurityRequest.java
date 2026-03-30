package com.ufvshares.backend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordWithSecurityRequest(
    @NotBlank @Email String email,
    @NotBlank String securityAnswer,
    @NotBlank @Size(min = 8, max = 100) String password
) {}