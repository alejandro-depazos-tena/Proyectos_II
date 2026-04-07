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

  @Value("${app.mail.from:${MAIL_FROM:${MAIL_USERNAME:soporteufvshares@gmail.com}}}")
  private String from;

  @Value("${app.frontend.url:http://localhost:4321}")
  private String frontendUrl;

  @Value("${app.api.url:http://localhost:8080/api}")
  private String apiUrl;

  @Value("${app.mail.support-to:${MAIL_SUPPORT_TO:${MAIL_USERNAME:soporteufvshares@gmail.com}}}")
  private String supportTo;

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
    MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
    helper.setFrom(from);
    helper.setTo(destinatario);
    helper.setSubject("Confirma el cambio de tu " + campoLabel + " · UFV Shares");
    helper.setText(html, true);
    mailSender.send(msg);
  }

  public void enviarResetPassword(String destinatario, String nombreCompleto, String token) throws MessagingException {
    String resetUrl = frontendUrl + "/forgot-password?token=" + token;
    String to = destinatario == null ? "" : destinatario.trim().toLowerCase();

    String plainText = """
      Hola, %s

      Hemos recibido una solicitud para restablecer tu contraseña.

      Abre este enlace para crear una nueva contraseña:
      %s

      El enlace expira en 30 minutos.

      Si no has solicitado este cambio, ignora este correo.
      """.formatted(nombreCompleto, resetUrl);

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
                <tr>
                  <td style="background:#0f5a86;padding:28px 32px;">
                    <p style="margin:0;color:#fff;font-size:20px;font-weight:700;">
                      UFV Shares
                    </p>
                  </td>
                </tr>
                <tr>
                  <td style="padding:32px;">
                    <p style="margin:0 0 12px;font-size:16px;color:#111;">
                      Hola, <strong>%s</strong>
                    </p>
                    <p style="margin:0 0 24px;font-size:15px;color:#444;line-height:1.6;">
                      Hemos recibido una solicitud para restablecer tu contraseña.
                    </p>
                    <p style="margin:0 0 28px;font-size:14px;color:#666;">
                      El enlace expira en <strong>30 minutos</strong>.
                    </p>
                    <a href="%s"
                      style="display:inline-block;background:#0f5a86;color:#fff;
                             text-decoration:none;font-weight:700;font-size:15px;
                             padding:14px 32px;border-radius:50px;">
                      Restablecer contraseña
                    </a>
                    <p style="margin:28px 0 0;font-size:12px;color:#999;">
                      Si no has solicitado este cambio, ignora este correo.
                    </p>
                  </td>
                </tr>
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
        """.formatted(nombreCompleto, resetUrl);

    MimeMessage msg = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
    helper.setFrom(from);
    helper.setReplyTo(from);
    helper.setTo(to);
    helper.setSubject("Restablece tu contraseña · UFV Shares");
    helper.setText(plainText, html);
    System.out.println("[MAIL] Password reset email sent to " + to + " from " + from);
    mailSender.send(msg);
  }

  public void enviarConsultaContacto(String nombre, String apellidos, String correo, String motivo, String mensaje)
      throws MessagingException {
    String fromUserName = ((nombre == null ? "" : nombre.trim()) + " " + (apellidos == null ? "" : apellidos.trim())).trim();
    String fromUserEmail = correo == null ? "" : correo.trim().toLowerCase();
    String subjectReason = motivo == null ? "Consulta" : motivo.trim();
    String messageBody = mensaje == null ? "" : mensaje.trim();

    String plainText = """
      Nueva consulta desde el formulario de contacto de UFV Shares

      Nombre: %s
      Correo: %s
      Motivo: %s

      Mensaje:
      %s
      """.formatted(fromUserName, fromUserEmail, subjectReason, messageBody);

    String html = """
        <!DOCTYPE html>
        <html lang="es">
        <head><meta charset="UTF-8" /></head>
        <body style="margin:0;padding:0;background:#f5f5f5;font-family:Arial,sans-serif;">
          <table width="100%%" cellpadding="0" cellspacing="0">
            <tr><td align="center" style="padding:32px 16px;">
              <table width="620" cellpadding="0" cellspacing="0"
                style="background:#ffffff;border-radius:14px;overflow:hidden;
                       box-shadow:0 2px 8px rgba(0,0,0,.08);">
                <tr>
                  <td style="background:#0f5a86;padding:24px 28px;">
                    <p style="margin:0;color:#fff;font-size:19px;font-weight:700;">UFV Shares · Contacto</p>
                  </td>
                </tr>
                <tr>
                  <td style="padding:26px 28px;">
                    <p style="margin:0 0 16px;font-size:15px;color:#111;">
                      Se ha recibido una nueva consulta desde la web.
                    </p>
                    <table width="100%%" cellpadding="0" cellspacing="0" style="margin:0 0 18px;">
                      <tr>
                        <td style="padding:6px 0;font-size:14px;color:#444;"><strong>Nombre:</strong> %s</td>
                      </tr>
                      <tr>
                        <td style="padding:6px 0;font-size:14px;color:#444;"><strong>Correo:</strong> %s</td>
                      </tr>
                      <tr>
                        <td style="padding:6px 0;font-size:14px;color:#444;"><strong>Motivo:</strong> %s</td>
                      </tr>
                    </table>
                    <div style="background:#f8fafc;border:1px solid #e5e7eb;border-radius:10px;padding:14px 16px;">
                      <p style="margin:0 0 8px;font-size:13px;color:#6b7280;font-weight:700;">Mensaje</p>
                      <p style="margin:0;font-size:14px;line-height:1.6;color:#111;white-space:pre-line;">%s</p>
                    </div>
                  </td>
                </tr>
              </table>
            </td></tr>
          </table>
        </body>
        </html>
        """.formatted(fromUserName, fromUserEmail, subjectReason, messageBody);

    MimeMessage msg = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
    helper.setFrom(from);
    helper.setReplyTo(fromUserEmail);
    helper.setTo(supportTo);
    helper.setSubject("[CONTACTO] " + subjectReason + " · " + fromUserName);
    helper.setText(plainText, html);
    mailSender.send(msg);
  }

  public void enviarNotificacionNuevaSolicitud(String destinatario, String nombrePropietario,
      String nombreSolicitante, String correoSolicitante, String tituloProducto, Long idProducto)
      throws MessagingException {

    String productUrl = frontendUrl + "/product/view?id=" + idProducto;

    String plainText = """
      Hola, %s

      %s (%s) te ha solicitado el producto "%s".

      Puedes revisar el producto aquí:
      %s

      Equipo UFV Shares
      """.formatted(nombrePropietario, nombreSolicitante, correoSolicitante, tituloProducto, productUrl);

    String html = """
        <!DOCTYPE html>
        <html lang="es">
        <head><meta charset="UTF-8" /></head>
        <body style="margin:0;padding:0;background:#f5f5f5;font-family:Arial,sans-serif;">
          <table width="100%%" cellpadding="0" cellspacing="0">
            <tr><td align="center" style="padding:32px 16px;">
              <table width="620" cellpadding="0" cellspacing="0"
                style="background:#ffffff;border-radius:14px;overflow:hidden;
                       box-shadow:0 2px 8px rgba(0,0,0,.08);">
                <tr>
                  <td style="background:#0f5a86;padding:24px 28px;">
                    <p style="margin:0;color:#fff;font-size:19px;font-weight:700;">UFV Shares</p>
                  </td>
                </tr>
                <tr>
                  <td style="padding:26px 28px;">
                    <p style="margin:0 0 12px;font-size:16px;color:#111;">
                      Hola, <strong>%s</strong>
                    </p>
                    <p style="margin:0 0 18px;font-size:15px;color:#444;line-height:1.6;">
                      <strong>%s</strong> (%s) te ha solicitado el producto:
                    </p>
                    <div style="background:#f8fafc;border:1px solid #e5e7eb;border-radius:10px;padding:14px 16px;">
                      <p style="margin:0;font-size:15px;color:#111;"><strong>%s</strong></p>
                    </div>
                    <p style="margin:18px 0 22px;font-size:14px;color:#666;">
                      Revisa los detalles del producto en UFV Shares.
                    </p>
                    <a href="%s"
                      style="display:inline-block;background:#0f5a86;color:#fff;
                             text-decoration:none;font-weight:700;font-size:15px;
                             padding:12px 24px;border-radius:40px;">
                      Ver producto
                    </a>
                  </td>
                </tr>
              </table>
            </td></tr>
          </table>
        </body>
        </html>
        """.formatted(nombrePropietario, nombreSolicitante, correoSolicitante, tituloProducto, productUrl);

    MimeMessage msg = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
    helper.setFrom(from);
    helper.setReplyTo(from);
    helper.setTo(destinatario);
    helper.setSubject("Nueva solicitud de producto · UFV Shares");
    helper.setText(plainText, html);
    mailSender.send(msg);
  }
}
