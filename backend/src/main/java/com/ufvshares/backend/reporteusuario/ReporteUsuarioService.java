package com.ufvshares.backend.reporteusuario;

import java.util.List;
import java.util.Comparator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ufvshares.backend.common.NotFoundException;

@Service
public class ReporteUsuarioService {

  private final ReporteUsuarioRepository repository;

  public ReporteUsuarioService(ReporteUsuarioRepository repository) {
    this.repository = repository;
  }

  public List<ReporteUsuario> findAll() {
    return repository.findAll().stream()
        .sorted(Comparator.comparing(ReporteUsuario::getFechaReporte).reversed())
        .toList();
  }

  public ReporteUsuario findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("REPORTE_USUARIO_NOT_FOUND"));
  }

  public ReporteUsuario create(ReporteUsuario reporteUsuario) {
    return repository.save(reporteUsuario);
  }

  @Transactional
  public ReporteUsuario close(Long id) {
    ReporteUsuario existing = findById(id);
    existing.setEstadoReporte(com.ufvshares.backend.reporte.EstadoReporte.CERRADO);
    return repository.save(existing);
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
