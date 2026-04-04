package com.ufvshares.backend.contrato;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratoRepository extends JpaRepository<Contrato, Long> {
  Optional<Contrato> findByIdTransaccion(Long idTransaccion);

  List<Contrato> findByIdPropietarioOrIdArrendatarioOrderByFechaCreacionDesc(Long idPropietario, Long idArrendatario);
}
