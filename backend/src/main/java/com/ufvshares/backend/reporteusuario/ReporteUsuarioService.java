package com.ufvshares.backend.reporteUsuario;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ufvshares.backend.common.NotFoundException;

@Service
public class ReporteUsuarioService {

  private final ReporteUsuarioRepository repository;

  public ReporteUsuarioService(ReporteUsuarioRepository repository) {
    this.repository = repository;
  }

  public List<ReporteUsuario> findAll() {
    return repository.findAll();
  }

  public ReporteUsuario findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("REPORTE_USUARIO_NOT_FOUND"));
  }

  public ReporteUsuario create(ReporteUsuario reporteUsuario) {
    return repository.save(reporteUsuario);
  }

  public ReporteUsuario update(Long id, ReporteUsuario data) {
    ReporteUsuario existing = findById(id);
    existing.setIdUsuarioReportante(data.getIdUsuarioReportante());
    existing.setIdUsuarioReportado(data.getIdUsuarioReportado());
    existing.setMotivo(data.getMotivo());
    existing.setComentario(data.getComentario());
    existing.setFechaReporte(data.getFechaReporte());
    existing.setEstadoReporte(data.getEstadoReporte());
    return repository.save(existing);
  }

  public void delete(Long id) {
    ReporteUsuario existing = findById(id);
    repository.delete(existing);
  }
}
