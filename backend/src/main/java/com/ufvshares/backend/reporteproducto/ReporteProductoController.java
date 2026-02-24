package com.ufvshares.backend.reporteproducto;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reportes-producto")
public class ReporteProductoController {

  private final ReporteProductoService service;

  public ReporteProductoController(ReporteProductoService service) {
    this.service = service;
  }

  @GetMapping
  public List<ReporteProducto> getAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ReporteProducto getById(@PathVariable Long id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ReporteProducto create(@Valid @RequestBody ReporteProducto reporteProducto) {
    return service.create(reporteProducto);
  }

  @PutMapping("/{id}")
  public ReporteProducto update(@PathVariable Long id, @Valid @RequestBody ReporteProducto reporteProducto) {
    return service.update(id, reporteProducto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}
