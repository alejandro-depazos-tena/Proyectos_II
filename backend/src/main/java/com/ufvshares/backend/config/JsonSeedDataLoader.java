package com.ufvshares.backend.config;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufvshares.backend.auth.SessionJpaRepository;
import com.ufvshares.backend.producto.CategoriaProducto;
import com.ufvshares.backend.producto.CondicionProducto;
import com.ufvshares.backend.producto.EstadoProducto;
import com.ufvshares.backend.producto.Producto;
import com.ufvshares.backend.producto.ProductoRepository;
import com.ufvshares.backend.producto.TipoTransaccion;
import com.ufvshares.backend.usuario.Usuario;
import com.ufvshares.backend.usuario.UsuarioRepository;

@Component
public class JsonSeedDataLoader implements CommandLineRunner {

  private static final Logger log = LoggerFactory.getLogger(JsonSeedDataLoader.class);

  private final ObjectMapper objectMapper;
  private final ResourceLoader resourceLoader;
  private final UsuarioRepository usuarioRepository;
  private final ProductoRepository productoRepository;
  private final SessionJpaRepository sessionRepository;

  @Value("${app.seed.json.enabled:true}")
  private boolean seedEnabled;

  @Value("${app.seed.json.location:classpath:dev-h2-data.json}")
  private String seedLocation;

  public JsonSeedDataLoader(
      ObjectMapper objectMapper,
      ResourceLoader resourceLoader,
      UsuarioRepository usuarioRepository,
      ProductoRepository productoRepository,
      SessionJpaRepository sessionRepository) {
    this.objectMapper = objectMapper;
    this.resourceLoader = resourceLoader;
    this.usuarioRepository = usuarioRepository;
    this.productoRepository = productoRepository;
    this.sessionRepository = sessionRepository;
  }

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    // Siempre limpiar sesiones al arrancar: el reinicio del backend invalida los tokens.
    long sessionsBorradas = sessionRepository.count();
    sessionRepository.deleteAll();
    if (sessionsBorradas > 0) {
      log.info("Sesiones anteriores eliminadas al arrancar: {} tokens invalidados.", sessionsBorradas);
    }

    if (!seedEnabled) {
      return;
    }

    if (usuarioRepository.count() > 0 || productoRepository.count() > 0) {
      log.info("Seed JSON omitido: ya existen datos en la base de datos.");
      return;
    }

    Resource resource = resourceLoader.getResource(seedLocation);
    if (!resource.exists()) {
      log.warn("Seed JSON no encontrado en {}", seedLocation);
      return;
    }

    JsonNode root;
    try (InputStream inputStream = resource.getInputStream()) {
      root = objectMapper.readTree(inputStream);
    }

    List<Usuario> usuarios = parseUsuarios(root.path("usuario"));
    if (!usuarios.isEmpty()) {
      usuarioRepository.saveAll(usuarios);
    }

    List<Producto> productos = parseProductos(root.path("producto"));
    if (!productos.isEmpty()) {
      productoRepository.saveAll(productos);
    }

    log.info("Seed JSON aplicado: {} usuarios y {} productos.", usuarios.size(), productos.size());
  }

  private List<Usuario> parseUsuarios(JsonNode usuariosNode) {
    List<Usuario> usuarios = new ArrayList<>();
    if (!usuariosNode.isArray()) {
      return usuarios;
    }

    for (JsonNode node : usuariosNode) {
      Usuario usuario = new Usuario();
      usuario.setIdUsuario(node.path("id_usuario").asLong());
      usuario.setNombre(node.path("nombre").asText());
      usuario.setApellidos(node.path("apellidos").asText());
      usuario.setCorreo(node.path("correo").asText());
      usuario.setTelefono(node.path("telefono").asText());
      usuario.setDni(node.path("dni").asText());
      usuario.setPasswordHash(node.path("password").asText());
      usuario.setEsAdmin(node.path("es_admin").asBoolean(false));
      usuarios.add(usuario);
    }

    return usuarios;
  }

  private List<Producto> parseProductos(JsonNode productosNode) {
    List<Producto> productos = new ArrayList<>();
    if (!productosNode.isArray()) {
      return productos;
    }

    for (JsonNode node : productosNode) {
      Producto producto = new Producto();
      producto.setIdProducto(node.path("id_producto").asLong());
      producto.setTitulo(node.path("titulo").asText());
      producto.setDescripcion(node.path("descripcion").asText());
      producto.setCategoria(CategoriaProducto.valueOf(node.path("categoria").asText()));
      producto.setTipoTransaccion(TipoTransaccion.valueOf(node.path("tipo_transaccion").asText()));
      producto.setEstadoProducto(EstadoProducto.valueOf(node.path("estado_producto").asText()));
      producto.setIdPropietario(node.path("id_propietario").asLong());

      if (!node.path("precio").isNull()) {
        producto.setPrecio(BigDecimal.valueOf(node.path("precio").asDouble()));
      }

      if (!node.path("condicion").isMissingNode() && !node.path("condicion").isNull()) {
        producto.setCondicion(CondicionProducto.valueOf(node.path("condicion").asText()));
      }

      if (!node.path("ubicacion").isMissingNode() && !node.path("ubicacion").isNull()) {
        producto.setUbicacion(node.path("ubicacion").asText());
      }

      if (!node.path("imagen_url").isMissingNode() && !node.path("imagen_url").isNull()) {
        producto.setImagenUrl(node.path("imagen_url").asText());
      }

      if (!node.path("vistas").isMissingNode()) {
        producto.setVistas(node.path("vistas").asInt(0));
      }

      productos.add(producto);
    }

    return productos;
  }
}
