# 🔌 Implementación de APIs Externas - UFV Shares (100% GRATUITAS)

## 🎯 Filosofía: APIs Totalmente Gratuitas y Sin Límites

Todas las APIs integradas en UFV Shares son **100% gratuitas y sin límites restrictivos** para garantizar:
- ✅ Cero coste operativo mensual
- ✅ Sin riesgo de facturación sorpresa
- ✅ Escalabilidad sin barreras económicas
- ✅ No requieren tarjeta de crédito
- ✅ Proyecto sostenible a largo plazo

---

## Resumen de APIs Integradas

| # | API | Funcionalidad | Coste | Límites | API Key |
|---|-----|---------------|-------|---------|---------|
| 1 | **OpenStreetMap + Leaflet.js** | Mapas interactivos, geolocalización | €0 | Ilimitado | ❌ No |
| 2 | **Nominatim (OSM)** | Geocoding, búsqueda de ubicaciones | €0 | 1 req/seg | ❌ No |
| 3 | **Keepa Widget Embebido** | Gráficas históricas de precios Amazon | €0 | Ilimitado | ❌ No |
| 4 | **OpenWeather API** | Clima del campus para planificar quedadas | €0 | 1,000 req/día | ✅ Sí (gratis) |
| 5 | **Telegram Bot API** | Notificaciones push ilimitadas | €0 | Ilimitado | ✅ Sí (gratis) |
| 6 | **QR Code Generator API** | Códigos QR para compartir quedadas | €0 | Ilimitado | ❌ No |

**Coste total mensual**: **€0** (100% gratuito)

---

## 1️⃣ OpenStreetMap + Leaflet.js - Mapas del Campus (GRATUITO, ILIMITADO)

### 🎯 Casos de Uso

✅ Visualizar todas las quedadas activas en mapa interactivo  
✅ Seleccionar ubicación arrastrando marcador al crear quedada  
✅ Marcadores personalizados por tipo (📚 biblioteca, ☕ cafetería, 🌳 zona verde)  
✅ Geolocalización del navegador para centrar mapa en tu posición  
✅ Calcular distancias entre puntos (fórmula Haversine)  

### 💡 Ventajas vs Google Maps

| Característica | OpenStreetMap | Google Maps |
|----------------|---------------|-------------|
| **Coste** | €0 permanente | €200+ después de cuota |
| **Límites** | Ilimitado | 28,000 cargas/mes gratis |
| **API Key** | No requerida | Obligatoria + tarjeta crédito |
| **Tiles** | Gratuitos | De pago tras cuota |
| **Open Source** | Sí | No |

### 🌐 Implementación Frontend (HTML + JavaScript)

```html
<!DOCTYPE html>
<html>
<head>
    <title>Mapa de Quedadas UFV</title>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
</head>
<body>
    <div class="container mt-4">
        <div class="d-flex justify-content-between mb-3">
            <h2>🗺️ Quedadas en el Campus UFV</h2>
            <div>
                <button id="btnMiUbicacion" class="btn btn-primary">
                    📍 Mi Ubicación
                </button>
                <button id="btnClima" class="btn btn-info">
                    🌤️ Ver Clima
                </button>
            </div>
        </div>
        
        <!-- Mapa -->
        <div id="map" style="height: 600px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0,0,0,0.1);"></div>
        
        <!-- Info del clima (oculto inicialmente) -->
        <div id="climaInfo" class="alert alert-info mt-3" style="display: none;"></div>
    </div>

    <script>
    // ====== INICIALIZACIÓN DEL MAPA (GRATUITO, SIN API KEY) ======
    const map = L.map('map').setView([40.4426, -3.8120], 16); // Centro: UFV
    
    // Tiles de OpenStreetMap (GRATUITO, ILIMITADO)
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors',
        maxZoom: 19
    }).addTo(map);
    
    // ====== EMOJIS COMO ICONOS (sin necesidad de imágenes) ======
    const iconos = {
        'AULA': '🎓',
        'BIBLIOTECA': '📚',
        'CAFETERIA': '☕',
        'ZONA_VERDE': '🌳',
        'DEPORTIVO': '⚽',
        'EDIFICIO': '🏛️',
        'OTRO': '📍'
    };
    
    // ====== CARGAR QUEDADAS DESDE BACKEND ======
    fetch('/api/quedadas/activas')
        .then(res => res.json())
        .then(quedadas => {
            quedadas.forEach(q => {
                // Crear icono emoji personalizado
                const icon = L.divIcon({
                    html: `<div style="font-size: 30px; text-shadow: 2px 2px 4px rgba(0,0,0,0.3);">
                             ${iconos[q.ubicacion.tipo] || '📍'}
                           </div>`,
                    className: 'emoji-marker',
                    iconSize: [30, 30]
                });
                
                // Añadir marcador
                L.marker([q.ubicacion.latitud, q.ubicacion.longitud], {icon})
                    .bindPopup(`
                        <div style="min-width: 200px;">
                            <h5>${q.titulo}</h5>
                            <p class="small">${q.descripcion}</p>
                            <hr class="my-2">
                            <p class="mb-1"><strong>📅</strong> ${new Date(q.fechaHora).toLocaleString('es-ES')}</p>
                            <p class="mb-2"><strong>👥</strong> ${q.asistentes}/${q.maxParticipantes}</p>
                            <p class="mb-2"><strong>📍</strong> ${q.ubicacion.nombre}</p>
                            <a href="/quedadas/${q.id}" class="btn btn-sm btn-primary w-100">
                                Ver detalles y unirme
                            </a>
                        </div>
                    `)
                    .addTo(map);
            });
        });
    
    // ====== MODO CREAR QUEDADA: Seleccionar ubicación ======
    let marcadorSeleccionado = null;
    map.on('click', function(e) {
        // Solo si estamos en modo crear quedada
        if (document.getElementById('modoCrearQuedada')) {
            if (marcadorSeleccionado) map.removeLayer(marcadorSeleccionado);
            
            marcadorSeleccionado = L.marker(e.latlng, {
                draggable: true,
                icon: L.divIcon({
                    html: '<div style="font-size: 40px;">📍</div>',
                    className: 'nuevo-marcador'
                })
            }).addTo(map);
            
            actualizarFormularioCoordenadas(e.latlng);
            
            // Evento al arrastrar
            marcadorSeleccionado.on('dragend', function(event) {
                actualizarFormularioCoordenadas(event.target.getLatLng());
            });
        }
    });
    
    function actualizarFormularioCoordenadas(latlng) {
        document.getElementById('latitud').value = latlng.lat.toFixed(6);
        document.getElementById('longitud').value = latlng.lng.toFixed(6);
        
        // Buscar nombre de ubicación con Nominatim (GRATUITO)
        fetch(`https://nominatim.openstreetmap.org/reverse?lat=${latlng.lat}&lon=${latlng.lng}&format=json`, {
            headers: {'User-Agent': 'UFVShares/1.0 (contact@ufvshares.com)'}
        })
        .then(r => r.json())
        .then(data => {
            if (data.display_name) {
                document.getElementById('ubicacionNombre').value = data.display_name;
            }
        });
    }
    
    // ====== BOTÓN: Centrar en mi ubicación ======
    document.getElementById('btnMiUbicacion').addEventListener('click', () => {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(pos => {
                const lat = pos.coords.latitude;
                const lng = pos.coords.longitude;
                
                map.setView([lat, lng], 18);
                
                L.marker([lat, lng], {
                    icon: L.divIcon({
                        html: '<div style="font-size: 35px; animation: pulso 1s infinite;">📍</div>',
                        className: 'mi-ubicacion'
                    })
                }).addTo(map).bindPopup('📍 Estás aquí').openPopup();
            });
        } else {
            alert('Tu navegador no soporta geolocalización');
        }
    });
    
    // ====== BOTÓN: Ver clima del campus ======
    document.getElementById('btnClima').addEventListener('click', () => {
        fetch('/api/clima/campus')
            .then(r => r.json())
            .then(data => {
                document.getElementById('climaInfo').innerHTML = `
                    <strong>🌤️ Clima en UFV:</strong> ${data.descripcion} - ${data.temperatura}°C
                    (Sensación: ${data.sensacion}°C) | Humedad: ${data.humedad}% | Viento: ${data.viento} km/h
                `;
                document.getElementById('climaInfo').style.display = 'block';
            });
    });
    </script>
    
    <style>
    @keyframes pulso {
        0%, 100% { transform: scale(1); }
        50% { transform: scale(1.2); }
    }
    .emoji-marker {
        background: white;
        border-radius: 50%;
        padding: 5px;
        box-shadow: 0 2px 8px rgba(0,0,0,0.3);
        display: flex;
        align-items: center;
        justify-content: center;
    }
    </style>
</body>
</html>
```

### 🛠️ Servicio Backend (Nominatim + Haversine)

```java
@Service
@Slf4j
public class GeolocationService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org";
    
    /**
     * Busca ubicación con Nominatim (Geocoding gratuito de OSM)
     */
    public UbicacionDTO buscarUbicacion(String query) {
        try {
            String url = String.format(
                "%s/search?q=%s&format=json&limit=1&bounded=1&viewbox=-3.82,-3.80,40.44,40.45",
                NOMINATIM_URL,
                URLEncoder.encode("UFV campus " + query, StandardCharsets.UTF_8)
            );
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "UFVShares/1.0 (educativo)");
            
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
     * Calcula distancia entre dos puntos (Fórmula Haversine)
     */
    public double calcularDistancia(BigDecimal lat1, BigDecimal lon1, 
                                     BigDecimal lat2, BigDecimal lon2) {
        final int RADIO_TIERRA = 6371000; // metros
        
        double dLat = Math.toRadians(lat2.subtract(lat1).doubleValue());
        double dLon = Math.toRadians(lon2.subtract(lon1).doubleValue());
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1.doubleValue())) *
                   Math.cos(Math.toRadians(lat2.doubleValue())) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return RADIO_TIERRA * c;
    }
    
    /**
     * Encuentra quedadas cercanas a un punto (radio en metros)
     */
    public List<QuedadaDTO> obtenerQuedadasCercanas(BigDecimal lat, BigDecimal lng, int radioMetros) {
        List<Quedada> todasQuedadas = quedadaRepository.findByEstado(EstadoQuedada.PENDIENTE);
        
        return todasQuedadas.stream()
            .filter(q -> {
                double distancia = calcularDistancia(
                    lat, lng,
                    q.getUbicacion().getLatitud(),
                    q.getUbicacion().getLongitud()
                );
                return distancia <= radioMetros;
            })
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }
}
```

---

## 2️⃣ Comparador de Precios Amazon (ENFOQUE 100% GRATUITO con Widgets)

### 🎯 Estrategia: Sin APIs de Pago

En lugar de usar APIs costosas (Amazon Product API €0 + límites, Keepa API €19.90/mes), implementamos:

1. **Productos del marketplace** → el usuario busca en tu app primero
2. **Botón "Buscar en Amazon"** → enlace directo con término de búsqueda pre-rellenado
3. **Usuario pega ASIN o URL** → obtiene del producto de Amazon encontrado
4. **Widget Keepa embebido** → muestra gráfica de precios históricos **SIN COSTE** (iframe)
5. **Alternativa CamelCamelCamel** → otra opción gratuita de históricoprecios

### ✅ Casos de Uso

✅ Ver producto del marketplace UFV Shares  
✅ Comparar con Amazon mediante búsqueda sugerida  
✅ Pegar ASIN/URL de Amazon para ver histórico  
✅ Visualizar gráfica de precios con Keepa (gratis)  
✅ Decidir si comprar nuevo (Amazon) o de segunda mano (marketplace)  

### 🎨 Vista de Comparación

```html
<div class="container mt-4">
    <div class="row">
        <!-- IZQUIERDA: Producto del Marketplace UFV -->
        <div class="col-md-6">
            <div class="card border-success">
                <div class="card-header bg-success text-white">
                    <h5>🎓 Producto de Estudiante UFV</h5>
                </div>
                <div class="card-body">
                    <img th:src="${producto.fotoPrincipal}" class="img-fluid rounded mb-3" alt="Producto">
                    <h4 th:text="${producto.titulo}"></h4>
                    <p th:text="${producto.descripcion}"></p>
                    
                    <div class="precio-local bg-light p-3 rounded my-3">
                        <h2 class="text-success mb-0">
                            <span th:text="${#numbers.formatDecimal(producto.precio, 1, 2)}"></span> €
                        </h2>
                        <small class="text-muted">
                            Estado: <strong th:text="${producto.estadoProducto}"></strong>
                        </small>
                    </div>
                    
                    <div class="vendedor-info border-top pt-3">
                        <p><strong>Vendedor:</strong> <span th:text="${producto.vendedor.nombre}"></span></p>
                        <p>⭐ <span th:text="${producto.vendedor.valoracionPromedio}"></span>/5.0</p>
                        <p>📍 Entrega: <span th:text="${producto.ubicacion.nombre}"></span></p>
                    </div>
                    
                    <button class="btn btn-success btn-lg w-100 mt-3" onclick="contactarVendedor()">
                        💬 Contactar Vendedor
                    </button>
                    
                    <div class="alert alert-success mt-3 small">
                        <strong>✅ Ventajas de comprar aquí:</strong>
                        <ul class="mb-0 mt-2">
                            <li>Apoyas a un compañero</li>
                            <li>Sin gastos de envío</li>
                            <li>Puedes verlo antes de comprar</li>
                            <li>Consumo sostenible ♻️</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- DERECHA: Comparador con Amazon -->
        <div class="col-md-6">
            <div class="card border-warning">
                <div class="card-header bg-warning">
                    <h5 class="mb-0">📊 Comparar con Amazon</h5>
                </div>
                <div class="card-body">
                    <div class="alert alert-info">
                        <strong>💡 Sugerencia de búsqueda:</strong>
                        <p class="mb-0" th:text="${sugerencia}"></p>
                    </div>
                    
                    <!-- Botón búsqueda Amazon -->
                    <a th:href="@{https://www.amazon.es/s(k=${producto.titulo})}" 
                       target="_blank" 
                       class="btn btn-warning btn-lg w-100 mb-3">
                        🔍 Buscar en Amazon
                    </a>
                    
                    <hr>
                    
                    <!-- Input para ASIN -->
                    <h6>¿Encontraste el producto en Amazon?</h6>
                    <p class="small text-muted">
                        Pega el código ASIN (ej: <code>B08N5WRWNW</code>) o la URL completa:
                    </p>
                    
                    <div class="input-group mb-3">
                        <input type="text" 
                               id="inputAsin" 
                               class="form-control" 
                               placeholder="B08N5WRWNW o https://www.amazon.es/dp/...">
                        <button class="btn btn-primary" onclick="mostrarHistorico()">
                            📈 Ver Histórico
                        </button>
                    </div>
                    
                    <!-- Widget Keepa (carga dinámica) -->
                    <div id="keepaWidget" style="display: none;">
                        <h6 class="mt-4">📈 Histórico de Precios (Keepa)</h6>
                        <div id="keepaIframe" style="width: 100%; height: 350px; border: 1px solid #ddd; border-radius: 5px;"></div>
                        <p class="small text-muted mt-2">
                            📌 Datos actualizados por Keepa.com - Widget gratuito
                        </p>
                    </div>
                    
                    <!-- Alternativa: CamelCamelCamel -->
                    <div id="camelWidget" style="display: none;" class="mt-3">
                        <h6>🐫 Alternativa: CamelCamelCamel</h6>
                        <a id="camelLink" href="#" target="_blank" class="btn btn-outline-info w-100 mb-2">
                            Ver en CamelCamelCamel
                        </a>
                        <p class="small text-muted">
                            Extensión gratuita para alertas de precio:
                            <a href="https://camelcamelcamel.com/extension" target="_blank">Descargar</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function mostrarHistorico() {
    let input = document.getElementById('inputAsin').value.trim();
    
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
    
    // Validar formato ASIN (10 caracteres alfanuméricos)
    if (!/^[A-Z0-9]{10}$/.test(asin)) {
        alert('ASIN inválido. Debe tener 10 caracteres (ej: B08N5WRWNW)');
        return;
    }
    
    // Mostrar widget Keepa (GRATUITO - iframe embebido, sin límites)
    document.getElementById('keepaWidget').style.display = 'block';
    document.getElementById('keepaIframe').innerHTML = `
        <iframe 
            src="https://keepa.com/iframe_addon.html?domain=4&asin=${asin}" 
            width="100%" 
            height="350" 
            frameborder="0"
            style="border-radius: 5px;">
        </iframe>
    `;
    
    // Mostrar enlace CamelCamelCamel (alternativa gratuita)
    document.getElementById('camelWidget').style.display = 'block';
    document.getElementById('camelLink').href = 
        `https://es.camelcamelcamel.com/product/${asin}`;
    
    // Registrar estadística en backend (opcional)
    fetch('/api/comparador/estadistica', {
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
</script>
```

### 🛠️ Servicio Backend (Solo Enlaces, Sin API)

```java
@Service
public class ComparadorService {
    
    /**
     * Genera enlace de búsqueda en Amazon (sin API)
     */
    public String generarEnlaceAmazon(Producto producto) {
        String queryLimpia = producto.getTitulo()
            .replaceAll("[^a-zA-Z0-9\\s]", "")
            .replace(" ", "+");
        
        return String.format("https://www.amazon.es/s?k=%s", queryLimpia);
    }
    
    /**
     * Genera sugerencia de búsqueda inteligente
     */
    public String generarSugerencia(Producto producto) {
        return String.format(
            "Busca '%s %s' para encontrar productos similares",
            producto.getCategoria().getNombre(),
            producto.getTitulo()
        );
    }
}
```

### 📊 Widgets Embebidos: Keepa vs CamelCamelCamel

| Widget | Coste | Límites | Funcionalidad |
|--------|-------|---------|---------------|
| **Keepa iframe** | €0 | Ilimitado | Gráfica 90 días, precio actual, ofertas |
| **CamelCamelCamel** | €0 | Ilimitado | Gráfica histórica completa, alertas (extensión) |

**Ambos son completamente gratuitos al usar sus widgets embebidos** (sin usar sus APIs de pago).

---

## 3️⃣ OpenWeather API - Clima del Campus (GRATUITO: 1,000 req/día)

### 🎯 Casos de Uso

✅ Ver clima actual del campus antes de confirmar asistencia a quedada  
✅ Recibir sugerencias ("Lleva paraguas ☔" / "Perfecto para picnic 🌞")  
✅ Mostrar icono del clima en cada quedada  
✅ Alertas por clima extremo (cancelar quedada si lluvia fuerte)  

### 🔑 Obtener API Key (Gratuita)

1. Crear cuenta en [openweathermap.org](https://openweathermap.org/api)
2. Plan Free: **1,000 llamadas/día** (suficiente para 500+ usuarios)
3. API Key se genera automáticamente

### 🛠️ Configuración Backend

```java
@Service
@Slf4j
public class ClimaService {
    
    @Value("${openweather.api.key}")
    private String apiKey;
    
    @Autowired
    private RestTemplate restTemplate;
    
    private static final String OPENWEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final double UFV_LAT = 40.4426;
    private static final double UFV_LON = -3.8120;
    
    /**
     * Obtiene clima actual del campus UFV
     */
    @Cacheable(value = "clima", key = "'ufv'", unless = "#result == null")
    public ClimaDTO obtenerClimaCampus() {
        try {
            String url = String.format(
                "%s?lat=%s&lon=%s&appid=%s&units=metric&lang=es",
                OPENWEATHER_URL, UFV_LAT, UFV_LON, apiKey
            );
            
            OpenWeatherResponse response = restTemplate.getForObject(url, OpenWeatherResponse.class);
            
            if (response != null) {
                return ClimaDTO.builder()
                    .temperatura(Math.round(response.getMain().getTemp()))
                    .sensacion(Math.round(response.getMain().getFeelsLike()))
                    .descripcion(response.getWeather().get(0).getDescription())
                    .icono(response.getWeather().get(0).getIcon())
                    .humedad(response.getMain().getHumidity())
                    .viento(Math.round(response.getWind().getSpeed() * 3.6)) // m/s a km/h
                    .sugerencia(generarSugerencia(response))
                    .build();
            }
        } catch (Exception e) {
            log.error("Error obteniendo clima: {}", e.getMessage());
        }
        return null;
    }
    
    /**
     * Genera sugerencia basada en el clima
     */
    private String generarSugerencia(OpenWeatherResponse response) {
        int temp = (int) response.getMain().getTemp();
        String main = response.getWeather().get(0).getMain().toLowerCase();
        
        if (main.contains("rain")) {
            return "☔ Lleva paraguas, va a llover";
        } else if (temp > 28) {
            return "🥵 Hace calor, busca sombra o aire acondicionado";
        } else if (temp < 10) {
            return "🥶 Hace frío, abrígate bien";
        } else if (main.contains("clear") && temp >= 18 && temp <= 25) {
            return "🌞 ¡Clima perfecto para una quedada al aire libre!";
        } else {
            return "👍 Buen día para quedar en el campus";
        }
    }
    
    /**
     * Verifica si el clima es apto para quedada al aire libre
     */
    public boolean esAptoParaExterior(Quedada quedada) {
        ClimaDTO clima = obtenerClimaCampus();
        
        if (clima == null) return true; // Si falla API, permitir por defecto
        
        String desc = clima.getDescripcion().toLowerCase();
        
        // Condiciones desfavorables
        if (desc.contains("tormenta") || desc.contains("nieve") || 
            clima.getTemperatura() < 5 || clima.getTemperatura() > 35) {
            return false;
        }
        
        return true;
    }
}
```

### 🎨 Controlador REST

```java
@RestController
@RequestMapping("/api/clima")
public class ClimaController {
    
    @Autowired
    private ClimaService climaService;
    
    @GetMapping("/campus")
    public ResponseEntity<ClimaDTO> obtenerClimaCampus() {
        ClimaDTO clima = climaService.obtenerClimaCampus();
        return ResponseEntity.ok(clima);
    }
}
```

### 📊 Límites y Optimización

| Plan | Coste | Llamadas/Día | Optimización |
|------|-------|--------------|--------------|
| **Free** | €0 | 1,000/día | Cache Redis 10 minutos |
| **Startup** | $40/mes | 100,000/día | No necesario para MVP |

**Estrategia**: 1,000 llamadas/día = **41 llamadas/hora**. Con cache de 10 minutos, soportas fácilmente 500+ usuarios activos.

---

## 4️⃣ Telegram Bot API - Notificaciones Push Ilimitadas (GRATUITO)

### 🎯 Casos de Uso

✅ Notificaciones instantáneas cuando alguien se une a tu quedada  
✅ Recordatorios 24h y 1h antes de la quedada  
✅ Alertas de mensajes nuevos del chat de marketplace  
✅ Alternativa gratuita a servicios de email (sin límites)  

### 🔑 Crear Bot de Telegram (Gratuito)

1. Hablar con [@BotFather](https://t.me/BotFather) en Telegram
2. Comando: `/newbot`
3. Elegir nombre: `UFV Shares Bot`
4. Obtener **Token de API** (gratuito, ilimitado)

### 🛠️ Configuración Backend

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.telegram</groupId>
    <artifactId>telegrambots</artifactId>
    <version>6.8.0</version>
</dependency>
```

```java
@Service
@Slf4j
public class TelegramNotificationService {
    
    @Value("${telegram.bot.token}")
    private String botToken;
    
    @Value("${telegram.bot.username}")
    private String botUsername;
    
    private TelegramBotsApi botsApi;
    
    @PostConstruct
    public void init() throws TelegramApiException {
        botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new UFVSharesBot(botToken, botUsername));
    }
    
    /**
     * Envía notificación a un usuario por Telegram
     */
    public void enviarNotificacion(Usuario usuario, String mensaje) {
        if (usuario.getTelegramChatId() == null) {
            log.warn("Usuario {} no tiene Telegram configurado", usuario.getEmail());
            return;
        }
        
        try {
            SendMessage message = new SendMessage();
            message.setChatId(usuario.getTelegramChatId());
            message.setText(mensaje);
            message.setParseMode("HTML");
            
            // Botones inline opcionales
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
            
            InlineKeyboardButton verApp = new InlineKeyboardButton();
            verApp.setText("Ver en la app");
            verApp.setUrl("https://ufvshares.com");
            
            keyboard.add(List.of(verApp));
            markup.setKeyboard(keyboard);
            message.setReplyMarkup(markup);
            
            // Enviar (GRATUITO, ILIMITADO)
            bot.execute(message);
            
            log.info("Notificación Telegram enviada a usuario {}", usuario.getId());
            
        } catch (TelegramApiException e) {
            log.error("Error enviando notificación Telegram: {}", e.getMessage());
        }
    }
    
    /**
     * Notificar nueva asistencia a quedada
     */
    public void notificarNuevaAsistencia(Quedada quedada, Usuario nuevoAsistente) {
        Usuario creador = quedada.getCreador();
        
        String mensaje = String.format(
            "🎉 <b>Nueva asistencia a tu quedada</b>\n\n" +
            "👤 %s se unió a:\n" +
            "📅 %s\n" +
            "📍 %s\n\n" +
            "👥 Asistentes ahora: %d/%d",
            nuevoAsistente.getNombre(),
            quedada.getTitulo(),
            quedada.getUbicacion().getNombre(),
            quedada.getAsistencias().size(),
            quedada.getMaxParticipantes()
        );
        
        enviarNotificacion(creador, mensaje);
    }
    
    /**
     * Recordatorio 24h antes de quedada
     */
    @Scheduled(cron = "0 0 */6 * * *") // Cada 6 horas
    public void enviarRecordatorios() {
        LocalDateTime dentro24h = LocalDateTime.now().plusHours(24);
        LocalDateTime dentro25h = LocalDateTime.now().plusHours(25);
        
        List<Quedada> quedadasProximas = quedadaRepository
            .findByFechaHoraBetween(dentro24h, dentro25h);
        
        for (Quedada quedada : quedadasProximas) {
            quedada.getAsistencias().forEach(asistencia -> {
                if (asistencia.getEstado() == EstadoAsistencia.CONFIRMADA) {
                    String mensaje = String.format(
                        "⏰ <b>Recordatorio: Quedada mañana</b>\n\n" +
                        "📅 %s\n" +
                        "🕐 %s\n" +
                        "📍 %s\n\n" +
                        "¡No olvides asistir!",
                        quedada.getTitulo(),
                        quedada.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")),
                        quedada.getUbicacion().getNombre()
                    );
                    
                    enviarNotificacion(asistencia.getUsuario(), mensaje);
                }
            });
        }
    }
}
```

### 📱 Vinculación de Usuarios con Bot

```java
@RestController
@RequestMapping("/api/telegram")
public class TelegramController {
    
    @Autowired
    private UsuarioRepository usuarioRepo;
    
    /**
     * Usuario vincula su cuenta de Telegram
     */
    @PostMapping("/vincular")
    public ResponseEntity<?> vincularTelegram(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String codigo) {
        
        Usuario usuario = usuarioRepo.findByEmail(userDetails.getUsername())
            .orElseThrow();
        
        // Verificar código temporal (generado cuando usuario habla con bot)
        Long chatId = codigoVinculacionCache.get(codigo);
        
        if (chatId == null) {
            return ResponseEntity.badRequest()
                .body("Código inválido o expirado");
        }
        
        usuario.setTelegramChatId(chatId);
        usuarioRepo.save(usuario);
        
        return ResponseEntity.ok("Telegram vinculado correctamente");
    }
}
```

### 📊 Ventajas de Telegram vs Email

| Característica | Telegram Bot | Email (SendGrid Free) |
|----------------|--------------|----------------------|
| **Coste** | €0 permanente | €0 (100 emails/día) |
| **Límite diario** | Ilimitado | 100 emails |
| **Entrega instantánea** | ✅ Sí | ⏱️ Puede tardar |
| **Lectura garantizada** | ✅ Push notification | ❌ Puede ir a spam |
| **Interactividad** | ✅ Botones inline | ❌ Solo enlaces |

---

## 5️⃣ QR Code Generator API - Compartir Quedadas (GRATUITO, ILIMITADO)

### 🎯 Casos de Uso

✅ Generar código QR único para cada quedada  
✅ Escanear QR para unirse instantáneamente  
✅ Compartir quedada en redes sociales con QR  
✅ Imprimir pósters con QR para tablón de anuncios  

### 🛠️ Implementación (Sin Backend, Solo URL)

```java
@Service
public class QRCodeService {
    
    private static final String QR_API_URL = "https://api.qrserver.com/v1/create-qr-code/";
    
    /**
     * Genera URL de QR Code (API gratuita, sin límites)
     */
    public String generarQRQuedada(Quedada quedada) {
        String urlQuedada = "https://ufvshares.com/quedadas/" + quedada.getId();
        
        try {
            String urlEncoded = URLEncoder.encode(urlQuedada, StandardCharsets.UTF_8);
            
            return String.format(
                "%s?size=300x300&data=%s",
                QR_API_URL,
                urlEncoded
            );
        } catch (Exception e) {
            log.error("Error generando QR: {}", e.getMessage());
            return null;
        }
    }
}
```

### 🎨 Vista de Quedada con QR

```html
<div class="card">
    <div class="card-header">
        <h4 th:text="${quedada.titulo}"></h4>
    </div>
    <div class="card-body">
        <div class="row">
            <div class="col-md-8">
                <p><strong>📅 Fecha:</strong> <span th:text="${quedada.fechaHora}"></span></p>
                <p><strong>📍 Lugar:</strong> <span th:text="${quedada.ubicacion.nombre}"></span></p>
                <p><strong>👥 Asistentes:</strong> <span th:text="${quedada.asistentes}"></span>/<span th:text="${quedada.maxParticipantes}"></span></p>
                
                <button class="btn btn-primary" onclick="unirme()">Unirme a la quedada</button>
            </div>
            
            <div class="col-md-4 text-center">
                <h6>Compartir con QR:</h6>
                <img th:src="${quedada.urlQR}" alt="QR Code" class="img-fluid" style="max-width: 200px;">
                <p class="small text-muted mt-2">Escanea para unirte</p>
                
                <button class="btn btn-sm btn-outline-primary" onclick="descargarQR()">
                    📥 Descargar QR
                </button>
            </div>
        </div>
    </div>
</div>

<script>
function descargarQR() {
    const url = '[[${quedada.urlQR}]]';
    const a = document.createElement('a');
    a.href = url;
    a.download = 'quedada_qr.png';
    a.click();
}
</script>
```

---

## 6️⃣ BONUS: Otras APIs Gratuitas que Puedes Integrar

### 📚 Universidad/Academic APIs

```java
// API de horarios UFV (si existe API pública)
@Service
public class HorarioService {
    
    /**
     * Sugiere horarios libres para crear quedadas
     */
    public List<LocalDateTime> sugerirHorariosLibres(Usuario usuario) {
        // Consultar horario académico del usuario
        // Sugerir huecos entre clases
        return horariosLibres;
    }
}
```

### 🚌 Transport APIs (EMT Madrid - Gratuita)

```java
// API EMT Madrid (transporte público cerca del campus)
@Service
public class TransporteService {
    
    private static final String EMT_API = "https://openapi.emtmadrid.es/v2/";
    
    /**
     * Obtiene próximos buses cerca del campus UFV
     */
    public List<BusDTO> obtenerProximosBuses() {
        // Paradas cercanas a UFV
        // Mostrar tiempos de llegada
        return buses;
    }
}
```

### 📰 News API (Noticias del Campus - Scraping o RSS)

```java
// Scraping de noticias UFV (si no hay API)
@Service
public class NoticiasService {
    
    /**
     * Obtiene últimas noticias del campus
     */
    public List<NoticiaDTO> obtenerNoticiasUFV() {
        // Web scraping de ufv.es/noticias
        // O RSS feed si está disponible
        return noticias;
    }
}
```

---

## 🚀 Configuración de Despliegue (Variables de Entorno)

```properties
# application.properties

# ===== NO REQUIERE: OpenStreetMap + Leaflet =====
# (Sin configuración, 100% frontend)

# ===== OpenWeather API (GRATUITO: 1,000/día) =====
openweather.api.key=${OPENWEATHER_API_KEY}

# ===== Telegram Bot API (GRATUITO: ILIMITADO) =====
telegram.bot.token=${TELEGRAM_BOT_TOKEN}
telegram.bot.username=UFVSharesBot

# ===== Base de Datos PostgreSQL =====
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# ===== Cache Redis (opcional para clima) =====
spring.redis.host=${REDIS_HOST}
spring.redis.port=6379
spring.cache.type=redis
spring.cache.redis.time-to-live=600000

# ===== Email (backup para notificaciones) =====
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${EMAIL_USERNAME}
spring.mail.password=${EMAIL_PASSWORD}
```

---

## 💰 Análisis de Costes (100% GRATUITO)

| Servicio | Coste Mensual | Límites | Estado |
|----------|---------------|---------|--------|
| OpenStreetMap + Leaflet | €0 | Ilimitado | ✅ |
| Nominatim (OSM) | €0 | 1 req/seg | ✅ |
| Keepa Widget | €0 | Ilimitado | ✅ |
| OpenWeather API | €0 | 1,000/día | ✅ |
| Telegram Bot API | €0 | Ilimitado | ✅ |
| QR Code Generator | €0 | Ilimitado | ✅ |
| **TOTAL** | **€0** | - | ✅ |

---

## 📊 Comparación: Antes vs Ahora

| Aspecto | Versión Original | Versión Gratuita |
|---------|------------------|------------------|
| **Coste mensual** | ~€60 (Keepa + infra) | **€0** |
| **APIs de pago** | 3 (Google Maps, Amazon, Keepa) | **0** |
| **Límites restrictivos** | Sí (cuotas) | **No** |
| **Requiere tarjeta** | Sí | **No** |
| **Escalabilidad** | Limitada por coste | **Ilimitada** |
| **Funcionalidad** | Completa | **Completa (equivalente)** |

---

## ✅ Conclusiones

### Ventajas del Enfoque 100% Gratuito

✅ **Cero riesgo financiero**: Sin sorpresas de facturación  
✅ **Escalable sin límites**: Crece con 0 coste adicional  
✅ **Sostenible a largo plazo**: No depende de inversión continua  
✅ **Ideal para proyecto académico**: Sin necesidad de monetización inmediata  
✅ **Funcionalidad completa**: No sacrificas features por ser gratis  

### Alternativas Propuestas

🔄 **Si necesitas más funcionalidad**:
- **Mapas**: Mantén OpenStreetMap (es superior a Google Maps para tu caso)
- **Clima**: Upgrade a plan pago OpenWeather solo si >1,000 req/día (poco probable)
- **Notificaciones**: Telegram Bot es **mejor que email** (push instantáneo, gratis, ilimitado)
- **Precios Amazon**: Widget Keepa embebido es **suficiente** (sin API de pago)

---

**Documento generado para**: UFV Shares - Proyectos II (3º Año)  
**Profesor**: Roberto Rodríguez Galán  
**Fecha**: 7 de febrero de 2026  
**Versión**: 2.0 - APIs 100% Gratuitas
