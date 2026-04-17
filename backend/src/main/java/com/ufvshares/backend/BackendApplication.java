package com.ufvshares.backend;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

@SpringBootApplication
public class BackendApplication {
  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(BackendApplication.class);
    app.addInitializers(context -> applyDatabaseUrlCompatibility(context.getEnvironment()));
    app.run(args);
  }

  private static void applyDatabaseUrlCompatibility(ConfigurableEnvironment env) {
    if (hasText(env.getProperty("DB_URL"))) {
      return;
    }

    String rawDatabaseUrl = env.getProperty("DATABASE_URL");
    if (!hasText(rawDatabaseUrl)) {
      return;
    }

    ParsedDatabaseUrl parsed = parseDatabaseUrl(rawDatabaseUrl);
    if (parsed == null) {
      return;
    }

    Map<String, Object> overrides = new HashMap<>();
    overrides.put("DB_URL", parsed.jdbcUrl());

    if (!hasText(env.getProperty("DB_DRIVER"))) {
      overrides.put("DB_DRIVER", "org.postgresql.Driver");
    }
    if (!hasText(env.getProperty("JPA_DIALECT"))) {
      overrides.put("JPA_DIALECT", "org.hibernate.dialect.PostgreSQLDialect");
    }
    if (!hasText(env.getProperty("DB_USER")) && hasText(parsed.username())) {
      overrides.put("DB_USER", parsed.username());
    }
    if (!hasText(env.getProperty("DB_PASSWORD")) && parsed.password() != null) {
      overrides.put("DB_PASSWORD", parsed.password());
    }

    env.getPropertySources()
        .addFirst(new MapPropertySource("renderDatabaseUrlCompatibility", overrides));
  }

  private static ParsedDatabaseUrl parseDatabaseUrl(String rawUrl) {
    String normalized = rawUrl == null ? "" : rawUrl.trim();
    if (!hasText(normalized)) {
      return null;
    }

    if (normalized.startsWith("postgres://")) {
      normalized = "postgresql://" + normalized.substring("postgres://".length());
    }

    if (normalized.startsWith("jdbc:postgresql://")) {
      return new ParsedDatabaseUrl(normalized, null, null);
    }

    if (!normalized.startsWith("postgresql://")) {
      return null;
    }

    URI uri = URI.create(normalized);
    if (!hasText(uri.getHost())) {
      return null;
    }

    int port = uri.getPort() > 0 ? uri.getPort() : 5432;
    String path = uri.getPath();
    if (!hasText(path) || path.length() <= 1) {
      return null;
    }

    String database = path.substring(1);
    String query = uri.getRawQuery();
    String jdbcUrl = "jdbc:postgresql://" + uri.getHost() + ":" + port + "/" + database;
    if (hasText(query)) {
      jdbcUrl = jdbcUrl + "?" + query;
    }

    String username = null;
    String password = null;
    String userInfo = uri.getUserInfo();
    if (hasText(userInfo)) {
      String[] parts = userInfo.split(":", 2);
      username = parts[0];
      password = parts.length > 1 ? parts[1] : "";
    }

    return new ParsedDatabaseUrl(jdbcUrl, username, password);
  }

  private static boolean hasText(String value) {
    return value != null && !value.isBlank();
  }

  private record ParsedDatabaseUrl(String jdbcUrl, String username, String password) {}
}
