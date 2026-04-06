package com.ufvshares.backend.reporteproducto;

import java.util.List;
import java.util.Comparator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ufvshares.backend.common.NotFoundException;

@Service
public class ReporteProductoService {

  private final ReporteProductoRepository repository;

  public ReporteProductoService(ReporteProductoRepository repository) {
    this.repository = repository;
  }

  public List<ReporteProducto> findAll() {
    return repository.findAll().stream()
        .sorted(Comparator.comparing(ReporteProducto::getFechaReporte).reversed())
        .toList();
  }

  public ReporteProducto findById(Long id) {
    return repository.findById(id).orElseThrow(() -> new NotFoundException("REPORTE_PRODUCTO_NOT_FOUND"));
  }

  public ReporteProducto create(ReporteProducto reporteProducto) {
    return repository.save(reporteProducto);
  }

  @Transactional
  public ReporteProducto close(Long id) {
    ReporteProducto existing = findById(id);
    existing.setEstadoReporte(com.ufvshares.backend.reporte.EstadoReporte.CERRADO);
    return repository.save(existing);
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
