# 🔌 Implementación de APIs Externas - UFV Shares

## Resumen de APIs Integradas (100% Gratuitas)

UFV Shares integra **6 APIs externas gratuitas** para proporcionar funcionalidades avanzadas sin coste:

| API | Proveedor | Funcionalidad | Coste | Límites |
|-----|-----------|---------------|-------|---------|
| **OpenStreetMap + Leaflet.js** | OSM Foundation | Mapas interactivos, geolocalización | €0 | Ilimitado |
| **Nominatim API** | OpenStreetMap | Geocoding, búsqueda de ubicaciones | €0 | 1 req/seg (suficiente) |
| **Keepa Widget** | Keepa.com | Gráficas de precios embebidas | €0 | Ilimitado (embed) |
| **OpenWeather API** | OpenWeather | Clima del campus (planificar quedadas) | €0 | 1,000 llamadas/día |
| **Telegram Bot API** | Telegram | Notificaciones push ilimitadas | €0 | Ilimitado |
| **QR Code API** | goqr.me | Generación de QR para compartir | €0 | Ilimitado |

**Coste total mensual**: €0 (100% gratuito)

---

## 1️⃣ OpenStreetMap + Leaflet.js - Sistema de Mapas (100% Gratuito)

### 🎯 Casos de Uso

✅ **Visualización de quedadas** en mapa interactivo  
✅ **Selección visual** de punto de encuentro al crear quedada  
✅ **Marcadores personalizados** por tipo de ubicación (aula, biblioteca, cafetería)  
✅ **Detección de quedadas cercanas** por geolocalización del navegador  
✅ **Rutas a pie** entre puntos del campus  

### 📦 Configuración (Solo Frontend, sin backend necesario)

```xml
<!-- No requiere dependencias Maven, solo frontend -->
<!-- Leaflet.js se incluye vía CDN -->

### 🛠️ Servicio de Geolocalización (Backend)

```java
@Service
@Slf4j
public class GeolocationService {
    
    @Autowired
    private GeoApiContext context;
    
    @Autowired
    private UbicacionRepository ubicacionRepo;
    
    /**
     * Busca una ubicación por nombre o dirección
     * @param query Término de búsqueda
     * @return DTO con coordenadas y nombre formateado
     */
    public UbicacionDTO buscarUbicacion(String query) {
        try {
            // Añadir contexto "UFV campus" para mejorar resultados
            GeocodingResult[] results = GeocodingApi
                .geocode(context, "UFV campus " + query)
                .await();
            
            if (results.length > 0) {
                LatLng location = results[0].geometry.location;
                
                return UbicacionDTO.builder()
            RestTemplate restTemplate;
    
    @Autowired
    private UbicacionRepository ubicacionRepo;
    
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org";
    
    /**
     * Busca una ubicación usando Nominatim API (OSM) - GRATUITO
     * @param query Término de búsqueda
     * @return DTO con coordenadas y nombre formateado
     */
    public UbicacionDTO buscarUbicacion(String query) {
        try {
            // Limitar búsqueda al área del campus UFV
            String url = String.format(
                "%s/search?q=%s&format=json&limit=5&bounded=1&viewbox=-3.82,-3.80,40.44,40.45",
                NOMINATIM_URL,
                URLEncoder.encode("UFV campus " + query, StandardCharsets.UTF_8)
            );
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "UFVShares/1.0 (contact@ufvshares.com)");
            
            ResponseEntity<NominatimResult[]> response = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), NominatimResult[].class
            );
            
            if (response.getBody() != null && response.getBody().length > 0) {
                NominatimResult result = response.getBody()[0];
                
                return UbicacionDTO.builder()
                    .nombre(result.getDisplayName())
                    .latitud(new BigDecimal(result.getLat()))
                    .longitud(new BigDecimal(result.getLon()))
                    .build();
            }
        } catch (Exception e) {
            log.error("Error buscando ubicación: {}", e.getMessage());
        }
        return null;
    }
    
    /**
     * Calcula ruta entre dos puntos usando algoritmo A* interno
     * (Sin API externa, cálculo local basado en distancia euclidiana)
     */
    public RutaDTO calcularRuta(Ubicacion origen, Ubicacion destino) {
        try {
            // Calcular distancia Haversine
            double distanciaMetros = calcularDistanciaHaversine(
                origen.getLatitud(), origen.getLongitud(),
                destino.getLatitud(), destino.getLongitud()
            );
            
            // Estimar tiempo (velocidad promedio peatonal: 5 km/h)
            int duracionMinutos = (int) Math.ceil((distanciaMetros / 1000.0) * 12);
            
            return RutaDTO.builder()
                .distanciaMetros((int) distanciaMetros)
                .duracionMinutos(duracionMinutos)
                .tipo("peatonal")
                .build();
            
        } catch (Exception e) {
            log.error("Error calculando ruta: {}", e.getMessage());
        }
        return null;
    }
    
    /**
     * Fórmula Haversine para calcular distancia entre coordenadas
     */
    private double calcularDistanciaHaversine(
            BigDecimal lat1, BigDecimal lon1,
            BigDecimal lat2, BigDecimal lon2) {
        
        final int RADIO_TIERRA = 6371000; // metros
        
        double dLat = Math.toRadians(lat2.subtract(lat1).doubleValue());
        double dLon = Math.toRadians(lon2.subtract(lon1).doubleValue());
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1.doubleValue())) *
                   Math.cos(Math.toRadians(lat2.doubleValue())) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return RADIO_TIERRA * cadio
            ORDER BY (6371000 * acos(
                cos(radians(:lat)) * cos(radians(q.ubicacion.latitud)) *
                cos(radians(q.ubicacion.longitud) - radians(:lng)) +
                sin(radians(:lat)) * sin(radians(q.ubicacion.latitud))
            )) ASC
            """;
        
        List<Quedada> quedadas = quedadaRepository.createQuery(query)
            .setParameter("estado", EstadoQuedada.PENDIENTE)
            .setParameter("lat", lat)
            .setParameter("lng", lng)
            .setParameter("radio", radioMetros)
            .getResultList();
         + OpenStreetMap

```html
<!-- Vista de Mapa de Quedadas -->
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h2>🗺️ Quedadas en el Campus</h2>
        <div class="btn-group">
            <button id="btnMiUbicacion" class="btn btn-outline-primary">
                📍 Mi Ubicación
            </button>
            <button id="btnClima" class="btn btn-outline-info">
                🌤️ Ver Clima
            </button>
        </div>
    </div>
    
    <div id="map" style="height: 600px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);"></div>
    
    <div id="climaInfo" class="alert alert-info mt-3" style="display: none;">
        <strong>Clima actual en UFV:</strong> <span id="climaTexto"></span>
    </div>
</div>

<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>

<script>
// Inicializar mapa centrado en UFV (100% GRATUITO, sin límites)
var map = L.map('map').setView([40.4426, -3.8120], 16);

// Capa base OpenStreetMap (GRATUITO, sin API key)
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors',
    maxZoom: 19
}).addTo(map);

// Iconos personalizados por tipo de ubicación
const iconos = {
    'AULA': '🎓',
    'BIBLIOTECA': '📚',
    'CAFETERIA': '☕',
    'ZONA_VERDE': '🌳',
    'DEPORTIVO': '⚽',
    'OTRO': '📍'
};

// Cargar quedadas activas desde el backend
fetch('/api/quedadas/activas')
    .then(response => response.json())
    .then(quedadas => {
        quedadas.forEach(quedada => {
            // Crear marcador HTML personalizado (sin imágenes)
            var iconHtml = L.divIcon({
                html: `<div style="font-size: 24px;">${iconos[quedada.ubicacion.tipo]}</div>`,
                className: 'custom-marker',
                iconSize: [30, 30],
                iconAnchor: [15, 15]
            });
            
            var marker = L.marker(
                [quedada.ubicacion.latitud, quedada.ubicacion.longitud],
                {icon: iconHtml}
            ).bindPopup(`
                <div class="popup-quedada">
                    <h5>${quedada.titulo}</h5>
                    <p><small>${quedada.descripcion}</small></p>
                    <hr>
                    <p class="mb-1">
                        <strong>📅</strong> ${new Date(quedada.fechaHora).toLocaleString('es-ES')}
                    </p>
                    <p class="mb-2">
                        <strong>👥</strong> ${quedada.asistentes}/${quedada.maxParticipantes} personas
                    </p>
                    <p class="mb-2">
                        <strong>📍</strong> ${quedada.ubicacion.nombre}
                    </p>
                    <a href="/quedadas/${quedada.id}" class="btn btn-sm btn-primary w-100">
                        Ver detalles y unirme
                    </a>
                </div>
            `).addTo(map);
        });
    });Ventajas de OpenStreetMap + Leaflet.js

| Característica | OpenStreetMap | Google Maps (comparación) |
|----------------|---------------|---------------------------|
| **Coste** | €0 | €200+/mes en producción |
| **Límites de uso** | Ilimitado | 28,000 cargas/mes gratis |
| **API Key requerida** | No | Sí |
| **Tiles del mapa** | Gratuitos | De pago |
| **Nominatim (geocoding)** | 1 req/seg (suficiente) | De pago después de cuota |
| **Libertad de uso** | Open Source | Términos restrictivos |

**Conclusión**: OpenStreetMap es **superior para este proyecto** por ser gratuito, sin límites, y sin requerir tarjeta de crédito
        }
        
        // Crear nuevo marcador arrastrable
        selectedMarker = L.marker(e.latlng, {
            draggable: true,
            icon: L.divIcon({
                html: '<div style="font-size: 30px;">📍</div>',
                className: 'custom-marker-new'
            })
        }).addTo(map);
        
        // Actualizar formulario con coordenadas
        actualizarCoordenadas(e.latlng.lat, e.latlng.lng);
        
        // Evento al arrastrar
        selectedMarker.on('dragend', function(event) {
            var position = event.target.getLatLng();
            actualizarCoordenadas(position.lat, position.lng);
        });
    }
});

function actualizarCoordenadas(lat, lng) {
    document.getElementById('latitud').value = lat.toFixed(6);
    document.getElementById('longitud').value = lng.toFixed(6);
    
    // Buscar nombre de ubicación con Nominatim (GRATUITO)
    fetch(`https://nominatim.openstreetmap.org/reverse?lat=${lat}&lon=${lng}&format=json`, {
        headers: {'User-Agent': 'UFVShares/1.0'}
    })
    .then(r => r.json())
    .then(data => {
        if (data.display_name) {
            document.getElementById('ubicacionNombre').value = data.display_name;
        }
    });
}

// Botón: Centrar en mi ubicación (geolocalización del navegador)
document.getElementById('btnMiUbicacion').addEventListener('click', function() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var lat = position.coords.latitude;
            var lng = position.coords.longitude;
            
            map.setView([lat, lng], 18);
            
            // Añadir marcador temporal "Estás aquí"
            L.marker([lat, lng], {
                icon: L.divIcon({
                    html: '<div style="font-size: 30px; animation: pulse 1s infinite;">📍</div>',
                    className: 'mi-ubicacion'
                })
            }).addTo(map).bindPopup('📍 Estás aquí').openPopup();
        }, function(error) {
            alert('No se pudo obtener tu ubicación. Activa el GPS.');
        });
    } else {
        alert('Tu navegador no soporta geolocalización.');
    }
});

// Botón: Ver clima del campus (OpenWeather API - GRATUITO)
document.getElementById('btnClima').addEventListener('click', function() {
    fetch('/api/clima/campus')
        .then(r => r.json())
        .then(data => {
            document.getElementById('climaTexto').innerHTML = `
                ${data.descripcion} - ${data.temperatura}°C 
                (Sensación térmica: ${data.sensacion}°C)
                <br>Humedad: ${data.humedad}% | Viento: ${data.viento} km/h
            `;
            document.getElementById('climaInfo').style.display = 'block';
        });
});
</script>

<style>
@keyframes pulse {
    0%, 100% { transform: scale(1); }
    50% { transform: scale(1.2); }
}
.custom-marker {
    background: white;
    border-radius: 50%;
    box-shadow: 0 2px 5px rgba(0,0,0,0.3);
    display: flex;
    align-items: center;
    justify-content: center;
}
</style    map.removeLayer(selectedMarker);
        }
        
        // Crear nuevo marcador
        selectedMarker = L.marker(e.latlng, {
            draggable: true
        }).addTo(map);
        
        // Actualizar campos del formulario
        document.getElementById('latitud').value = e.latlng.lat;
        document.getElementById('longitud').value = e.latlng.lng;
    }
});

// Botón para centrar en mi ubicación
document.getElementById('btnMiUbicacion').addEventListener('click', function() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            map.setView([position.coords.latitude, position.coords.longitude], 18);
        });
    }
});
</script>
```

### 📊 Limitaciones y Estrategias de Optimización

| Recurso | Cuota Gratuita | Optimización Aplicada |
|---------|----------------|----------------------|
| Geocoding API | 28,500 llamadas/mes | Cache Redis 24h para ubicaciones del campus |
| Directions API | 28,500 llamadas/mes | Pre-calcular rutas comunes (biblioteca ↔ cafetería) |
| Maps JavaScript API | 28,000 cargas/mes | Usar Leaflet.js con tiles OpenStreetMap (ilimitado) |

**Estrategia**: Usar **OpenStreetMap + Leaflet.js** como solución principal (gratuita, sin límites). Reservar Google Maps API solo para funcionalidades avanzadas como geocoding de alta precisión.

---

## 2️⃣ Comparador de Precios Amazon (Enfoque 100% Gratuito)

### 🎯 Estrategia Sin APIs de Pago

En lugar de usar APIs costosas de Amazon o Keepa, implementamos un **enfoque híbrido gratuito**:

1. **El usuario busca productos en tu marketplace** (productos reales de estudiantes)
2. **Para cada producto, sugieres búsqueda manual en Amazon** con enlace directo
3. **Integras widget gratuito de Keepa** que muestra gráfica de precios sin coste
4. **Alternativa**: Usar **CamelCamelCamel tracker** (también gratuito)

### ✅ Casos de Uso

✅ Ver productos del marketplace UFV Shares  
✅ Comparar con precios Amazon mediante búsqueda sugerida  
✅ Visualizar histórico de precios con widget Keepa embebido  
✅ Decidir si comprar nuevo (Amazon) o usado (marketplace)  
✅ Crear alertas de precio usando extensión CamelCamelCamel (gratis)  

### 🛠️ Implementación Backend (Generar Enlaces Inteligentes)

```java
@Service
@Slf4j
public class ComparadorService {
    
    @Autowired
    private ProductoRepository productoRepo;
    
    /**
     * Genera enlace de búsqueda en Amazon basado en producto del marketplace
     */
    public String generarEnlaceAmazon(Producto producto) {
        // Limpiar y formatear título para búsqueda
        String queryLimpia = producto.getTitulo()
            .replaceAll("[^a-zA-Z0-9\\s]", "")
            .replace(" ", "+");
        
        // Construir URL de búsqueda Amazon
        String urlAmazon = String.format(
            "https://www.amazon.es/s?k=%s&tag=ufvshares-21",
            queryLimpia
        );
        
        return urlAmazon;
    }
    
    /**
     * Genera sugerencias de productos similares en Amazon
     */
    public ComparadorDTO obtenerComparacion(Long productoId) {
        Producto producto = productoRepo.findById(productoId)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));
        
        return ComparadorDTO.builder()
            .productoLocal(convertirADTO(producto))
            .enlaceAmazon(generarEnlaceAmazon(producto))
            .sugerenciaBusqueda(generarSugerencia(producto))
            .mensajeComparacion(
                "💡 Compara el precio con Amazon antes de decidir. " +
                "¡Puede que encuentres una oferta mejor o prefieras apoyar a un compañero!"
            )
            .build();
    }
    
    private String generarSugerencia(Producto producto) {
        return String.format(
            "Busca '%s %s' en Amazon para comparar precios",
            producto.getCategoria().getNombre(),
            producto.getTitulo()
        );
    }
}
```

### 🎨 Vista de Comparación con Widget Keepa Embebido

```html
<!-- Vista de Detalle de Producto con Comparador -->
<div class="container mt-4">
    <div class="row">
        <!-- Producto del Marketplace UFV -->
        <div class="col-md-6">
            <div class="card">
                <img th:src="${producto.fotoPrincipal}" class="card-img-top" alt="Producto">
                <div class="card-body">
                    <span class="badge bg-success">Vendido por estudiante UFV</span>
                    <h3 class="mt-2" th:text="${producto.titulo}"></h3>
                    <p th:text="${producto.descripcion}"></p>
                    
                    <div class="precio-local">
                        <h2 class="text-primary">
                            <span th:text="${#numbers.formatDecimal(producto.precio, 1, 2)}"></span> €
                        </h2>
                        <small class="text-muted">Estado: <span th:text="${producto.estadoProducto}"></span></small>
                    </div>
                    
                    <hr>
                    
                    <div class="vendedor-info">
                        <p><strong>Vendedor:</strong> <span th:text="${producto.vendedor.nombre}"></span></p>
                        <p>⭐ Valoración: <span th:text="${producto.vendedor.valoracionPromedio}"></span>/5</p>
                        <p>📍 Entrega en: <span th:text="${producto.ubicacion.nombre}"></span></p>
                    </div>
                    
                    <button class="btn btn-primary btn-lg w-100 mt-3" onclick="contactarVendedor()">
                        💬 Contactar al vendedor
                    </button>
                </div>
            </div>
        </div>
        
        <!-- Comparación con Amazon -->
        <div class="col-md-6">
            <div class="card border-warning">
                <div class="card-header bg-warning text-dark">
                    <h5 class="mb-0">📊 Comparar con Amazon</h5>
                </div>
                <div class="card-body">
                    <div class="alert alert-info">
                        <strong>💡 Sugerencia:</strong>
                        <p th:text="${comparador.sugerenciaBusqueda}"></p>
                    </div>
                    
                    <!-- Botón para buscar en Amazon -->
                    <a th:href="${comparador.enlaceAmazon}" 
                       target="_blank" 
                       class="btn btn-warning btn-lg w-100 mb-3">
                        🔍 Buscar en Amazon
                    </a>
                    
                    <hr>
                    
                    <!-- Sección: Pegar ASIN de Amazon -->
                    <h6>¿Encontraste el producto en Amazon?</h6>
                    <p class="small text-muted">
                        Pega el código ASIN (ejemplo: B08N5WRWNW) o la URL completa:
                    </p>
                    
                    <div class="input-group mb-3">
                        <input type="text" 
                               id="inputAsin" 
                               class="form-control" 
                               placeholder="Ej: B08N5WRWNW o URL completa">
                        <button class="btn btn-success" onclick="mostrarHistorico()">
                            📈 Ver Histórico de Precios
                        </button>
                    </div>
                    
                    <!-- Widget de Keepa (se carga dinámicamente) -->
                    <div id="keepaWidget" style="display: none;">
                        <h6 class="mt-3">📈 Histórico de Precios (Keepa)</h6>
                        <div id="keepaGraph" style="width: 100%; height: 300px;"></div>
                        <small class="text-muted">
                            Datos proporcionados por Keepa.com - Actualizado cada 6 horas
                        </small>
                    </div>
                    
                    <!-- Alternativa: CamelCamelCamel -->
                    <div id="camelWidget" style="display: none; margin-top: 20px;">
                        <h6>🐫 O usa CamelCamelCamel:</h6>
                        <a id="camelLink" href="#" target="_blank" class="btn btn-outline-info w-100">
                            Ver en CamelCamelCamel
                        </a>
                        <small class="text-muted d-block mt-2">
                            Extensión gratuita para alertas de precio: 
                            <a href="https://camelcamelcamel.com/extension" target="_blank">Descargar</a>
                        </small>
                    </div>
                </div>
            </div>
            
            <!-- Ventajas de comprar local -->
            <div class="alert alert-success mt-3">
                <h6>✅ Ventajas de comprar en UFV Shares:</h6>
                <ul class="mb-0">
                    <li>🤝 Apoyas a un compañero de universidad</li>
                    <li>📍 Recogida en campus (sin envío)</li>
                    <li>👀 Puedes ver el producto antes de comprar</li>
                    <li>♻️ Contribuyes al consumo sostenible</li>
                    <li>💰 Normalmente más barato que nuevo</li>
                </ul>
            </div>
        </div>
    </div>
</div>

<script>
function mostrarHistorico() {
    const input = document.getElementById('inputAsin').value.trim();
    
    if (!input) {
        alert('Por favor, ingresa un ASIN o URL de Amazon');
        return;
    }
    
    // Extraer ASIN de URL si es necesario
    let asin = input;
    const matchASIN = input.match(/\/dp\/([A-Z0-9]{10})/);
    if (matchASIN) {
        asin = matchASIN[1];
    }
    
    // Validar formato ASIN
    if (!/^[A-Z0-9]{10}$/.test(asin)) {
        alert('ASIN inválido. Debe ser 10 caracteres alfanuméricos (ej: B08N5WRWNW)');
        return;
    }
    
    // Mostrar widget de Keepa (GRATUITO - embebido)
    document.getElementById('keepaWidget').style.display = 'block';
    document.getElementById('keepaGraph').innerHTML = `
        <iframe 
            src="https://keepa.com/iframe_addon.html?domain=4&asin=${asin}" 
            width="100%" 
            height="300" 
            frameborder="0">
        </iframe>
    `;
    
    // Mostrar enlace a CamelCamelCamel (alternativa gratuita)
    document.getElementById('camelWidget').style.display = 'block';
    document.getElementById('camelLink').href = 
        `https://es.camelcamelcamel.com/product/${asin}`;
    
    // Guardar ASIN en backend para estadísticas (opcional)
    fetch('/api/comparador/registrar', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            productoId: /*[[${producto.id}]]*/ 0,
            asin: asin
        })
    });
}

function contactarVendedor() {
    window.location.href = '/mensajes/nuevo?vendedorId=' + /*[[${producto.vendedor.id}]]*/ 0;
}

// Auto-detectar ASIN si viene en URL
const urlParams = new URLSearchParams(window.location.search);
if (urlParams.has('asin')) {
    document.getElementById('inputAsin').value = urlParams.get('asin');
    mostrarHistorico();
}
</script>
```

### 📋 Controlador REST

```java
@RestController
@RequestMapping("/api/comparador")
public class ComparadorController {
    
    @Autowired
    private ComparadorService comparadorService;
    
    @GetMapping("/producto/{id}")
    public ResponseEntity<ComparadorDTO> obtenerComparacion(@PathVariable Long id) {
        ComparadorDTO comparacion = comparadorService.obtenerComparacion(id);
        return ResponseEntity.ok(comparacion);
    }
    
    /**
     * Registra cuando un usuario compara con Amazon (analytics)
     */
    @PostMapping("/registrar")
    public ResponseEntity<Void> registrarComparacion(
            @RequestBody ComparacionRequest request) {
        
        log.info("Usuario comparó producto {} con ASIN {}", 
            request.getProductoId(), request.getAsin());
        
        // Opcional: guardar estadística para análisis futuro
        // ¿Qué productos se comparan más? ¿Con qué ASINs?
        
        return ResponseEntity.ok().build();
    }
}
```

### 🎯 Casos de Uso

✅ **Búsqueda de productos** en Amazon por palabra clave  
✅ **Comparación de precios** en tiempo real  
✅ **Obtención de detalles** de producto (título, precio, imagen, enlace)  
✅ **Integración con Keepa** para históricos de precio  

### 📦 Configuración de Dependencias

```xml
<!-- pom.xml -->
<dependency>
    <groupId>com.amazon.paapi5</groupId>
    <artifactId>paapi5-java-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### ⚙️ Configuración de Spring Boot

```java
@Configuration
public class AmazonAPIConfig {
    
    @Value("${amazon.api.access.key}")
    private String accessKey;
    
    @Value("${amazon.api.secret.key}")
    private String secretKey;
    
    @Value("${amazon.api.partner.tag}")
    private String partnerTag;
    
    @Value("${amazon.api.region:es}")
    private String region;
    
    @Bean
    public ApiClient amazonClient() {
        ApiClient client = new ApiClient();
        client.setAccessKey(accessKey);
        client.setSecretKey(secretKey);
        client.setHost("webservices.amazon.es");
        client.setRegion(region);
        return client;
    }
    
    @Bean
    public DefaultApi amazonApi(ApiClient client) {
        return new DefaultApi(client);
    }
}
```

### 🛠️ Servicio de Amazon

```java
@Service
@Slf4j
public class AmazonService {
    
    @Autowired
    private DefaultApi amazonApi;
    
    @Autowired
    private ProductoAmazonRepository productoRepo;
    
    @Autowired
    private CacheManager cacheManager;
    
    @Value("${amazon.api.partner.tag}")
    private String partnerTag;
    
    /**
     * Busca productos por palabra clave con cache
     */
    @Cacheable(value = "amazonSearch", key = "#query", unless = "#result.isEmpty()")
    public List<ProductoAmazonDTO> buscarProductos(String query) {
        try {
            SearchItemsRequest request = SearchItemsRequest.builder()
                .keywords(query)
                .searchIndex("All")
                .itemCount(10)
                .partnerTag(partnerTag)
                .partnerType(PartnerType.ASSOCIATES)
                .resources(Arrays.asList(
                    SearchItemsResource.IMAGES_PRIMARY_LARGE,
                    SearchItemsResource.ITEM_INFO_TITLE,
                    SearchItemsResource.OFFERS_LISTINGS_PRICE
                ))
                .build();
            
            SearchItemsResponse response = amazonApi.searchItems(request);
            
            if (response.getSearchResult() != null) {
                return response.getSearchResult().getItems().stream()
                    .map(this::convertirADTO)
                    .collect(Collectors.toList());
            }
            
        } catch (ApiException e) {
            log.error("Error buscando en Amazon: {} - {}", 
                e.getCode(), e.getMessage());
        }
        
        return Collections.emptyList();
    }
    
    /**
     * Obtiene detalles completos de producto por ASIN
     */
    public ProductoAmazonDTO obtenerProducto(String asin) {
        // Verificar cache primero
        ProductoAmazon cached = productoRepo.findByAsin(asin).orElse(null);
        if (cached != null && cached.esActualizado()) {
            return convertirADTO(cached);
        }
        
        try {
            GetItemsRequest request = GetItemsRequest.builder()
                .itemIds(Collections.singletonList(asin))
                .partnerTag(partnerTag)
                .partnerType(PartnerType.ASSOCIATES)
                .resources(Arrays.asList(
                    GetItemsResource.IMAGES_PRIMARY_LARGE,
                    GetItemsResource.ITEM_INFO_TITLE,
                    GetItemsResource.ITEM_INFO_FEATURES,
                    GetItemsResource.OFFERS_LISTINGS_PRICE,
                    GetItemsResource.OFFERS_LISTINGS_AVAILABILITY
                ))
                .build();
            
            GetItemsResponse response = amazonApi.getItems(request);
            
            if (response.getItemsResult() != null 
                    && !response.getItemsResult().getItems().isEmpty()) {
                
                Item item = response.getItemsResult().getItems().get(0);
                
                // Guardar/actualizar en BD
                ProductoAmazon producto = guardarOActualizarProducto(item);
                
                return convertirADTO(producto);
            }
            
        } catch (ApiException e) {
            log.error("Error obteniendo producto {}: {}", asin, e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Guarda o actualiza producto en base de datos
     */
    private ProductoAmazon guardarOActualizarProducto(Item item) {
        ProductoAmazon producto = productoRepo.findByAsin(item.getASIN())
            .orElse(new ProductoAmazon());
        
        producto.setAsin(item.getASIN());
        producto.setTitulo(item.getItemInfo().getTitle().getDisplayValue());
        producto.setImagenUrl(item.getImages().getPrimary().getLarge().getURL());
        producto.setUrl("https://www.amazon.es/dp/" + item.getASIN());
        
        // Extraer precio actual
        if (item.getOffers() != null 
                && !item.getOffers().getListings().isEmpty()) {
            OfferListing offer = item.getOffers().getListings().get(0);
            producto.setPrecioActual(offer.getPrice().getAmount());
        }
        
        producto.setUltimaActualizacion(LocalDateTime.now());
        
        return productoRepo.save(producto);
    }
    
    /**
     * Actualización masiva nocturna de productos populares
     */
    @Scheduled(cron = "0 0 3 * * *") // 3:00 AM diariamente
    public void actualizarProductosPopulares() {
        List<ProductoAmazon> populares = productoRepo
            .findTop50ByOrderByNumeroConsultasDesc();
        
        for (ProductoAmazon producto : populares) {
            try {
                obtenerProducto(producto.getAsin());
                Thread.sleep(1000); // Rate limiting: 1 req/segundo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        log.info("Actualizados {} productos populares", populares.size());
    }
    
    private ProductoAmazonDTO convertirADTO(Item item) {
        return ProductoAmazonDTO.builder()
            .asin(item.getASIN())
            .titulo(item.getItemInfo().getTitle().getDisplayValue())
            .precio(item.getOffers().getListings().get(0).getPrice().getAmount())
            .imagenUrl(item.getImages().getPrimary().getLarge().getURL())
            .url("https://www.amazon.es/dp/" + item.getASIN())
            .build();
    }
}
```

### 🎨 Controlador REST

```java
@RestController
@RequestMapping("/api/comparador")
@CrossOrigin(origins = "*")
public class ComparadorController {
    
    @Autowired
    private AmazonService amazonService;
    
    @Autowired
    private KeepaService keepaService;
    
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoAmazonDTO>> buscar(
            @RequestParam String q) {
        
        if (q == null || q.trim().length() < 3) {
            return ResponseEntity.badRequest().build();
        }
        
        List<ProductoAmazonDTO> productos = amazonService.buscarProductos(q);
        return ResponseEntity.ok(productos);
    }
    
    @GetMapping("/producto/{asin}")
    public ResponseEntity<ProductoDetalleDTO> detalleProducto(
            @PathVariable String asin,
            @RequestParam(defaultValue = "90") int dias) {
        
        ProductoAmazonDTO producto = amazonService.obtenerProducto(asin);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Obtener histórico de Keepa
        List<HistoricoPrecioDTO> historico = keepaService.obtenerHistorico(asin, dias);
        
        // Calcular métricas
        BigDecimal precioMinimo = historico.stream()
            .map(HistoricoPrecioDTO::getPrecio)
            .min(BigDecimal::compareTo)
            .orElse(producto.getPrecio());
        
        BigDecimal precioPromedio = historico.stream()
            .map(HistoricoPrecioDTO::getPrecio)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(new BigDecimal(historico.size()), 2, RoundingMode.HALF_UP);
        
        boolean esOferta = keepaService.esOferta(asin, producto.getPrecio());
        
        ProductoDetalleDTO detalle = ProductoDetalleDTO.builder()
            .producto(producto)
            .historico(historico)
            .precioMinimo(precioMinimo)
            .precioPromedio(precioPromedio)
            .esOferta(esOferta)
            .porcentajeDescuento(calcularDescuento(producto.getPrecio(), precioPromedio))
            .build();
        
        return ResponseEntity.ok(detalle);
    }
    
    private int calcularDescuento(BigDecimal actual, BigDecimal promedio) {
        if (promedio.compareTo(BigDecimal.ZERO) == 0) return 0;
        return promedio.subtract(actual)
            .divide(promedio, 2, RoundingMode.HALF_UP)
            .multiply(new BigDecimal(100))
            .intValue();
    }
}
```

### 📊 Limitaciones y Cuotas

| Recurso | Cuota Gratuita | Restricciones |
|---------|----------------|---------------|
| Search Items | 8,640 llamadas/día | Requiere partner tag con ventas activas |
| Get Items | 8,640 llamadas/día | Máximo 10 ASINs por llamada |
| Rate Limit | 1 req/segundo | Pausar 1 segundo entre llamadas |

**Requisitos**:
- Tener cuenta Amazon Associates activa
- Generar al menos 3 ventas en 180 días para mantener acceso
- Partner tag válido en todas las peticiones

---

## 3️⃣ Keepa API - Histórico de Precios Amazon

### 🎯 Casos de Uso

✅ **Gráficas históricas** de precios (30/90/365 días)  
✅ **Detección automática de ofertas** (precio 15% bajo promedio)  
✅ **Alertas personalizadas** cuando precio objetivo se alcanza  
✅ **Precio mínimo histórico** para toma de decisiones informadas  

### 📦 Configuración HTTP Client

```java
@Configuration
public class KeepaConfig {
    
    @Value("${keepa.api.key}")
    private String apiKey;
    
    @Bean
    public RestTemplate keepaRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        // Timeout de 10 segundos
        HttpComponentsClientHttpRequestFactory factory = 
            new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(10000);
        
        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }
}
```

### 🛠️ Servicio de Keepa

```java
@Service
@Slf4j
public class KeepaService {
    
    @Autowired
    private RestTemplate keepaRestTemplate;
    
    @Autowired
    private HistoricoPrecioRepository historicoRepo;
    
    @Value("${keepa.api.key}")
    private String apiKey;
    
    private static final String KEEPA_API_URL = "https://api.keepa.com";
    private static final int KEEPA_DOMAIN_ES = 4; // España
    
    /**
     * Obtiene histórico de precios de un producto
     */
    public List<HistoricoPrecioDTO> obtenerHistorico(String asin, int dias) {
        try {
            String url = String.format(
                "%s/product?key=%s&domain=%d&asin=%s&stats=30",
                KEEPA_API_URL, apiKey, KEEPA_DOMAIN_ES, asin
            );
            
            KeepaResponse response = keepaRestTemplate.getForObject(
                url, KeepaResponse.class
            );
            
            if (response != null && response.getProducts() != null 
                    && !response.getProducts().isEmpty()) {
                
                KeepaProduct product = response.getProducts().get(0);
                
                // Keepa devuelve datos comprimidos en formato CSV
                int[][] csvData = product.getCsv();
                
                if (csvData != null && csvData.length > 0) {
                    // csvData[0] = Amazon price
                    // csvData[1] = Amazon price (new)
                    // csvData[2] = Used price
                    // ... (ver documentación Keepa)
                    
                    return parsearHistorico(csvData[0], dias, asin);
                }
            }
            
        } catch (Exception e) {
            log.error("Error obteniendo histórico de Keepa para {}: {}", 
                asin, e.getMessage());
        }
        
        return Collections.emptyList();
    }
    
    /**
     * Parsea datos comprimidos de Keepa a lista de históricos
     */
    private List<HistoricoPrecioDTO> parsearHistorico(
            int[] csvData, int dias, String asin) {
        
        List<HistoricoPrecioDTO> historico = new ArrayList<>();
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
        
        // Datos vienen en pares: [timestamp_keepa, precio_centavos, ...]
        for (int i = 0; i < csvData.length; i += 2) {
            if (i + 1 >= csvData.length) break;
            
            int timestampKeepa = csvData[i];
            int precioCentavos = csvData[i + 1];
            
            // Convertir timestamp Keepa a LocalDateTime
            LocalDateTime fecha = keepaTimestampToDateTime(timestampKeepa);
            
            // Filtrar por rango de fechas
            if (fecha.isAfter(fechaLimite)) {
                HistoricoPrecio registro = new HistoricoPrecio();
                registro.setFecha(fecha);
                registro.setPrecio(convertirCentavosAEuros(precioCentavos));
                registro.setFuente("keepa");
                
                // Guardar en BD
                historicoRepo.save(registro);
                
                historico.add(convertirADTO(registro));
            }
        }
        
        return historico;
    }
    
    /**
     * Determina si el precio actual es una oferta
     */
    public boolean esOferta(String asin, BigDecimal precioActual) {
        List<HistoricoPrecioDTO> historico = obtenerHistorico(asin, 30);
        
        if (historico.isEmpty()) return false;
        
        // Calcular precio promedio últimos 30 días
        BigDecimal precioPromedio = historico.stream()
            .map(HistoricoPrecioDTO::getPrecio)
            .filter(precio -> precio.compareTo(BigDecimal.ZERO) > 0)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(new BigDecimal(historico.size()), 2, RoundingMode.HALF_UP);
        
        // Es oferta si está 15% por debajo del promedio
        BigDecimal umbral = precioPromedio.multiply(new BigDecimal("0.85"));
        return precioActual.compareTo(umbral) < 0;
    }
    
    /**
     * Obtiene precio mínimo histórico
     */
    public BigDecimal obtenerPrecioMinimo(String asin) {
        return obtenerHistorico(asin, 365).stream()
            .map(HistoricoPrecioDTO::getPrecio)
            .filter(precio -> precio.compareTo(BigDecimal.ZERO) > 0)
            .min(BigDecimal::compareTo)
            .orElse(BigDecimal.ZERO);
    }
    
    /**
     * Obtiene precio actual desde Keepa (más actualizado que Amazon API)
     */
    public BigDecimal obtenerPrecioActual(String asin) {
        try {
            String url = String.format(
                "%s/product?key=%s&domain=%d&asin=%s&stats=0",
                KEEPA_API_URL, apiKey, KEEPA_DOMAIN_ES, asin
            );
            
            KeepaResponse response = keepaRestTemplate.getForObject(
                url, KeepaResponse.class
            );
            
            if (response != null && response.getProducts() != null 
                    && !response.getProducts().isEmpty()) {
                
                KeepaProduct product = response.getProducts().get(0);
                int[][] csv = product.getCsv();
                
                if (csv != null && csv.length > 0 && csv[0].length >= 2) {
                    // Último precio registrado
                    int precioCentavos = csv[0][csv[0].length - 1];
                    return convertirCentavosAEuros(precioCentavos);
                }
            }
        } catch (Exception e) {
            log.error("Error obteniendo precio actual de Keepa: {}", e.getMessage());
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Convierte timestamp de Keepa a LocalDateTime
     * Keepa epoch: 21 de enero de 2011 00:00 UTC
     */
    private LocalDateTime keepaTimestampToDateTime(int minutosDesdeEpoch) {
        LocalDateTime keepaEpoch = LocalDateTime.of(2011, 1, 21, 0, 0);
        return keepaEpoch.plusMinutes(minutosDesdeEpoch);
    }
    
    /**
     * Convierte precio de Keepa (centavos) a euros
     * Valor -1 en Keepa = producto no disponible
     */
    private BigDecimal convertirCentavosAEuros(int centavos) {
        if (centavos < 0) return BigDecimal.ZERO;
        return new BigDecimal(centavos).divide(
            new BigDecimal(100), 2, RoundingMode.HALF_UP
        );
    }
}
```

### 🎨 Vista con Gráfica de Precios (Chart.js)

```html
<!-- Vista de Detalle de Producto con Gráfica -->
<div class="container mt-4">
    <div class="row">
        <div class="col-md-6">
            <img th:src="${producto.imagenUrl}" class="img-fluid rounded" alt="Producto">
            <h3 class="mt-3" th:text="${producto.titulo}"></h3>
            
            <div class="precio-actual mt-3">
                <h2 class="text-success">
                    <span th:text="${#numbers.formatDecimal(producto.precio, 1, 2)}"></span> €
                    <span th:if="${esOferta}" class="badge bg-danger ms-2">
                        ¡OFERTA! -<span th:text="${porcentajeDescuento}"></span>%
                    </span>
                </h2>
            </div>
            
            <div class="estadisticas mt-3">
                <p>
                    💰 <strong>Precio mínimo histórico:</strong> 
                    <span class="text-info" th:text="${#numbers.formatDecimal(precioMinimo, 1, 2)}"></span> €
                </p>
                <p>
                    📊 <strong>Precio promedio (90 días):</strong> 
                    <span th:text="${#numbers.formatDecimal(precioPromedio, 1, 2)}"></span> €
                </p>
            </div>
            
            <div class="acciones mt-4">
                <a th:href="${producto.url}" target="_blank" 
                   class="btn btn-primary btn-lg">
                    🛒 Ver en Amazon
                </a>
                <button onclick="configurarAlerta()" class="btn btn-warning btn-lg ms-2">
                    🔔 Crear Alerta de Precio
                </button>
            </div>
        </div>
        
        <div class="col-md-6">
            <h4>📈 Evolución de Precio</h4>
            <canvas id="chartPrecios" width="400" height="300"></canvas>
            
            <div class="btn-group mt-3" role="group">
                <button th:onclick="|cambiarPeriodo(30)|" 
                        class="btn btn-sm btn-outline-secondary">30 días</button>
                <button th:onclick="|cambiarPeriodo(90)|" 
                        class="btn btn-sm btn-outline-secondary active">90 días</button>
                <button th:onclick="|cambiarPeriodo(365)|" 
                        class="btn btn-sm btn-outline-secondary">1 año</button>
            </div>
            
            <div class="alert alert-info mt-3">
                <small>
                    📌 Datos actualizados cada 6 horas mediante Keepa API
                </small>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-adapter-date-fns@3.0.0/dist/chartjs-adapter-date-fns.bundle.min.js"></script>

<script>
// Datos del histórico pasados desde Thymeleaf
const historico = /*[[${historico}]]*/ [];
const precioActual = /*[[${producto.precio}]]*/ 0;

// Preparar datos para Chart.js
const labels = historico.map(h => new Date(h.fecha));
const precios = historico.map(h => h.precio);

// Crear gráfica
const ctx = document.getElementById('chartPrecios').getContext('2d');
const chart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: labels,
        datasets: [
            {
                label: 'Precio Amazon',
                data: precios,
                borderColor: 'rgb(75, 192, 192)',
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                tension: 0.1,
                fill: true,
                pointRadius: 2,
                pointHoverRadius: 5
            },
            {
                label: 'Precio Actual',
                data: Array(labels.length).fill(precioActual),
                borderColor: 'rgb(255, 99, 132)',
                borderDash: [5, 5],
                pointRadius: 0,
                tension: 0
            }
        ]
    },
    options: {
        responsive: true,
        plugins: {
            legend: {
                display: true,
                position: 'top'
            },
            tooltip: {
                mode: 'index',
                intersect: false,
                callbacks: {
                    label: function(context) {
                        return context.dataset.label + ': ' + 
                               context.parsed.y.toFixed(2) + ' €';
                    }
                }
            }
        },
        scales: {
            y: {
                beginAtZero: false,
                ticks: {
                    callback: function(value) {
                        return value.toFixed(2) + ' €';
                    }
                }
            },
            x: {
                type: 'time',
                time: {
                    unit: 'day',
                    displayFormats: {
                        day: 'dd MMM'
                    }
                },
                title: {
                    display: true,
                    text: 'Fecha'
                }
            }
        },
        interaction: {
            mode: 'nearest',
            axis: 'x',
            intersect: false
        }
    }
});

function cambiarPeriodo(dias) {
    const asin = '[[${producto.asin}]]';
    window.location.href = `/comparador/producto/${asin}?dias=${dias}`;
}

function configurarAlerta() {
    const precioObjetivo = prompt('¿A qué precio quieres recibir una alerta?');
    if (precioObjetivo && !isNaN(precioObjetivo)) {
        fetch('/api/alertas/crear', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                asin: '[[${producto.asin}]]',
                precioObjetivo: parseFloat(precioObjetivo)
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('✅ Alerta creada correctamente! Te notificaremos por email cuando el precio baje a ' + precioObjetivo + ' €');
            } else {
                alert('❌ Error al crear alerta: ' + data.mensaje);
            }
        })
        .catch(error => {
            alert('❌ Error de conexión. Inténtalo de nuevo.');
        });
    }
}
</script>
```

### 🔔 Sistema de Alertas Automático

```java
@Service
public class AlertaService {
    
    @Autowired
    private AlertaPrecioRepository alertaRepo;
    
    @Autowired
    private KeepaService keepaService;
    
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private ProductoAmazonRepository productoAmazonRepo;
    
    /**
     * Tarea programada para verificar alertas (cada 6 horas)
     */
    @Scheduled(cron = "0 0 */6 * * *")
    public void verificarAlertas() {
        log.info("Iniciando verificación de alertas de precio...");
        
        List<AlertaPrecio> alertasActivas = alertaRepo.findByActivaTrue();
        int alertasDisparadas = 0;
        
        for (AlertaPrecio alerta : alertasActivas) {
            try {
                ProductoAmazon producto = alerta.getProducto();
                
                // Actualizar precio actual desde Keepa
                BigDecimal precioActual = keepaService.obtenerPrecioActual(
                    producto.getAsin()
                );
                
                // Actualizar en BD
                producto.setPrecioActual(precioActual);
                producto.setUltimaActualizacion(LocalDateTime.now());
                productoAmazonRepo.save(producto);
                
                // Verificar si se alcanzó el precio objetivo
                if (precioActual.compareTo(BigDecimal.ZERO) > 0 
                        && precioActual.compareTo(alerta.getPrecioObjetivo()) <= 0) {
                    
                    // ¡Precio alcanzado! Enviar notificación
                    notificationService.enviarAlertaPrecio(alerta, precioActual);
                    
                    // Desactivar alerta
                    alerta.setActiva(false);
                    alerta.setFechaDisparada(LocalDateTime.now());
                    alertaRepo.save(alerta);
                    
                    alertasDisparadas++;
                    
                    log.info("Alerta disparada para usuario {} - Producto: {} ({}€ <= {}€)",
                        alerta.getUsuario().getEmail(),
                        producto.getTitulo(),
                        precioActual,
                        alerta.getPrecioObjetivo());
                }
                
                // Rate limiting: pausar 1 segundo entre consultas
                Thread.sleep(1000);
                
            } catch (Exception e) {
                log.error("Error verificando alerta ID {}: {}", 
                    alerta.getId(), e.getMessage());
            }
        }
        
        log.info("Verificación completada: {}/{} alertas disparadas", 
            alertasDisparadas, alertasActivas.size());
    }
    
    /**
     * Crea una nueva alerta de precio
     */
    public AlertaPrecio crearAlerta(Long usuarioId, String asin, 
                                     BigDecimal precioObjetivo) {
        
        // Validar límite de alertas por usuario (máximo 10)
        long alertasActivas = alertaRepo.countByUsuarioIdAndActivaTrue(usuarioId);
        if (alertasActivas >= 10) {
            throw new IllegalStateException(
                "Límite de alertas activas alcanzado (10). Desactiva alguna alerta anterior."
            );
        }
        
        // Obtener o crear producto Amazon
        ProductoAmazon producto = productoAmazonRepo.findByAsin(asin)
            .orElseThrow(() -> new EntityNotFoundException(
                "Producto no encontrado. Búscalo primero en el comparador."
            ));
        
        // Crear alerta
        AlertaPrecio alerta = new AlertaPrecio();
        alerta.setUsuario(usuarioRepository.findById(usuarioId).orElseThrow());
        alerta.setProducto(producto);
        alerta.setPrecioObjetivo(precioObjetivo);
        alerta.setActiva(true);
        alerta.setCreatedAt(LocalDateTime.now());
        
        return alertaRepo.save(alerta);
    }
}
```

### 📧 Servicio de Notificaciones

```java
@Service
public class NotificationService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${app.mail.from}")
    private String fromEmail;
    
    /**
     * Envía notificación de alerta de precio
     */
    public void enviarAlertaPrecio(AlertaPrecio alerta, BigDecimal precioActual) {
        ProductoAmazon producto = alerta.getProducto();
        Usuario usuario = alerta.getUsuario();
        
        String asunto = String.format(
            "🔔 ¡Alerta de Precio! %s ahora a %.2f€",
            producto.getTitulo(),
            precioActual
        );
        
        String cuerpo = String.format("""
            <html>
            <body style="font-family: Arial, sans-serif;">
                <h2>¡Tu producto alcanzó el precio objetivo!</h2>
                <div style="border: 1px solid #ddd; padding: 20px; margin: 20px 0;">
                    <img src="%s" style="max-width: 200px;" />
                    <h3>%s</h3>
                    <p><strong>Precio actual:</strong> <span style="color: green; font-size: 24px;">%.2f €</span></p>
                    <p><strong>Tu precio objetivo:</strong> %.2f €</p>
                    <a href="%s" style="background-color: #ff9900; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">
                        Ver en Amazon
                    </a>
                </div>
                <p><small>Esta alerta ha sido desactivada automáticamente.</small></p>
            </body>
            </html>
            """,
            producto.getImagenUrl(),
            producto.getTitulo(),
            precioActual,
            alerta.getPrecioObjetivo(),
            producto.getUrl()
        );
        
        enviarEmail(usuario.getEmail(), asunto, cuerpo);
    }
    
    private void enviarEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true = HTML
            
            mailSender.send(message);
            
            log.info("Email enviado a {}: {}", to, subject);
            
        } catch (Exception e) {
            log.error("Error enviando email: {}", e.getMessage());
        }
    }
}
```

### 📊 Limitaciones y Cuotas de Keepa

| Plan | Precio | Llamadas/Mes | Tokens | Datos Históricos |
|------|--------|--------------|--------|------------------|
| **Free** | €0 | 0 | 0 | No disponible |
| **Hobby** | €19.90/mes | 300,000 | 5,000 | Últimos 3 meses |
| **Business** | €149/mes | 3,000,000 | 50,000 | Histórico completo |

**Estrategia de optimización para plan Hobby**:
- 300,000 llamadas/mes = 10,000/día = 416/hora
- Actualización automática cada 6 horas (4 veces/día)
- Priorizar productos con alertas activas
- Cache de 6 horas para gráficas
- Lazy loading: solo consultar cuando usuario lo solicita

---

## 🚀 Configuración de Despliegue

### Variables de Entorno Requeridas

```properties
# application.properties

# ===== Google Maps API =====
google.maps.api.key=${GOOGLE_MAPS_API_KEY}

# ===== Amazon Product Advertising API =====
amazon.api.access.key=${AMAZON_ACCESS_KEY}
amazon.api.secret.key=${AMAZON_SECRET_KEY}
amazon.api.partner.tag=${AMAZON_PARTNER_TAG}
amazon.api.region=es

# ===== Keepa API =====
keepa.api.key=${KEEPA_API_KEY}

# ===== Base de Datos PostgreSQL =====
spring.datasource.url=${AZURE_POSTGRESQL_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update

# ===== Cache Redis =====
spring.redis.host=${REDIS_HOST}
spring.redis.port=6379
spring.redis.password=${REDIS_PASSWORD}
spring.cache.type=redis

# ===== Email (Notificaciones) =====
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${EMAIL_USERNAME}
spring.mail.password=${EMAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
app.mail.from=noreply@ufvshares.com

# ===== Configuración de Tareas Programadas =====
spring.task.scheduling.pool.size=5
```

### Azure Key Vault (Producción)

```java
@Configuration
public class AzureKeyVaultConfig {
    
    @Value("${azure.keyvault.uri}")
    private String keyVaultUri;
    
    @Bean
    public SecretClient secretClient() {
        return new SecretClientBuilder()
            .vaultUrl(keyVaultUri)
            .credential(new DefaultAzureCredentialBuilder().build())
            .buildClient();
    }
    
    @Bean
    public String getGoogleMapsApiKey(SecretClient secretClient) {
        return secretClient.getSecret("google-maps-api-key").getValue();
    }
    
    @Bean
    public String getAmazonAccessKey(SecretClient secretClient) {
        return secretClient.getSecret("amazon-access-key").getValue();
    }
    
    @Bean
    public String getKeepaApiKey(SecretClient secretClient) {
        return secretClient.getSecret("keepa-api-key").getValue();
    }
}
```

---

## 💰 Análisis de Costes

| Servicio | Plan | Coste Mensual | Uso Estimado |
|----------|------|---------------|--------------|
| **Google Maps API** | Free Tier | €0 | 20,000 llamadas/mes |
| **OpenStreetMap + Leaflet** | Gratuito | €0 | Ilimitado |
| **Amazon Product API** | Gratuito (con ventas) | €0 | 5,000 llamadas/mes |
| **Keepa API** | Hobby | €19.90 | 300,000 llamadas/mes |
| **Azure PostgreSQL** | Basic B1ms | €13/mes | 5 GB storage |
| **Azure Redis Cache** | Basic C0 | €13/mes | 250 MB |
| **Azure App Service** | B1 | €12/mes | 1 core, 1.75 GB RAM |
| **AWS S3** | Free Tier | €0 | 5 GB storage, 20K GET |
| **SendGrid Email** | Free | €0 | 100 emails/día |

**Total estimado**: ~€60/mes para 500 usuarios activos

**Optimizaciones para reducir costes**:
1. Usar OpenStreetMap en lugar de Google Maps (ahorro €0, ya gratis)
2. Cache agresivo con Redis (reducir llamadas API 70%)
3. Batch updates nocturnos (concentrar llamadas fuera de pico)
4. Lazy loading de gráficas Keepa (solo cuando usuario lo solicita)

---

## 📚 Conclusiones

### Ventajas de la Integración

✅ **Propuesta de valor única**: Ningún competidor integra quedadas + mapas + comparador Amazon  
✅ **APIs maduras y documentadas**: Ecosistema robusto de Amazon/Google/Keepa  
✅ **Escalabilidad**: Cache y batch updates permiten crecer sin aumentar costes linealmente  
✅ **Experiencia de usuario superior**: Visualizaciones (mapas, gráficas) mejoran decisiones de compra  

### Riesgos y Mitigaciones

⚠️ **Dependencia de APIs de terceros**
- Mitigación: Implementar fallbacks (OpenStreetMap como alternativa a Google Maps)

⚠️ **Coste de Keepa (€19.90/mes)**
- Mitigación: Plan Hobby suficiente para MVP. Monetizar premium users para cubrir coste.

⚠️ **Límites de cuota Amazon API**
- Mitigación: Requiere 3 ventas/180 días para mantener acceso. Incluir enlaces de afiliado en marketplace.

---

**Documento generado para**: UFV Shares - Proyectos II (3º Año)  
**Profesor**: Roberto Rodríguez Galán  
**Fecha**: Sprint 2 - Fase de Diseño
