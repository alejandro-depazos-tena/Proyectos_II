package com.ufvshares.backend.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Value("${app.upload.dir:./uploads}")
  private String uploadDir;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/api/**")
        .allowedOrigins(
            "http://localhost:4321",
            "http://127.0.0.1:4321"
        )
        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        .allowedHeaders("*")
        .allowCredentials(false);

    registry
        .addMapping("/uploads/**")
        .allowedOrigins(
            "http://localhost:4321",
            "http://127.0.0.1:4321"
        )
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
