package com.ufvshares.backend.reporteusuario;

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
@RequestMapping("/api/reportes-usuario")
public class ReporteUsuarioController {

  private final ReporteUsuarioService service;

  public ReporteUsuarioController(ReporteUsuarioService service) {
    this.service = service;
  }

  @GetMapping
  public List<ReporteUsuario> getAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public ReporteUsuario getById(@PathVariable Long id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ReporteUsuario create(@Valid @RequestBody ReporteUsuario reporteUsuario) {
    return service.create(reporteUsuario);
  }

  @PutMapping("/{id}")
  public ReporteUsuario update(@PathVariable Long id, @Valid @RequestBody ReporteUsuario reporteUsuario) {
    return service.update(id, reporteUsuario);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}
