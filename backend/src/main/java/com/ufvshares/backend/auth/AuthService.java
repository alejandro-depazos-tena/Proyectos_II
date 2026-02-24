package com.ufvshares.backend.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ufvshares.backend.usuario.Usuario;
import com.ufvshares.backend.usuario.UsuarioRepository;

@Service
public class AuthService {
  private final UsuarioRepository usuarios;
  private final SessionRepository sessions;

  public AuthService(UsuarioRepository usuarios, SessionRepository sessions) {
    this.usuarios = usuarios;
    this.sessions = sessions;
  }

  public AuthResponse register(String email, String password) {
    var normalized = normalize(email);
    if (usuarios.findByCorreoIgnoreCase(normalized).isPresent()) {
      throw new IllegalArgumentException("EMAIL_ALREADY_EXISTS");
    }

    Usuario usuario = new Usuario();
    usuario.setNombre("Nuevo");
    usuario.setApellidos("Usuario");
    usuario.setCorreo(normalized);

    String suffix = UUID.randomUUID().toString().replace("-", "");
    usuario.setTelefono("tmp" + suffix.substring(0, 9));
    usuario.setDni("tmp" + suffix.substring(9, 18));
    usuario.setPasswordHash(hashSha256(password));
    usuarios.save(usuario);

    var token = UUID.randomUUID().toString();
    sessions.put(token, normalized);
    return new AuthResponse(token, normalized);
  }

  public AuthResponse login(String email, String password) {
    var normalized = normalize(email);
    var usuario = usuarios.findByCorreoIgnoreCase(normalized)
        .orElseThrow(() -> new IllegalArgumentException("INVALID_CREDENTIALS"));

    String incomingHash = hashSha256(password);
    if (usuario.getPasswordHash() == null || !incomingHash.equalsIgnoreCase(usuario.getPasswordHash())) {
      throw new IllegalArgumentException("INVALID_CREDENTIALS");
    }

    var token = UUID.randomUUID().toString();
    sessions.put(token, normalized);
    return new AuthResponse(token, normalized);
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
