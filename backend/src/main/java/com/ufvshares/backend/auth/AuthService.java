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
    usuario.setPasswordHash(hashSha256(req.password()));
    usuarios.save(usuario);

    var token = UUID.randomUUID().toString();
    sessions.put(token, normalized);
    return new AuthResponse(token, normalized, req.nombre().trim());
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
    return new AuthResponse(token, normalized, usuario.getNombre());
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
