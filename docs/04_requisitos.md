# 📝 Requisitos del Sistema - UFV Shares

## Tabla de Contenidos

1. [Requisitos Funcionales](#requisitos-funcionales)
2. [Requisitos No Funcionales](#requisitos-no-funcionales)
3. [Matriz de Trazabilidad](#matriz-de-trazabilidad)

---

## Requisitos Funcionales

### RF-01: Gestión de Usuarios

#### RF-01.1: Registro de Usuario
**Descripción**: El sistema debe permitir el registro de nuevos usuarios con email universitario (@ufv.es)

**Criterios de Aceptación**:
- El email debe tener dominio @ufv.es
- La contraseña debe tener mínimo 8 caracteres, incluir mayúsculas, minúsculas y números
- Los campos nombre, apellidos, carrera y curso son obligatorios
- Se envía email de verificación al registrarse
- El usuario debe confirmar su email antes de acceder al sistema

**Prioridad**: ALTA  
**Complejidad**: Media

---

#### RF-01.2: Autenticación de Usuario
**Descripción**: El sistema debe validar las credenciales de los usuarios en el inicio de sesión

**Criterios de Aceptación**:
- El login se realiza con email y contraseña
- Máximo 5 intentos fallidos antes de bloqueo temporal (15 minutos)
- Las contraseñas se almacenan con hash BCrypt
- Se genera token de sesión JWT válido por 24 horas
- Opción "Recordarme" extiende sesión a 30 días

**Prioridad**: ALTA  
**Complejidad**: Media

---

#### RF-01.3: Recuperación de Contraseña
**Descripción**: El sistema debe permitir a los usuarios recuperar su contraseña mediante email

**Criterios de Aceptación**:
- Se envía link de recuperación al email registrado
- El link expira después de 1 hora
- El usuario debe crear una nueva contraseña (no puede repetir las últimas 3)
- Se notifica al email cuando se cambia la contraseña

**Prioridad**: MEDIA  
**Complejidad**: Baja

---

#### RF-01.4: Edición de Perfil
**Descripción**: El usuario debe poder editar su información personal

**Criterios de Aceptación**:
- Se pueden modificar: nombre, apellidos, carrera, curso, foto de perfil
- El email no puede modificarse (es identificador único)
- Los cambios se guardan inmediatamente
- Se muestra confirmación visual de cambio guardado
- La foto de perfil se sube a AWS S3

**Prioridad**: MEDIA  
**Complejidad**: Baja

---

### RF-02: Gestión de Quedadas

#### RF-02.1: Crear Quedada
**Descripción**: Los usuarios deben poder crear quedadas con ubicación en mapa

**Criterios de Aceptación**:
- Campos obligatorios: título, fecha/hora, ubicación
- Fecha/hora debe ser futura
- Ubicación se selecciona en mapa interactivo
- Se puede definir como pública o privada
- Se puede establecer número máximo de participantes
- El creador aparece automáticamente como asistente

**Prioridad**: ALTA  
**Complejidad**: Alta

---

#### RF-02.2: Visualizar Quedadas en Mapa
**Descripción**: El sistema debe mostrar quedadas activas en mapa interactivo

**Criterios de Aceptación**:
- El mapa muestra todas las quedadas públicas activas
- Cada quedada se representa con un marcador
- Al hacer click en marcador se muestra popup con información básica
- Se pueden filtrar quedadas por fecha y tipo
- El mapa permite zoom y navegación
- Las quedadas pasadas no se muestran

**Prioridad**: ALTA  
**Complejidad**: Media

---

#### RF-02.3: Confirmar Asistencia
**Descripción**: Los usuarios deben poder confirmar asistencia a quedadas

**Criterios de Aceptación**:
- Se muestra botón "Confirmar Asistencia" si hay plazas disponibles
- Se valida que el usuario no esté ya confirmado
- Se valida que no se exceda el máximo de participantes
- Se actualiza contador de asistentes en tiempo real
- El usuario puede cancelar su asistencia hasta 1 hora antes
- Se notifica al creador cuando alguien se une

**Prioridad**: ALTA  
**Complejidad**: Media

---

#### RF-02.4: Enviar Notificaciones de Recordatorio
**Descripción**: El sistema debe enviar notificaciones automáticas de recordatorio

**Criterios de Aceptación**:
- Los asistentes confirmados reciben recordatorio 30 minutos antes
- La notificación se envía por email y notificación in-app
- La notificación incluye: título, ubicación, hora y link al mapa
- Solo se envía una vez por asistente
- No se envía si el usuario canceló su asistencia

**Prioridad**: MEDIA  
**Complejidad**: Media

---

#### RF-02.5: Cancelar/Modificar Quedada
**Descripción**: El creador debe poder cancelar o modificar sus quedadas

**Criterios de Aceptación**:
- Solo el creador puede modificar o cancelar
- Si se cancela, se notifica a todos los asistentes
- Se pueden modificar: título, descripción, fecha/hora, ubicación
- No se puede cambiar de pública a privada si ya hay asistentes
- Las modificaciones notifican a los asistentes

**Prioridad**: MEDIA  
**Complejidad**: Media

---

### RF-03: Comparador de Precios Amazon

#### RF-03.1: Buscar Productos en Amazon
**Descripción**: El sistema debe buscar productos en Amazon por nombre o ASIN

**Criterios de Aceptación**:
- Se acepta búsqueda por texto libre o URL de Amazon
- Se extraen ASINs de URLs automáticamente
- Se muestran hasta 10 resultados por búsqueda
- Cada resultado muestra: imagen, título, precio, rating
- Los resultados se cachean por 1 hora
- Se muestra indicador de carga durante la búsqueda

**Prioridad**: ALTA  
**Complejidad**: Alta

---

#### RF-03.2: Obtener Precio Actual
**Descripción**: El sistema debe obtener el precio actual de productos de Amazon

**Criterios de Aceptación**:
- Se consulta Amazon Product Advertising API
- Los precios se muestran en euros (€)
- Se indica fecha/hora de última actualización
- Los precios se actualizan cada 6 horas automáticamente
- Se muestra mensaje si el producto no está disponible

**Prioridad**: ALTA  
**Complejidad**: Media

---

#### RF-03.3: Mostrar Gráfica Histórica de Precios
**Descripción**: El sistema debe mostrar gráfica de evolución de precios usando Keepa

**Criterios de Aceptación**:
- Se muestra gráfica interactiva con Chart.js
- Por defecto se muestra últimos 90 días
- Se ofrecen opciones: 30, 90, 365 días
- Se marca el precio actual con línea vertical
- Se indica precio mínimo y máximo del periodo
- Se calcula y muestra precio promedio
- Al pasar mouse sobre puntos se ve precio exacto y fecha

**Prioridad**: ALTA  
**Complejidad**: Alta

---

#### RF-03.4: Configurar Alertas de Precio
**Descripción**: Los usuarios deben poder configurar alertas cuando un precio alcanza objetivo

**Criterios de Aceptación**:
- Se sugiere precio objetivo basado en histórico (10% menos que actual)
- El usuario puede personalizar el precio objetivo
- Se valida que precio objetivo < precio actual
- Máximo 10 alertas activas por usuario
- Se muestra lista de alertas activas en dashboard
- Se pueden desactivar alertas manualmente

**Prioridad**: MEDIA  
**Complejidad**: Media

---

#### RF-03.5: Notificar cuando se Alcanza Precio Objetivo
**Descripción**: El sistema debe notificar automáticamente cuando un precio cumple la condición

**Criterios de Aceptación**:
- Las alertas se verifican cada 6 horas (cron job)
- Se envía email cuando se cumple la condición
- Se envía notificación in-app
- La notificación incluye: producto, precio objetivo, precio actual, link
- La alerta se desactiva automáticamente al dispararse
- Las alertas expiran después de 90 días sin cumplirse

**Prioridad**: MEDIA  
**Complejidad**: Alta

---

### RF-04: Marketplace Estudiantil

#### RF-04.1: Publicar Productos
**Descripción**: Los usuarios deben poder publicar productos en venta

**Criterios de Aceptación**:
- Campos obligatorios: título, descripción, precio, categoría
- Se pueden subir hasta 5 fotos (JPG/PNG, max 5MB cada una)
- Se selecciona estado del producto: nuevo, como nuevo, bueno, aceptable
- Opcionalmente se puede añadir ubicación de entrega
- Las publicaciones tienen estado: disponible, reservado, vendido
- Un usuario puede tener máximo 20 publicaciones activas

**Prioridad**: ALTA  
**Complejidad**: Media

---

#### RF-04.2: Buscar y Filtrar Productos
**Descripción**: Los usuarios deben poder buscar productos con filtros

**Criterios de Aceptación**:
- Búsqueda por texto en título y descripción
- Filtros: categoría, rango de precio, estado del producto
- Ordenación: más recientes, precio ascendente/descendente
- Se muestran 20 resultados por página con paginación
- Se muestra contador de resultados encontrados
- Se puede guardar búsqueda favorita

**Prioridad**: MEDIA  
**Complejidad**: Media

---

#### RF-04.3: Chat entre Comprador y Vendedor
**Descripción**: El sistema debe permitir chat entre usuarios para coordinar compra

**Criterios de Aceptación**:
- Se abre chat al presionar "Contactar Vendedor"
- Los mensajes se guardan en base de datos
- Se muestra historial completo de la conversación
- Se notifica al destinatario de nuevos mensajes
- Los mensajes se marcan como leídos automáticamente
- Máximo 2000 caracteres por mensaje

**Prioridad**: ALTA  
**Complejidad**: Alta

---

#### RF-04.4: Marcar Productos como Vendidos
**Descripción**: Los usuarios deben poder marcar productos como vendidos

**Criterios de Aceptación**:
- Solo el vendedor puede marcar como vendido
- Se solicita confirmación antes de marcar
- El producto desaparece del marketplace activo
- Se archiva la conversación asociada
- Se solicita valoración mutua (opcional)
- El producto queda en historial del vendedor

**Prioridad**: MEDIA  
**Complejidad**: Baja

---

### RF-05: Sistema de Ubicación

#### RF-05.1: Mostrar Mapa Interactivo del Campus
**Descripción**: El sistema debe mostrar mapa interactivo con ubicaciones del campus

**Criterios de Aceptación**:
- El mapa carga centrado en campus UFV
- Se usan Leaflet.js y OpenStreetMap/Google Maps
- Se muestran marcadores para ubicaciones predefinidas
- Los marcadores se categorizan por tipo (aula, biblioteca, cafetería, etc.)
- Se puede hacer zoom in/out
- Se puede navegar arrastrando el mapa

**Prioridad**: ALTA  
**Complejidad**: Media

---

#### RF-05.2: Buscar Ubicaciones
**Descripción**: El sistema debe permitir búsqueda de ubicaciones por nombre

**Criterios de Aceptación**:
- Se muestra barra de búsqueda prominente
- Se ofrecen sugerencias autocompletadas mientras se escribe
- La búsqueda es case-insensitive
- Se priorizan ubicaciones dentro del campus
- Al seleccionar resultado, el mapa centra en la ubicación
- Se muestra información de la ubicación (nombre, tipo, descripción)

**Prioridad**: MEDIA  
**Complejidad**: Media

---

#### RF-05.3: Calcular Ruta entre Puntos
**Descripción**: El sistema debe mostrar ruta óptima a pie entre dos puntos

**Criterios de Aceptación**:
- Se solicita permiso de geolocalización del navegador
- Si se permite, se usa ubicación actual como origen
- Si no, se usa entrada principal del campus
- Se calcula ruta a pie (no en coche)
- Se muestra línea de ruta en el mapa
- Se indica distancia en metros y tiempo estimado en minutos
- Velocidad promedio: 5 km/h

**Prioridad**: MEDIA  
**Complejidad**: Alta

---

#### RF-05.4: Mostrar Quedadas en Mapa
**Descripción**: El mapa debe mostrar quedadas activas como marcadores

**Criterios de Aceptación**:
- Las quedadas se muestran con marcador diferenciado (color/icono)
- Al hacer click se muestra popup con: título, hora, asistentes
- Solo se muestran quedadas futuras y públicas
- El popup tiene link "Ver detalles" que lleva a página completa
- Se actualiza en tiempo real cuando se crean nuevas quedadas
- Se pueden ocultar/mostrar quedadas con toggle

**Prioridad**: MEDIA  
**Complejidad**: Media

---

## Requisitos No Funcionales

### RNF-01: Rendimiento

#### RNF-01.1: Tiempo de Carga de Mapa
**Descripción**: El mapa interactivo debe cargar en menos de 2 segundos

**Métrica**: 
- First Contentful Paint (FCP) < 1.5s
- Time to Interactive (TTI) < 2s

**Justificación**: Experiencia de usuario fluida en funcionalidad core

---

#### RNF-01.2: Respuesta de Búsqueda Amazon
**Descripción**: La búsqueda de productos Amazon debe responder en menos de 3 segundos

**Métrica**:
- Tiempo de respuesta p95 < 3s
- Tiempo de respuesta promedio < 2s

**Justificación**: Mantener atención del usuario durante búsqueda

---

#### RNF-01.3: Usuarios Concurrentes
**Descripción**: La aplicación debe soportar 500 usuarios concurrentes

**Métrica**:
- 500 usuarios simultáneos sin degradación
- Tiempo de respuesta < 500ms para operaciones CRUD básicas

**Justificación**: Picos de uso durante horario de clases

---

#### RNF-01.4: Base de Datos
**Descripción**: Las consultas a base de datos deben ser eficientes

**Métrica**:
- Consultas simples < 50ms
- Consultas complejas (joins) < 200ms
- Uso de índices en todas las foreign keys

---

### RNF-02: Seguridad

#### RNF-02.1: Almacenamiento de Contraseñas
**Descripción**: Las contraseñas deben almacenarse con hash BCrypt

**Implementación**:
- BCrypt con cost factor de 12
- Salt único por usuario
- Nunca se almacena contraseña en texto plano
- Nunca se envía contraseña en logs

---

#### RNF-02.2: Expiración de Sesiones
**Descripción**: Las sesiones deben expirar tras inactividad

**Configuración**:
- Sesión normal: 24 horas
- "Recordarme": 30 días
- Logout automático tras 24h de inactividad
- Refresh token para renovar sesión

---

#### RNF-02.3: Protección de API Keys
**Descripción**: Las claves de APIs externas deben estar protegidas

**Implementación**:
- API keys en variables de entorno (no en código)
- Uso de Azure Key Vault en producción
- Rotación de keys cada 90 días
- Logs no deben exponer keys

---

#### RNF-02.4: Protección contra Ataques
**Descripción**: El sistema debe protegerse contra ataques comunes

**Medidas**:
- Protección XSS: sanitización de inputs
- Protección CSRF: tokens en formularios
- Protección SQL Injection: prepared statements
- Rate limiting: 100 requests/minuto por IP
- HTTPS obligatorio en producción

---

### RNF-03: Usabilidad

#### RNF-03.1: Diseño Responsive
**Descripción**: La interfaz debe ser responsive para múltiples dispositivos

**Soporte**:
- Móvil: 320px - 768px
- Tablet: 768px - 1024px
- Desktop: > 1024px
- Breakpoints de Bootstrap 5
- Touch-friendly en móviles

---

#### RNF-03.2: Compatibilidad de Navegadores
**Descripción**: Soporte para navegadores modernos

**Navegadores Soportados**:
- Chrome >= 90
- Firefox >= 88
- Edge >= 90
- Safari >= 14
- NO soporta Internet Explorer

---

#### RNF-03.3: Mensajes de Error
**Descripción**: Los mensajes de error deben ser claros y en español

**Características**:
- Texto en español de España
- Mensajes específicos (no genéricos)
- Sugerencias de solución cuando sea posible
- No exponer detalles técnicos al usuario
- Iconos visuales para reforzar mensaje

---

#### RNF-03.4: Accesibilidad
**Descripción**: La aplicación debe ser accesible

**Cumplimiento**:
- Etiquetas ARIA en elementos interactivos
- Navegación por teclado funcional
- Contraste de colores WCAG AA
- Textos alternativos en imágenes
- Tamaño de fuente ajustable

---

### RNF-04: Disponibilidad

#### RNF-04.1: Uptime del Sistema
**Descripción**: El sistema debe tener alta disponibilidad

**Métrica**:
- Uptime objetivo: 99% (< 7.2 horas downtime/mes)
- Ventanas de mantenimiento: domingos 2am-4am
- Notificación de mantenimientos con 48h anticipación

---

#### RNF-04.2: Degradación Controlada
**Descripción**: Las caídas de APIs externas no deben bloquear funcionalidad principal

**Estrategia**:
- Si Keepa API falla: mostrar solo precio actual
- Si Amazon API falla: mostrar datos cacheados
- Si Google Maps falla: fallback a OpenStreetMap
- Mensajes informativos al usuario sobre limitaciones temporales

---

#### RNF-04.3: Monitorización
**Descripción**: El sistema debe estar monitorizado

**Herramientas**:
- Azure Application Insights para logs y métricas
- Health check endpoint: `/api/health`
- Alertas automáticas si disponibilidad < 95%
- Dashboard de métricas en tiempo real

---

### RNF-05: Escalabilidad

#### RNF-05.1: Crecimiento de Usuarios
**Descripción**: La base de datos debe soportar crecimiento a 10,000 usuarios

**Diseño**:
- Índices optimizados para consultas frecuentes
- Particionamiento de tablas grandes (HISTORICO_PRECIO)
- Archivado de datos antiguos (> 2 años)
- Estimación de 150MB de datos en 5 años

---

#### RNF-05.2: Optimización de Cache
**Descripción**: El sistema de cache debe reducir llamadas a APIs externas

**Objetivo**:
- Reducción de 70% en llamadas a APIs
- Cache de búsquedas: 1 hora
- Cache de precios: 6 horas
- Cache de ubicaciones: inmutable
- Uso de Redis para cache distribuido

---

#### RNF-05.3: Escalado Horizontal
**Descripción**: La aplicación debe poder escalar horizontalmente

**Arquitectura**:
- Stateless: sesiones en Redis (no en memoria)
- Azure App Service con autoscaling
- Regla: escalar a +1 instancia si CPU > 70%
- Máximo 3 instancias simultáneas

---

### RNF-06: Mantenibilidad

#### RNF-06.1: Cobertura de Tests
**Descripción**: El código debe tener alta cobertura de tests

**Objetivos**:
- Cobertura global: ≥ 75%
- Cobertura de servicios: ≥ 85%
- Cobertura de controladores: ≥ 70%
- Tests unitarios + tests de integración
- CI/CD no despliega si coverage < 75%

---

#### RNF-06.2: Calidad de Código
**Descripción**: El código debe seguir buenas prácticas

**Métricas**:
- Complejidad ciclomática: ≤ 10 por método
- Duplicación de código: < 5%
- Deuda técnica: < 5 días
- SonarQube quality gate: PASS

---

#### RNF-06.3: Documentación
**Descripción**: El código y APIs deben estar documentados

**Requisitos**:
- Javadoc en todas las clases públicas
- README completo con setup
- Swagger/OpenAPI para endpoints REST
- Diagramas de arquitectura actualizados

---

## Matriz de Trazabilidad

### Requisitos Funcionales vs Casos de Uso

| Requisito | UC-01 | UC-02 | UC-03 | UC-04 | UC-05 | UC-06 | UC-07 | UC-08 |
|-----------|-------|-------|-------|-------|-------|-------|-------|-------|
| RF-01.1 | X | X | X | X | X | X | X | X |
| RF-01.2 | X | X | X | X | X | X | X | X |
| RF-02.1 | X | | | | X | | | |
| RF-02.2 | | | X | | X | | | |
| RF-02.3 | | | X | | | | | |
| RF-03.1 | | X | | | | | | |
| RF-03.3 | | | | | | | | X |
| RF-03.4 | | | | | | X | | |
| RF-04.1 | | | | X | | | | |
| RF-04.3 | | | | | | | X | |
| RF-05.1 | X | | | | X | | | |
| RF-05.2 | | | | | X | | | |
| RF-05.3 | | | | | X | | | |

### Requisitos No Funcionales vs Módulos

| RNF | Autenticación | Quedadas | Comparador | Marketplace | Mapa |
|-----|---------------|----------|------------|-------------|------|
| RNF-01.1 | | | | | X |
| RNF-01.2 | | | X | | |
| RNF-02.1 | X | | | | |
| RNF-02.2 | X | | | | |
| RNF-03.1 | X | X | X | X | X |
| RNF-04.2 | | | X | | X |
| RNF-05.2 | | | X | | X |

---

## Priorización de Requisitos (MoSCoW)

### Must Have (Debe tener) - MVP
- RF-01.1, RF-01.2: Registro y login
- RF-02.1, RF-02.2, RF-02.3: Crear y unirse a quedadas
- RF-03.1, RF-03.2: Buscar productos y precios Amazon
- RF-05.1, RF-05.2: Mapa con búsqueda básica
- RNF-02.1, RNF-02.2, RNF-02.3: Seguridad básica
- RNF-06.1: Cobertura de tests

### Should Have (Debería tener) - Release 1.0
- RF-02.4: Notificaciones de recordatorio
- RF-03.3: Gráficas de precios
- RF-03.4, RF-03.5: Sistema de alertas
- RF-04.1, RF-04.2, RF-04.3: Marketplace completo
- RF-05.3: Cálculo de rutas
- RNF-01: Todos los requisitos de rendimiento

### Could Have (Podría tener) - Futuro
- RF-01.3: Recuperación de contraseña
- RF-02.5: Edición de quedadas
- RF-04.4: Valoraciones
- RNF-03.4: Accesibilidad avanzada

### Won't Have (No tendrá) - Fuera de alcance
- App móvil nativa
- Pagos integrados
- Chat en tiempo real con WebSockets
- Gamificación y badges

---

> Esta especificación de requisitos representa el alcance completo del proyecto UFV Shares para cumplir con los objetivos académicos y funcionales establecidos.
