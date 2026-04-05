package com.ufvshares.backend.appreview;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AppReviewRequest(
    @Min(1) @Max(5) Integer puntuacion,
    @NotBlank @Size(min = 10, max = 280) String comentario
) {
}
