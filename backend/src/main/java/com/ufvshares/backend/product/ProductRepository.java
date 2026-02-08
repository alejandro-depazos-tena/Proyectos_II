package com.ufvshares.backend.product;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {
  private final List<Product> products = new CopyOnWriteArrayList<>();

  public ProductRepository() {
    // Datos de ejemplo para desarrollo local
    products.add(new Product(UUID.randomUUID(), "Libro de Álgebra", "En buen estado", ProductType.SALE, 15.0, "demo@ufv.es", OffsetDateTime.now()));
    products.add(new Product(UUID.randomUUID(), "Calculadora científica", "Alquiler por semanas", ProductType.RENT, 5.0, "demo@ufv.es", OffsetDateTime.now()));
    products.add(new Product(UUID.randomUUID(), "Apuntes de Programación", "PDF + impresos", ProductType.SALE, 8.0, "demo@ufv.es", OffsetDateTime.now()));
  }

  public List<Product> findAll(ProductType type) {
    if (type == null) {
      return new ArrayList<>(products);
    }
    return products.stream().filter(p -> p.type() == type).toList();
  }

  public Product save(Product product) {
    products.add(product);
    return product;
  }
}
