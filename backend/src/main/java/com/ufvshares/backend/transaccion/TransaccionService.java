package com.ufvshares.backend.transaccion;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ufvshares.backend.common.NotFoundException;

@Service
public class TransaccionService {

  private final TransaccionRepository repository;

  public TransaccionService(TransaccionRepository repository) {
    this.repository = repository;
  }

  public List<Transaccion> findAll() {
    return repository.findAll();
  }

  public Transaccion findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("TRANSACCION_NOT_FOUND"));
  }

  public Transaccion create(Transaccion transaccion) {
    return repository.save(transaccion);
  }

  public Transaccion update(Long id, Transaccion data) {
    Transaccion existing = findById(id);
    existing.setIdSolicitud(data.getIdSolicitud());
    existing.setFechaCreacion(data.getFechaCreacion());
    existing.setFechaInicioReal(data.getFechaInicioReal());
    existing.setFechaFinReal(data.getFechaFinReal());
    existing.setEstadoTransaccion(data.getEstadoTransaccion());
    return repository.save(existing);
  }

  public void delete(Long id) {
    Transaccion existing = findById(id);
    repository.delete(existing);
  }
}
