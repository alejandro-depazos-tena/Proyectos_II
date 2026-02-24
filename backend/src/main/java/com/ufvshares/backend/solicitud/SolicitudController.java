package com.ufvshares.backend.solicitud;

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
@RequestMapping("/api/solicitudes")
public class SolicitudController {

  private final SolicitudService service;

  public SolicitudController(SolicitudService service) {
    this.service = service;
  }

  @GetMapping
  public List<Solicitud> getAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Solicitud getById(@PathVariable Long id) {
    return service.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Solicitud create(@Valid @RequestBody Solicitud solicitud) {
    return service.create(solicitud);
  }

  @PutMapping("/{id}")
  public Solicitud update(@PathVariable Long id, @Valid @RequestBody Solicitud solicitud) {
    return service.update(id, solicitud);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}
