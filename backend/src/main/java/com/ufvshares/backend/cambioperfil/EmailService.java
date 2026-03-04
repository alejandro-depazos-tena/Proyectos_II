package com.ufvshares.backend.cambioperfil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

  private final JavaMailSender mailSender;

  @Value("${app.mail.from:noreply@ufvshares.com}")
  private String from;

  @Value("${app.frontend.url:http://localhost:4321}")
  private String frontendUrl;

  @Value("${app.api.url:http://localhost:8080/api}")
  private String apiUrl;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void enviarConfirmacionCambio(String destinatario, String nombreCompleto,
      String campoLabel, String valorNuevo, String token) throws MessagingException {

    String confirmUrl = apiUrl + "/me/confirmar-cambio?token=" + token;

    String html = """
        <!DOCTYPE html>
        <html lang="es">
        <head><meta charset="UTF-8" /></head>
        <body style="margin:0;padding:0;background:#f5f5f5;font-family:Arial,sans-serif;">
          <table width="100%%" cellpadding="0" cellspacing="0">
            <tr><td align="center" style="padding:40px 16px;">
              <table width="520" cellpadding="0" cellspacing="0"
                style="background:#ffffff;border-radius:16px;overflow:hidden;
                       box-shadow:0 2px 8px rgba(0,0,0,.08);">
                <!-- Header -->
                <tr>
                  <td style="background:#6d28d9;padding:28px 32px;">
                    <p style="margin:0;color:#fff;font-size:20px;font-weight:700;">
                      UFV Shares
                    </p>
                  </td>
                </tr>
                <!-- Body -->
                <tr>
                  <td style="padding:32px;">
                    <p style="margin:0 0 12px;font-size:16px;color:#111;">
                      Hola, <strong>%s</strong>
                    </p>
                    <p style="margin:0 0 24px;font-size:15px;color:#444;line-height:1.6;">
                      Has solicitado cambiar tu <strong>%s</strong> a:
                    </p>
                    <div style="background:#f3f0ff;border-left:4px solid #6d28d9;
                                border-radius:8px;padding:14px 20px;margin:0 0 24px;">
                      <p style="margin:0;font-size:17px;font-weight:700;color:#6d28d9;">
                        %s
                      </p>
                    </div>
                    <p style="margin:0 0 28px;font-size:14px;color:#666;">
                      Pulsa el botón para confirmar este cambio.
                      El enlace expira en <strong>30 minutos</strong>.
                    </p>
                    <a href="%s"
                      style="display:inline-block;background:#6d28d9;color:#fff;
                             text-decoration:none;font-weight:700;font-size:15px;
                             padding:14px 32px;border-radius:50px;">
                      Confirmar cambio
                    </a>
                    <p style="margin:28px 0 0;font-size:12px;color:#999;">
                      Si no has solicitado este cambio, ignora este correo y tu cuenta
                      permanecerá sin cambios.
                    </p>
                  </td>
                </tr>
                <!-- Footer -->
                <tr>
                  <td style="background:#f9f9f9;padding:16px 32px;border-top:1px solid #eee;">
                    <p style="margin:0;font-size:11px;color:#bbb;text-align:center;">
                      UFV Shares · Universidad Francisco de Vitoria
                    </p>
                  </td>
                </tr>
              </table>
            </td></tr>
          </table>
        </body>
        </html>
        """.formatted(nombreCompleto, campoLabel, valorNuevo, confirmUrl);

    MimeMessage msg = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(msg, "UTF-8");
    helper.setFrom(from);
    helper.setTo(destinatario);
    helper.setSubject("Confirma el cambio de tu " + campoLabel + " · UFV Shares");
    helper.setText(html, true);
    mailSender.send(msg);
  }
}
