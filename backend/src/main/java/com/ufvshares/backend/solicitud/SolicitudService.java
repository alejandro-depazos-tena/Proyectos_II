package com.ufvshares.backend.solicitud;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ufvshares.backend.common.NotFoundException;

@Service
public class SolicitudService {

  private final SolicitudRepository repository;

  public SolicitudService(SolicitudRepository repository) {
    this.repository = repository;
  }

  public List<Solicitud> findAll() {
    return repository.findAll();
  }

  public Solicitud findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("SOLICITUD_NOT_FOUND"));
  }

  public Solicitud create(Solicitud solicitud) {
    return repository.save(solicitud);
  }

  public Solicitud update(Long id, Solicitud data) {
    Solicitud existing = findById(id);
    existing.setIdProducto(data.getIdProducto());
    existing.setIdSolicitante(data.getIdSolicitante());
    existing.setTipoTransaccion(data.getTipoTransaccion());
    existing.setFechaSolicitud(data.getFechaSolicitud());
    existing.setFechaInicio(data.getFechaInicio());
    existing.setFechaFin(data.getFechaFin());
    existing.setEstadoSolicitud(data.getEstadoSolicitud());
    return repository.save(existing);
  }

  public void delete(Long id) {
    Solicitud existing = findById(id);
    repository.delete(existing);
  }
}
