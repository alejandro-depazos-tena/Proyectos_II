package com.ufvshares.backend.product;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Product(
    UUID id,
    String title,
    String description,
    ProductType type,
    double price,
    String ownerEmail,
    OffsetDateTime createdAt
) {}
