package com.ufvshares.backend.usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByCorreoIgnoreCase(String correo);
	Optional<Usuario> findByTelefono(String telefono);
	Optional<Usuario> findByDniIgnoreCase(String dni);
}
