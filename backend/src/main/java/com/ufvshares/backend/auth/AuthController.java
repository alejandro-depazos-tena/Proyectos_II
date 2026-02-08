package com.ufvshares.backend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService auth;

  public AuthController(AuthService auth) {
    this.auth = auth;
  }

  @PostMapping("/register")
  public AuthResponse register(@Valid @RequestBody AuthRequest request) {
    return auth.register(request.email(), request.password());
  }

  @PostMapping("/login")
  public AuthResponse login(@Valid @RequestBody AuthRequest request) {
    return auth.login(request.email(), request.password());
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleIllegalArg(IllegalArgumentException ex) {
    var code = ex.getMessage();
    if ("EMAIL_ALREADY_EXISTS".equals(code)) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(code));
    }
    if ("INVALID_CREDENTIALS".equals(code)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(code));
    }
    return ResponseEntity.badRequest().body(new ErrorResponse(code));
  }

  public record ErrorResponse(String error) {}
}
