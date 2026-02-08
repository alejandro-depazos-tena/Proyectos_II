package com.ufvshares.backend.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCreateRequest(
    @NotBlank String title,
    @NotBlank String description,
    @NotNull ProductType type,
    @DecimalMin(value = "0.0", inclusive = true) double price
) {}
