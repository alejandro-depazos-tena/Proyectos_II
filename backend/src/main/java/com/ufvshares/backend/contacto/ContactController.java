package com.ufvshares.backend.contacto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ufvshares.backend.cambioperfil.EmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

  private final EmailService emailService;

  public ContactController(EmailService emailService) {
    this.emailService = emailService;
  }

  @PostMapping
  public ResponseEntity<?> sendContactMessage(@Valid @RequestBody ContactRequest request) {
    try {
      emailService.enviarConsultaContacto(
          request.nombre(),
          request.apellido(),
          request.correo(),
          request.motivo(),
          request.mensaje());
      return ResponseEntity.ok(new OkResponse(true));
    } catch (Exception ex) {
      System.out.println("[MAIL ERROR CONTACT] correo=" + request.correo() + " causa=" + ex.getClass().getName() + " mensaje=" + ex.getMessage());
      if (ex.getCause() != null) {
        System.out.println("[MAIL ERROR CONTACT CAUSE] " + ex.getCause().getClass().getName() + ": " + ex.getCause().getMessage());
      }
      return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new ErrorResponse("MAIL_SEND_FAILED"));
    }
  }

  public record OkResponse(boolean ok) {}
  public record ErrorResponse(String error) {}
}