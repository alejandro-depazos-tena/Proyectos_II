package com.ufvshares.backend;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

  @GetMapping("/hello")
  public Map<String, Object> hello() {
    return Map.of(
        "message", "Hola desde el backend 👋",
        "timestamp", OffsetDateTime.now().toString()
    );
  }
}
