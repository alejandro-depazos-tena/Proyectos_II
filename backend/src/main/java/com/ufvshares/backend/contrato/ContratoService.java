package com.ufvshares.backend.contrato;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ufvshares.backend.producto.Producto;
import com.ufvshares.backend.solicitud.Solicitud;
import com.ufvshares.backend.transaccion.Transaccion;
import com.ufvshares.backend.usuario.Usuario;

@Service
public class ContratoService {

  private final ContratoRepository repository;

  public ContratoService(ContratoRepository repository) {
    this.repository = repository;
  }

  public Optional<Contrato> findByTransaccion(Long idTransaccion) {
    return repository.findByIdTransaccion(idTransaccion);
  }

  public Contrato crearDesdeSolicitud(
      Solicitud solicitud,
      Producto producto,
      Transaccion transaccion,
      Usuario arrendador,
      Usuario arrendatario) {
    Contrato contrato = new Contrato();
    contrato.setIdTransaccion(transaccion.getIdTransaccion());
    contrato.setIdProducto(producto.getIdProducto());
    contrato.setIdPropietario(producto.getIdPropietario());
    contrato.setIdArrendatario(solicitud.getIdSolicitante());
    contrato.setFechaCreacion(LocalDateTime.now());
    contrato.setFechaInicio(transaccion.getFechaInicioReal());
    contrato.setFechaFin(transaccion.getFechaFinReal());
    contrato.setEstadoContrato(EstadoContrato.PENDIENTE_FIRMA);
    contrato.setAceptaTerminos(false);
    contrato.setTextoCondiciones(generarCondicionesPorDefecto(producto, arrendador, arrendatario, contrato));
    return repository.save(contrato);
  }

  public Contrato firmarPorArrendatario(Contrato contrato, String ip, String userAgent) {
    contrato.setEstadoContrato(EstadoContrato.ACTIVO);
    contrato.setFechaFirmaArrendatario(LocalDateTime.now());
    contrato.setAceptaTerminos(true);
    return repository.save(contrato);
  }

  public void finalizarSiExiste(Long idTransaccion) {
    repository.findByIdTransaccion(idTransaccion).ifPresent(c -> {
      c.setEstadoContrato(EstadoContrato.FINALIZADO);
      repository.save(c);
    });
  }

  public void cancelarSiExiste(Long idTransaccion) {
    repository.findByIdTransaccion(idTransaccion).ifPresent(c -> {
      c.setEstadoContrato(EstadoContrato.CANCELADO);
      repository.save(c);
    });
  }

  private String generarCondicionesPorDefecto(
      Producto producto,
      Usuario arrendador,
      Usuario arrendatario,
      Contrato contrato) {
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String fechaInicio = contrato.getFechaInicio() != null ? contrato.getFechaInicio().format(fmt) : "la fecha de entrega efectiva";
    String fechaFin = contrato.getFechaFin() != null ? contrato.getFechaFin().format(fmt) : "la fecha de devolucion acordada";

    String arrendadorTexto = "D./Dña. " + arrendador.getNombre() + " " + arrendador.getApellidos();
    String arrendatarioTexto = "D./Dña. " + arrendatario.getNombre() + " " + arrendatario.getApellidos();
    String descripcionObjeto = "Objeto: " + producto.getTitulo() + ". Categoria: " + producto.getCategoria() +
        ". Condicion declarada: " + (producto.getCondicion() != null ? producto.getCondicion() : "SIN_DEFINIR") +
        ". Descripcion: " + producto.getDescripcion();
    return "Contrato digital de cesion temporal del objeto '" + producto.getTitulo() + "'. " +
        arrendadorTexto + " (arrendador) cede temporalmente el bien a " +
        arrendatarioTexto + " (arrendatario). " +
        "Identificacion de partes: arrendador DNI " + arrendador.getDni() + ", arrendatario DNI " + arrendatario.getDni() + ". " +
        descripcionObjeto + " " +
        "El arrendatario declara recibir el objeto en buen estado en el momento de la entrega, " +
        "sin perjuicio de poder documentar fotográficamente dicha entrega en la aplicación. " +
        "El arrendatario se hace responsable del objeto desde " + fechaInicio + " y hasta " + fechaFin + ". " +
        "Responsabilidad civil: el arrendatario responde de cualquier daño material, pérdida o robo del objeto durante el periodo pactado. " +
        "En caso de daño material o perdida del objeto, el arrendatario abonará al arrendador el valor actual del objeto en el momento del siniestro, " +
        "atendiendo a su estado real y depreciacion razonable. " +
        "Durante ese periodo, se obliga a custodiarlo, usarlo de forma diligente y devolverlo en el estado pactado, " +
        "salvo el desgaste normal por uso adecuado. Cualquier daño por uso indebido, negligencia o perdida " +
        "sera responsabilidad del arrendatario.";
  }

}
