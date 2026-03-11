package com.ufvshares.backend.transaccion;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/transacciones")
public class TransaccionController {

  private final TransaccionService service;

  public TransaccionController(TransaccionService service) {
    this.service = service;
  }

  @GetMapping
  public List<Transaccion> getAll() {
    return service.findAll();
  }

  @GetMapping("/{id}")
  public Transaccion getById(@PathVariable Long id) {
    return service.findById(id);
  }

  /** Devuelve la transacción activa (EN_CURSO) de un producto con sus fechas. */
  @GetMapping("/producto/{idProducto}")
  public ResponseEntity<Map<String, Object>> getActivaByProducto(@PathVariable Long idProducto) {
    Map<String, Object> detalle = service.getDetalleActivo(idProducto);
    if (detalle == null) return ResponseEntity.noContent().build();
    return ResponseEntity.ok(detalle);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Transaccion create(@Valid @RequestBody Transaccion transaccion) {
    return service.create(transaccion);
  }

  @PutMapping("/{id}")
  public Transaccion update(@PathVariable Long id, @Valid @RequestBody Transaccion transaccion) {
    return service.update(id, transaccion);
  }

  /** Marca la transacción como COMPLETADA y libera el producto si era alquiler/préstamo. */
  @PutMapping("/{id}/completar")
  public Transaccion completar(@PathVariable Long id) {
    return service.completar(id);
  }

  /** Cancela la transacción y devuelve el producto a DISPONIBLE. */
  @PutMapping("/{id}/cancelar")
  public Transaccion cancelar(@PathVariable Long id) {
    return service.cancelar(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    service.delete(id);
  }
}

