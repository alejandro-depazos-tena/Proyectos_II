package com.ufvshares.backend.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ufvshares.backend.cambioperfil.EmailService;
import com.ufvshares.backend.usuario.Usuario;
import com.ufvshares.backend.usuario.UsuarioRepository;

@Service
public class AuthService {
  private final UsuarioRepository usuarios;
  private final SessionRepository sessions;
  private final PasswordResetTokenRepository resetTokens;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;

  @Value("${app.auth.password-reset-token-ttl-minutes:30}")
  private long passwordResetTokenTtlMinutes;

  @Value("${app.mail.dev-fallback:true}")
  private boolean devMailFallback;

  @Value("${app.frontend.url:http://localhost:4321}")
  private String frontendUrl;

  public AuthService(
      UsuarioRepository usuarios,
      SessionRepository sessions,
      PasswordResetTokenRepository resetTokens,
      PasswordEncoder passwordEncoder,
      EmailService emailService) {
    this.usuarios = usuarios;
    this.sessions = sessions;
    this.resetTokens = resetTokens;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  public AuthResponse register(RegisterRequest req) {
    var normalized = normalize(req.email());

    if (usuarios.findByCorreoIgnoreCase(normalized).isPresent()) {
      throw new IllegalArgumentException("EMAIL_ALREADY_EXISTS");
    }
    if (usuarios.findByDniIgnoreCase(req.dni().trim()).isPresent()) {
      throw new IllegalArgumentException("DNI_ALREADY_EXISTS");
    }
    if (usuarios.findByTelefono(req.telefono().trim()).isPresent()) {
      throw new IllegalArgumentException("TELEFONO_ALREADY_EXISTS");
    }

    Usuario usuario = new Usuario();
    usuario.setNombre(req.nombre().trim());
    usuario.setApellidos(req.apellidos().trim());
    usuario.setCorreo(normalized);
    usuario.setTelefono(req.telefono().trim());
    usuario.setDni(req.dni().trim().toUpperCase());
    usuario.setPasswordHash(passwordEncoder.encode(req.password()));
    usuario.setPreguntaSeguridad(req.preguntaSeguridad().trim());
    usuario.setRespuestaSeguridadHash(hashSha256(normalizeSecurityAnswer(req.respuestaSeguridad())));
    usuarios.save(usuario);

    var token = UUID.randomUUID().toString();
    sessions.put(token, normalized);
    return new AuthResponse(token, normalized, req.nombre().trim(), usuario.isEsAdmin());
  }

  public AuthResponse login(String email, String password) {
    var normalized = normalize(email);
    var usuario = usuarios.findByCorreoIgnoreCase(normalized)
        .orElseThrow(() -> new IllegalArgumentException("INVALID_CREDENTIALS"));

    String storedHash = usuario.getPasswordHash();
    if (storedHash == null || storedHash.isBlank()) {
      throw new IllegalArgumentException("INVALID_CREDENTIALS");
    }

    boolean valid;
    try {
      valid = passwordEncoder.matches(password, storedHash);
    } catch (IllegalArgumentException ex) {
      valid = false;
    }
    if (!valid && hashSha256(password).equalsIgnoreCase(storedHash)) {
      usuario.setPasswordHash(passwordEncoder.encode(password));
      usuarios.save(usuario);
      valid = true;
    }

    if (!valid) {
      throw new IllegalArgumentException("INVALID_CREDENTIALS");
    }

    var token = UUID.randomUUID().toString();
    sessions.put(token, normalized);
    return new AuthResponse(token, normalized, usuario.getNombre(), usuario.isEsAdmin());
  }

  public String getSecurityQuestion(String email) {
    var normalized = normalize(email);
    var usuario = usuarios.findByCorreoIgnoreCase(normalized)
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    String question = usuario.getPreguntaSeguridad();
    if (question == null || question.isBlank()) {
      throw new IllegalArgumentException("SECURITY_QUESTION_NOT_CONFIGURED");
    }
    return question;
  }

  public void resetPasswordWithSecurityQuestion(String email, String securityAnswer, String newPassword) {
    var normalized = normalize(email);
    var usuario = usuarios.findByCorreoIgnoreCase(normalized)
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    String storedHash = usuario.getRespuestaSeguridadHash();
    if (storedHash == null || storedHash.isBlank()) {
      throw new IllegalArgumentException("SECURITY_QUESTION_NOT_CONFIGURED");
    }

    String answerHash = hashSha256(normalizeSecurityAnswer(securityAnswer));
    if (!answerHash.equalsIgnoreCase(storedHash)) {
      throw new IllegalArgumentException("INVALID_SECURITY_ANSWER");
    }

    usuario.setPasswordHash(passwordEncoder.encode(newPassword));
    usuarios.save(usuario);
    sessions.deleteByEmail(usuario.getCorreo());
  }

  public String requestPasswordResetByEmail(String email) {
    var normalized = normalize(email);
    var usuario = usuarios.findByCorreoIgnoreCase(normalized)
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    resetTokens.deleteByEmailIgnoreCase(normalized);

    String token = UUID.randomUUID().toString().replace("-", "");
    LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(passwordResetTokenTtlMinutes);
    resetTokens.save(new PasswordResetToken(token, normalized, expiresAt));

    String resetUrl = frontendUrl + "/forgot-password?token=" + token;

    try {
      emailService.enviarResetPassword(usuario.getCorreo(), buildFullName(usuario), token);
      return null;
    } catch (Exception ex) {
      System.out.println("[MAIL ERROR] destinatario=" + normalized + " causa=" + ex.getClass().getName() + " mensaje=" + ex.getMessage());
      if (ex.getCause() != null) {
        System.out.println("[MAIL ERROR CAUSE] " + ex.getCause().getClass().getName() + ": " + ex.getCause().getMessage());
      }
      if (devMailFallback) {
        System.out.println("[DEV MAIL FALLBACK] Reset URL for " + normalized + ": " + resetUrl);
        return resetUrl;
      }
      throw new IllegalStateException("MAIL_SEND_FAILED", ex);
    }
  }

  public void resetPasswordWithToken(String token, String newPassword) {
    LocalDateTime now = LocalDateTime.now();
    PasswordResetToken resetToken = resetTokens.findByTokenAndUsedAtIsNullAndExpiresAtAfter(token, now)
        .orElseThrow(() -> new IllegalArgumentException("INVALID_RESET_TOKEN"));

    Usuario usuario = usuarios.findByCorreoIgnoreCase(resetToken.getEmail())
        .orElseThrow(() -> new IllegalArgumentException("USER_NOT_FOUND"));

    usuario.setPasswordHash(passwordEncoder.encode(newPassword));
    usuarios.save(usuario);

    resetToken.setUsedAt(now);
    resetTokens.save(resetToken);
    sessions.deleteByEmail(usuario.getCorreo());
  }

  public void logout(String token) {
    if (token == null || token.isBlank()) {
      return;
    }
    sessions.deleteByToken(token);
  }

  private String normalizeSecurityAnswer(String answer) {
    return answer == null ? "" : answer.trim().toLowerCase(Locale.ROOT);
  }

  private String buildFullName(Usuario usuario) {
    return (usuario.getNombre() + " " + usuario.getApellidos()).trim();
  }

  private String hashSha256(String rawValue) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashBytes = digest.digest(rawValue.getBytes(StandardCharsets.UTF_8));
      StringBuilder hex = new StringBuilder(hashBytes.length * 2);
      for (byte hashByte : hashBytes) {
        hex.append(String.format("%02x", hashByte));
      }
      return hex.toString();
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("SHA-256 no disponible", ex);
    }
  }

  private String normalize(String email) {
    return email == null ? null : email.trim().toLowerCase();
  }
}
