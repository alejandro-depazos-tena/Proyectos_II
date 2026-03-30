package com.ufvshares.backend.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SecurityQuestionRequest(
    @NotBlank @Email String email
) {}