package com.ufvshares.backend.config;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Value("${app.upload.dir:./uploads}")
  private String uploadDir;

  @Value("${app.frontend.url:http://localhost:4321}")
  private String frontendUrls;

  private String[] resolveAllowedOrigins() {
    Set<String> allowed = new LinkedHashSet<>();

    for (String origin : frontendUrls.split(",")) {
      String trimmed = origin == null ? "" : origin.trim();
      if (!trimmed.isEmpty()) {
        allowed.add(trimmed);
      }
    }

    allowed.add("http://localhost:4321");
    allowed.add("http://127.0.0.1:4321");
    allowed.add("http://localhost");
    allowed.add("http://127.0.0.1");

    List<String> result = new ArrayList<>(allowed);
    return result.toArray(new String[0]);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    String[] allowedOrigins = resolveAllowedOrigins();

    registry
        .addMapping("/api/**")
        .allowedOrigins(allowedOrigins)
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(false);

    registry
        .addMapping("/uploads/**")
        .allowedOrigins(allowedOrigins)
        .allowedMethods("GET", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(false);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String absolutePath = Paths.get(uploadDir).toAbsolutePath().normalize().toUri().toString();
    if (!absolutePath.endsWith("/")) absolutePath += "/";
    registry.addResourceHandler("/uploads/**")
        .addResourceLocations(absolutePath);
  }
}
