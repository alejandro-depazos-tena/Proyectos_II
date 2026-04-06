package com.ufvshares.backend.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestHeader;
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
  public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
    return auth.register(request);
  }

  @PostMapping("/login")
  public AuthResponse login(@Valid @RequestBody AuthRequest request) {
    return auth.login(request.email(), request.password());
  }

  @PostMapping("/security-question")
  public SecurityQuestionResponse securityQuestion(@Valid @RequestBody SecurityQuestionRequest request) {
    String question = auth.getSecurityQuestion(request.email());
    return new SecurityQuestionResponse(question);
  }

  @PostMapping("/forgot-password")
  public ForgotPasswordResponse forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    String resetUrl = auth.requestPasswordResetByEmail(request.email());
    return new ForgotPasswordResponse(true, resetUrl);
  }

  @PostMapping("/reset-password-token")
  public OkResponse resetPasswordToken(@Valid @RequestBody ResetPasswordWithTokenRequest request) {
    auth.resetPasswordWithToken(request.token(), request.password());
    return new OkResponse(true);
  }

  @PostMapping("/reset-password-security")
  public OkResponse resetPasswordSecurity(@Valid @RequestBody ResetPasswordWithSecurityRequest request) {
    auth.resetPasswordWithSecurityQuestion(request.email(), request.securityAnswer(), request.password());
    return new OkResponse(true);
  }

  @PostMapping("/logout")
  public OkResponse logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
    auth.logout(extractToken(authHeader));
    return new OkResponse(true);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> handleIllegalArg(IllegalArgumentException ex) {
    var code = ex.getMessage();
    if ("EMAIL_ALREADY_EXISTS".equals(code) ||
        "DNI_ALREADY_EXISTS".equals(code) ||
        "TELEFONO_ALREADY_EXISTS".equals(code)) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(code));
    }
    if ("INVALID_CREDENTIALS".equals(code)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(code));
    }
    if ("USER_NOT_FOUND".equals(code)
        || "SECURITY_QUESTION_NOT_CONFIGURED".equals(code)
        || "INVALID_SECURITY_ANSWER".equals(code)
        || "INVALID_RESET_TOKEN".equals(code)
        || "RESET_TOKEN_EXPIRED".equals(code)) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(code));
    }
    return ResponseEntity.badRequest().body(new ErrorResponse(code));
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<?> handleIllegalState(IllegalStateException ex) {
    var code = ex.getMessage();
    if ("MAIL_SEND_FAILED".equals(code)) {
      return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse(code));
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(code));
  }

  private String extractToken(String authHeader) {
    if (authHeader == null || authHeader.isBlank()) {
      return null;
    }
    return authHeader.startsWith("Bearer ") ? authHeader.substring(7).trim() : authHeader.trim();
  }

  public record ErrorResponse(String error) {}
  public record OkResponse(boolean ok) {}
  public record ForgotPasswordResponse(boolean ok, String resetUrl) {}
}
