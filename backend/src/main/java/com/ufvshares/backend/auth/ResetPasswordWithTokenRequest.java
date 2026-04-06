package com.ufvshares.backend.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordWithTokenRequest(
    @NotBlank String token,
    @NotBlank @Size(min = 8, max = 100) String password
) {}