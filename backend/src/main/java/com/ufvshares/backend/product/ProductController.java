package com.ufvshares.backend.product;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufvshares.backend.auth.SessionRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {
  private final ProductRepository products;
  private final SessionRepository sessions;

  public ProductController(ProductRepository products, SessionRepository sessions) {
    this.products = products;
    this.sessions = sessions;
  }

  @GetMapping
  public List<Product> list(@RequestParam(name = "type", required = false) String type) {
    return products.findAll(parseType(type));
  }

  @PostMapping
  public ResponseEntity<?> create(
      @RequestHeader(name = "Authorization", required = false) String authorization,
      @Valid @RequestBody ProductCreateRequest request
  ) {
    var token = extractBearerToken(authorization);
    if (token == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("MISSING_TOKEN"));
    }

    var ownerEmail = sessions.findEmailByToken(token).orElse(null);
    if (ownerEmail == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("INVALID_TOKEN"));
    }

    var product = new Product(
        UUID.randomUUID(),
        request.title(),
        request.description(),
        request.type(),
        request.price(),
        ownerEmail,
        OffsetDateTime.now()
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(products.save(product));
  }

  private ProductType parseType(String raw) {
    if (raw == null || raw.isBlank()) {
      return null;
    }
    var v = raw.trim().toLowerCase();
    if ("sale".equals(v) || "venta".equals(v)) {
      return ProductType.SALE;
    }
    if ("rent".equals(v) || "alquiler".equals(v)) {
      return ProductType.RENT;
    }
    try {
      return ProductType.valueOf(raw.trim().toUpperCase());
    } catch (Exception ex) {
      return null;
    }
  }

  private String extractBearerToken(String authorization) {
    if (authorization == null) {
      return null;
    }
    var prefix = "Bearer ";
    if (!authorization.startsWith(prefix)) {
      return null;
    }
    var token = authorization.substring(prefix.length()).trim();
    return token.isBlank() ? null : token;
  }

  public record ErrorResponse(String error) {}
}
