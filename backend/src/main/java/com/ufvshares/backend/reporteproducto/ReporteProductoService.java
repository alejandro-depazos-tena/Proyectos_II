package com.ufvshares.backend.reporteProducto;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ufvshares.backend.common.NotFoundException;

@Service
public class ReporteProductoService {

  private final ReporteProductoRepository repository;

  public ReporteProductoService(ReporteProductoRepository repository) {
    this.repository = repository;
  }

  public List<ReporteProducto> findAll() {
    return repository.findAll();
  }

  public ReporteProducto findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("REPORTE_PRODUCTO_NOT_FOUND"));
  }

  public ReporteProducto create(ReporteProducto reporteProducto) {
    return repository.save(reporteProducto);
  }

  public ReporteProducto update(Long id, ReporteProducto data) {
    ReporteProducto existing = findById(id);
    existing.setIdUsuarioReportante(data.getIdUsuarioReportante());
    existing.setIdProductoReportado(data.getIdProductoReportado());
    existing.setMotivo(data.getMotivo());
    existing.setComentario(data.getComentario());
    existing.setFechaReporte(data.getFechaReporte());
    existing.setEstadoReporte(data.getEstadoReporte());
    return repository.save(existing);
  }

  public void delete(Long id) {
    ReporteProducto existing = findById(id);
    repository.delete(existing);
  }
}
