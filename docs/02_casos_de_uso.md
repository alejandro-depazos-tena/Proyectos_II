# 📋 Casos de Uso - UFV Shares

## Índice de Casos de Uso

1. [UC-01: Organizar Quedada en el Campus](#uc-01-organizar-quedada-en-el-campus)
2. [UC-02: Comparar Precios de Producto](#uc-02-comparar-precios-de-producto)
3. [UC-03: Unirse a Quedada Existente](#uc-03-unirse-a-quedada-existente)
4. [UC-04: Publicar Producto en Marketplace](#uc-04-publicar-producto-en-marketplace)
5. [UC-05: Buscar Ubicación en Mapa](#uc-05-buscar-ubicación-en-mapa)
6. [UC-06: Configurar Alerta de Precio](#uc-06-configurar-alerta-de-precio)
7. [UC-07: Comprar Producto del Marketplace](#uc-07-comprar-producto-del-marketplace)
8. [UC-08: Ver Histórico de Precios](#uc-08-ver-histórico-de-precios)

---

## UC-01: Organizar Quedada en el Campus

### Información General
| Campo | Valor |
|-------|-------|
| **ID** | UC-01 |
| **Nombre** | Organizar Quedada en el Campus |
| **Actor Principal** | Estudiante |
| **Actores Secundarios** | Sistema de Notificaciones, Google Maps API |
| **Tipo** | Primario, Esencial |
| **Prioridad** | Alta |

### Descripción
El estudiante crea un evento para encontrarse con otros estudiantes en una ubicación específica del campus, seleccionando el lugar en un mapa interactivo.

### Precondiciones
- El usuario debe estar autenticado en el sistema
- El usuario debe tener conexión a Internet
- El mapa debe estar disponible

### Flujo Principal
1. El estudiante accede a la sección "Quedadas"
2. El sistema muestra las quedadas existentes en lista y mapa
3. El estudiante selecciona "Crear Nueva Quedada"
4. El sistema muestra formulario de creación con mapa interactivo
5. El estudiante introduce:
   - Título de la quedada
   - Descripción (opcional)
   - Fecha y hora
   - Selecciona ubicación haciendo click en el mapa
6. El estudiante define configuración:
   - Visibilidad (pública/privada)
   - Número máximo de participantes (opcional)
7. El estudiante confirma la creación
8. El sistema valida los datos ingresados
9. El sistema crea la quedada en la base de datos
10. El sistema envía notificaciones a usuarios cercanos (si es pública)
11. El sistema redirige al detalle de la quedada creada

### Flujos Alternativos

#### 3a. Usuario cancela la creación
3. El estudiante selecciona "Cancelar"
4. El sistema descarta los datos y vuelve a la lista de quedadas

#### 8a. Datos inválidos
8. El sistema detecta datos inválidos (ej: fecha pasada, ubicación fuera del campus)
9. El sistema muestra mensaje de error específico
10. El sistema mantiene los datos ingresados para corrección
11. Vuelve al paso 5

#### 8b. Ubicación no seleccionada
8. El sistema detecta que no se seleccionó ubicación en el mapa
9. El sistema sugiere ubicaciones predefinidas (biblioteca, cafetería, etc.)
10. El estudiante selecciona una ubicación sugerida o marca en el mapa
11. Continúa en paso 7

### Postcondiciones
- **Éxito**: 
  - La quedada queda visible en el mapa para otros usuarios (si es pública)
  - El creador aparece automáticamente como asistente
  - Los usuarios cercanos reciben notificación
- **Fracaso**: 
  - No se crea ninguna quedada
  - Los datos no se persisten

### Reglas de Negocio
- RN-01: Las quedadas solo pueden crearse para fechas futuras
- RN-02: Las ubicaciones deben estar dentro del perímetro del campus UFV
- RN-03: Las quedadas públicas son visibles para todos los estudiantes
- RN-04: Las quedadas privadas requieren invitación del creador
- RN-05: El número máximo de participantes debe ser >= 2

---

## UC-02: Comparar Precios de Producto

### Información General
| Campo | Valor |
|-------|-------|
| **ID** | UC-02 |
| **Nombre** | Comparar Precios de Producto |
| **Actor Principal** | Estudiante |
| **Actores Secundarios** | Amazon API, Keepa API |
| **Tipo** | Primario, Esencial |
| **Prioridad** | Alta |

### Descripción
El estudiante busca un producto en Amazon y visualiza su precio actual junto con el histórico de precios para tomar decisión informada de compra.

### Precondiciones
- El usuario debe estar autenticado
- Las APIs externas (Amazon, Keepa) deben estar operativas
- El producto debe existir en Amazon

### Flujo Principal
1. El estudiante accede a la sección "Comparador de Precios"
2. El sistema muestra barra de búsqueda
3. El estudiante ingresa nombre del producto o URL de Amazon
4. El estudiante presiona "Buscar"
5. El sistema consulta la API de Amazon para obtener productos coincidentes
6. El sistema muestra lista de resultados con:
   - Imagen del producto
   - Título
   - Precio actual
   - Rating
7. El estudiante selecciona un producto de interés
8. El sistema consulta Keepa API para obtener histórico de precios
9. El sistema muestra página de detalle con:
   - Información completa del producto
   - Precio actual destacado
   - Gráfica de evolución de precios (últimos 90 días por defecto)
   - Precio mínimo histórico
   - Indicador de si es buen momento de compra
10. El estudiante puede cambiar periodo de visualización (30/90/365 días)
11. El sistema actualiza la gráfica según periodo seleccionado

### Flujos Alternativos

#### 3a. Búsqueda por URL de Amazon
3. El estudiante pega una URL de Amazon
4. El sistema extrae el ASIN de la URL
5. Continúa en paso 5

#### 5a. No hay resultados
5. La API no devuelve resultados
6. El sistema muestra mensaje "No se encontraron productos"
7. El sistema sugiere refinar la búsqueda
8. Vuelve al paso 2

#### 8a. Keepa API no disponible
8. El sistema detecta que Keepa API no responde
9. El sistema muestra solo información básica del producto
10. El sistema muestra mensaje "Histórico temporalmente no disponible"
11. El estudiante puede configurar alerta para cuando se recupere el servicio

#### 8b. Producto sin histórico
8. El producto no tiene datos históricos en Keepa
9. El sistema muestra mensaje informativo
10. El sistema registra el precio actual como primer punto de histórico
11. Continúa en paso 9 con gráfica limitada

### Postcondiciones
- **Éxito**: 
  - El estudiante visualiza precio actual y histórico
  - Los datos se cachean para futuras consultas
  - El producto se registra en BD si no existía
- **Fracaso**: 
  - Se muestra mensaje de error apropiado
  - Se sugieren alternativas o retry

### Reglas de Negocio
- RN-06: Los precios se actualizan cada 6 horas máximo
- RN-07: Se considera "oferta" si el precio está 15% bajo el promedio de 30 días
- RN-08: El histórico por defecto es de 90 días
- RN-09: Los resultados de búsqueda se cachean por 1 hora

---

## UC-03: Unirse a Quedada Existente

### Información General
| Campo | Valor |
|-------|-------|
| **ID** | UC-03 |
| **Nombre** | Unirse a Quedada Existente |
| **Actor Principal** | Estudiante |
| **Actores Secundarios** | Sistema de Notificaciones |
| **Tipo** | Primario, Esencial |
| **Prioridad** | Alta |

### Descripción
El estudiante confirma su asistencia a una quedada organizada por otro usuario.

### Precondiciones
- El usuario debe estar autenticado
- Debe existir al menos una quedada disponible
- La quedada debe ser pública o el usuario debe estar invitado

### Flujo Principal
1. El estudiante navega por el mapa o lista de quedadas
2. El sistema muestra quedadas activas con indicadores visuales
3. El estudiante selecciona una quedada de interés
4. El sistema muestra página de detalle con:
   - Título y descripción
   - Ubicación en mapa
   - Fecha y hora
   - Lista de asistentes confirmados
   - Creador de la quedada
   - Número de plazas disponibles
5. El estudiante presiona botón "Confirmar Asistencia"
6. El sistema valida que hay plazas disponibles
7. El sistema registra la asistencia en estado "CONFIRMADA"
8. El sistema actualiza contador de asistentes
9. El sistema notifica al creador de la quedada
10. El sistema programa recordatorio automático 30 minutos antes
11. El sistema muestra confirmación de registro

### Flujos Alternativos

#### 6a. No hay plazas disponibles
6. El sistema detecta que se alcanzó el máximo de participantes
7. El sistema muestra mensaje "Quedada completa"
8. El sistema ofrece opción "Lista de espera"
9. Si el estudiante acepta, se registra en lista de espera
10. El caso de uso termina

#### 6b. Usuario ya está registrado
6. El sistema detecta que el usuario ya confirmó asistencia
7. El sistema muestra botón "Cancelar Asistencia" en lugar de confirmar
8. Si el estudiante cancela:
   - El sistema elimina el registro de asistencia
   - Notifica al creador
   - Libera plaza para lista de espera
9. El caso de uso termina

#### 9a. Quedada privada sin invitación
3. El estudiante intenta acceder a quedada privada
4. El sistema verifica que el usuario no está invitado
5. El sistema muestra mensaje "Quedada privada - Solo con invitación"
6. El sistema ofrece "Solicitar invitación" al creador
7. El caso de uso termina

### Postcondiciones
- **Éxito**: 
  - El estudiante aparece en lista de asistentes
  - Se programa recordatorio automático
  - Creador es notificado
- **Fracaso**: 
  - No se registra asistencia
  - Estado de la quedada permanece sin cambios

### Reglas de Negocio
- RN-10: Un usuario puede confirmar asistencia a múltiples quedadas
- RN-11: Los recordatorios se envían 30 minutos antes del evento
- RN-12: Las asistencias pueden cancelarse hasta 1 hora antes del evento
- RN-13: Si una quedada se cancela, todos los asistentes son notificados

---

## UC-04: Publicar Producto en Marketplace

### Información General
| Campo | Valor |
|-------|-------|
| **ID** | UC-04 |
| **Nombre** | Publicar Producto en Marketplace |
| **Actor Principal** | Estudiante (Vendedor) |
| **Actores Secundarios** | Sistema de Notificaciones, AWS S3 |
| **Tipo** | Primario, Importante |
| **Prioridad** | Media |

### Descripción
El estudiante crea una publicación para vender un producto usado a otros estudiantes.

### Precondiciones
- El usuario debe estar autenticado
- El usuario debe tener perfil completo
- Sistema de almacenamiento de imágenes debe estar operativo

### Flujo Principal
1. El estudiante accede a "Marketplace"
2. El sistema muestra productos publicados por categoría
3. El estudiante selecciona "Vender Producto"
4. El sistema muestra formulario de publicación
5. El estudiante completa:
   - Título del producto (obligatorio)
   - Descripción detallada (obligatorio)
   - Precio en € (obligatorio)
   - Categoría (obligatorio): Libros, Tecnología, Muebles, Ropa, Otros
   - Estado del producto: Nuevo, Como nuevo, Bueno, Aceptable
   - Ubicación para entrega (opcional - puede seleccionar en mapa)
6. El estudiante sube fotos del producto (mínimo 1, máximo 5)
7. El sistema valida formato y tamaño de las imágenes
8. El sistema sube las imágenes a AWS S3
9. El estudiante confirma la publicación
10. El sistema valida todos los campos obligatorios
11. El sistema crea la publicación con estado "DISPONIBLE"
12. El sistema notifica a usuarios interesados en la categoría
13. El sistema redirige a la página del producto publicado

### Flujos Alternativos

#### 6a. Sin fotos
6. El estudiante no sube ninguna foto
7. El sistema muestra advertencia "Se recomienda añadir fotos"
8. El sistema permite continuar pero marca publicación como "sin imágenes"
9. Continúa en paso 9

#### 7a. Imagen inválida
7. El sistema detecta imagen con formato o tamaño no válido
8. El sistema muestra error específico (ej: "Máximo 5MB por imagen")
9. El sistema elimina la imagen problemática
10. El estudiante puede subir imagen alternativa
11. Vuelve al paso 6

#### 10a. Datos incompletos
10. El sistema detecta campos obligatorios vacíos
11. El sistema resalta campos faltantes en rojo
12. El sistema mantiene los datos ya ingresados
13. Vuelve al paso 5

### Postcondiciones
- **Éxito**: 
  - El producto aparece en el marketplace
  - Usuarios de la categoría son notificados
  - El vendedor puede gestionar su publicación
- **Fracaso**: 
  - No se crea publicación
  - Las imágenes no se suben

### Reglas de Negocio
- RN-14: El precio debe ser mayor a 0€
- RN-15: Las publicaciones caducan automáticamente después de 90 días
- RN-16: Un usuario puede tener máximo 20 publicaciones activas
- RN-17: Las imágenes deben ser JPG o PNG, máximo 5MB cada una
- RN-18: El título debe tener mínimo 10 caracteres

---

## UC-05: Buscar Ubicación en Mapa

### Información General
| Campo | Valor |
|-------|-------|
| **ID** | UC-05 |
| **Nombre** | Buscar Ubicación en Mapa |
| **Actor Principal** | Estudiante |
| **Actores Secundarios** | Google Maps API / OpenStreetMap |
| **Tipo** | Primario, Importante |
| **Prioridad** | Media |

### Descripción
El estudiante busca y localiza una ubicación específica en el campus universitario.

### Precondiciones
- El usuario debe estar autenticado
- El mapa debe estar cargado correctamente
- Debe haber conexión a Internet

### Flujo Principal
1. El estudiante accede al mapa interactivo desde menú principal
2. El sistema muestra mapa centrado en campus UFV con zoom apropiado
3. El sistema muestra barra de búsqueda en la parte superior
4. El estudiante escribe nombre de ubicación (ej: "Biblioteca", "Cafetería", "Aula 301")
5. El sistema muestra sugerencias autocompletadas mientras escribe
6. El estudiante selecciona una sugerencia o presiona Enter
7. El sistema consulta la base de datos de ubicaciones
8. El sistema centra el mapa en la ubicación encontrada
9. El sistema coloca marcador destacado en la ubicación
10. El sistema muestra card informativa con:
    - Nombre completo de la ubicación
    - Tipo (aula, edificio, cafetería, etc.)
    - Descripción breve
    - Horarios (si aplica)
11. El sistema ofrece botón "Cómo llegar desde mi ubicación"
12. Si el estudiante presiona el botón:
    - El sistema solicita permiso de geolocalización
    - Calcula ruta óptima a pie
    - Muestra línea de ruta en el mapa
    - Muestra tiempo estimado y distancia

### Flujos Alternativos

#### 5a. Sin sugerencias
5. No hay sugerencias que coincidan
6. El sistema muestra "No se encontraron ubicaciones"
7. El estudiante puede:
   - Refinar la búsqueda
   - Navegar manualmente en el mapa
8. El caso de uso termina

#### 7a. Ubicación no encontrada
7. No existe la ubicación en la base de datos
8. El sistema intenta búsqueda en Google Maps API
9. Si Google Maps encuentra resultado:
   - Muestra ubicación en mapa
   - Marca como "ubicación externa al sistema"
10. Si no encuentra nada:
    - Muestra mensaje de error
    - Sugiere ubicaciones similares
11. El caso de uso termina

#### 12a. Usuario deniega geolocalización
12. El sistema detecta que no hay permiso de ubicación
13. El sistema muestra mensaje "Activa ubicación para calcular rutas"
14. El sistema muestra ruta desde entrada principal del campus
15. Continúa mostrando el mapa

### Postcondiciones
- **Éxito**: 
  - La ubicación se muestra en el mapa
  - El usuario sabe cómo llegar (si solicitó ruta)
  - Se registra búsqueda para mejorar sugerencias futuras
- **Fracaso**: 
  - Se informa error de manera clara
  - El mapa permanece navegable

### Reglas de Negocio
- RN-19: Las búsquedas deben ser case-insensitive
- RN-20: Las sugerencias se ordenan por relevancia y popularidad
- RN-21: Las rutas son solo a pie (velocidad promedio: 5 km/h)
- RN-22: Se priorizan ubicaciones dentro del campus sobre externas

---

## UC-06: Configurar Alerta de Precio

### Información General
| Campo | Valor |
|-------|-------|
| **ID** | UC-06 |
| **Nombre** | Configurar Alerta de Precio |
| **Actor Principal** | Estudiante |
| **Actores Secundarios** | Sistema de Alertas, Keepa API |
| **Tipo** | Secundario, Importante |
| **Prioridad** | Media |

### Descripción
El estudiante configura una alerta para ser notificado cuando un producto de Amazon alcance un precio objetivo.

### Precondiciones
- El usuario debe estar autenticado
- El usuario debe estar en la página de detalle de un producto Amazon
- El sistema de notificaciones debe estar operativo

### Flujo Principal
1. El estudiante está visualizando detalle de producto Amazon
2. El sistema muestra precio actual y gráfica histórica
3. El estudiante presiona botón "🔔 Crear Alerta de Precio"
4. El sistema muestra modal de configuración de alerta
5. El sistema sugiere precio objetivo basado en:
   - Precio mínimo histórico
   - 10% menos que precio actual
6. El estudiante puede:
   - Aceptar precio sugerido
   - Ingresar precio personalizado
7. El sistema valida que precio objetivo es menor que precio actual
8. El estudiante confirma la alerta
9. El sistema crea registro de alerta con estado "ACTIVA"
10. El sistema muestra confirmación: "Te notificaremos cuando el precio baje a X€"
11. El sistema añade el producto al dashboard de alertas del usuario

### Flujos Alternativos

#### 7a. Precio objetivo mayor o igual que precio actual
7. El sistema detecta precio objetivo no lógico
8. El sistema muestra error "El precio objetivo debe ser menor al actual"
9. El sistema mantiene el modal abierto
10. Vuelve al paso 6

#### 9a. Usuario alcanzó límite de alertas
9. El sistema detecta que el usuario tiene 10 alertas activas (límite)
10. El sistema muestra "Has alcanzado el límite de alertas activas"
11. El sistema sugiere desactivar alguna alerta existente
12. Si el usuario desactiva una:
    - Continúa con la creación de nueva alerta
13. Si cancela:
    - El caso de uso termina

### Postcondiciones
- **Éxito**: 
  - La alerta queda registrada y activa
  - El usuario será notificado cuando se cumpla la condición
  - La alerta aparece en el dashboard del usuario
- **Fracaso**: 
  - No se crea alerta
  - Se informa el motivo claramente

### Reglas de Negocio
- RN-23: Máximo 10 alertas activas por usuario
- RN-24: Las alertas se verifican cada 6 horas
- RN-25: Una alerta se desactiva automáticamente al dispararse
- RN-26: Las alertas caducan después de 90 días sin cumplirse
- RN-27: El precio objetivo debe ser al menos 1€ menor que el actual

---

## UC-07: Comprar Producto del Marketplace

### Información General
| Campo | Valor |
|-------|-------|
| **ID** | UC-07 |
| **Nombre** | Comprar Producto del Marketplace |
| **Actor Principal** | Estudiante (Comprador) |
| **Actores Secundarios** | Estudiante (Vendedor), Sistema de Mensajería |
| **Tipo** | Primario, Importante |
| **Prioridad** | Media |

### Descripción
El estudiante contacta con el vendedor de un producto del marketplace para acordar compra.

### Precondiciones
- El usuario debe estar autenticado
- El producto debe estar en estado "DISPONIBLE"
- El vendedor debe estar activo

### Flujo Principal
1. El estudiante busca o navega productos en marketplace
2. El sistema muestra productos con filtros por categoría y precio
3. El estudiante selecciona un producto de interés
4. El sistema muestra página de detalle con:
   - Fotos del producto
   - Descripción completa
   - Precio
   - Estado del producto
   - Ubicación de entrega (si la indicó el vendedor)
   - Información del vendedor (nombre, carrera)
5. El estudiante presiona "Contactar Vendedor"
6. El sistema abre ventana de chat integrado
7. El estudiante envía mensaje al vendedor
8. El sistema notifica al vendedor vía email y notificación push
9. El vendedor responde con detalles de encuentro
10. Ambos usuarios acuerdan:
    - Lugar de encuentro
    - Hora
    - Método de pago
11. (Fuera del sistema) Se realiza la transacción física
12. El vendedor marca producto como "VENDIDO"
13. El sistema archiva la conversación
14. El sistema solicita valoración mutua (opcional)

### Flujos Alternativos

#### 5a. Producto ya vendido
4. El sistema detecta que el producto cambió a "VENDIDO"
5. El sistema muestra mensaje "Producto ya vendido"
6. El sistema sugiere productos similares
7. El caso de uso termina

#### 8a. Vendedor no responde
8. Pasan 48 horas sin respuesta del vendedor
9. El sistema envía recordatorio al vendedor
10. Si no responde en 24h más:
    - El sistema sugiere al comprador buscar otros productos
    - El sistema marca publicación como "vendedor inactivo"
11. El caso de uso termina

#### 12a. Comprador no completa compra
11. El comprador decide no comprar
12. El vendedor mantiene producto como "DISPONIBLE"
13. Ambos pueden cerrar la conversación
14. El caso de uso termina

### Postcondiciones
- **Éxito**: 
  - El producto cambia a estado "VENDIDO"
  - Ambos usuarios pueden valorarse mutuamente
  - La publicación desaparece del marketplace activo
- **Fracaso**: 
  - El producto sigue disponible
  - La conversación queda abierta

### Reglas de Negocio
- RN-28: El sistema no gestiona pagos (solo facilita contacto)
- RN-29: Las transacciones son responsabilidad de los usuarios
- RN-30: Los chats se archivan después de 30 días de inactividad
- RN-31: Solo el vendedor puede marcar producto como vendido

---

## UC-08: Ver Histórico de Precios

### Información General
| Campo | Valor |
|-------|-------|
| **ID** | UC-08 |
| **Nombre** | Ver Histórico de Precios |
| **Actor Principal** | Estudiante |
| **Actores Secundarios** | Keepa API |
| **Tipo** | Secundario, Importante |
| **Prioridad** | Baja |

### Descripción
El estudiante visualiza gráficas de evolución histórica de precios de un producto Amazon para análisis de tendencias.

### Precondiciones
- El usuario debe estar autenticado
- Debe estar en página de detalle de producto Amazon
- Debe existir histórico de precios en Keepa

### Flujo Principal
1. El estudiante está en página de detalle de producto Amazon
2. El sistema muestra gráfica de precios de últimos 90 días por defecto
3. El sistema muestra:
   - Línea de evolución de precio
   - Precio actual marcado
   - Precio mínimo histórico marcado
   - Precio promedio como línea horizontal
4. El estudiante puede interactuar con la gráfica:
   - Hover sobre puntos para ver precio exacto y fecha
   - Zoom in/out en periodos específicos
5. El sistema muestra tres botones de periodo:
   - 30 días
   - 90 días (activo por defecto)
   - 1 año
6. El estudiante selecciona un periodo diferente
7. El sistema recarga gráfica con nuevos datos
8. El sistema actualiza estadísticas:
   - Precio mínimo en el periodo
   - Precio máximo en el periodo
   - Precio promedio
   - Variación porcentual
9. El sistema indica si es buen momento de compra con badge visual:
   - 🟢 "Excelente precio" (< 10% sobre mínimo histórico)
   - 🟡 "Buen precio" (10-20% sobre mínimo)
   - 🔴 "Precio elevado" (> 20% sobre mínimo)

### Flujos Alternativos

#### 2a. Sin datos históricos
2. El producto no tiene histórico en Keepa
3. El sistema muestra mensaje "Histórico no disponible aún"
4. El sistema registra producto para seguimiento futuro
5. El sistema sugiere crear alerta para cuando haya datos
6. El caso de uso termina

#### 7a. Error al cargar datos
7. Keepa API no responde o falla
8. El sistema muestra datos cacheados (si existen)
9. El sistema muestra advertencia "Datos pueden estar desactualizados"
10. El sistema intenta recargar automáticamente después de 30 segundos
11. Continúa en paso 8 con datos disponibles

### Postcondiciones
- **Éxito**: 
  - El usuario visualiza tendencias de precio
  - El usuario puede tomar decisión informada
  - Los datos se cachean para futuras consultas
- **Fracaso**: 
  - Se muestran datos parciales o mensaje apropiado
  - El usuario entiende limitaciones

### Reglas de Negocio
- RN-32: Los datos históricos se actualizan cada 6 horas
- RN-33: Las gráficas son interactivas (Chart.js)
- RN-34: Se cachean datos de gráficas por 6 horas
- RN-35: El periodo por defecto es 90 días

---

## Matriz de Trazabilidad

| Requisito Funcional | Casos de Uso Relacionados |
|---------------------|---------------------------|
| RF-01: Gestión de Usuarios | (Implícito en todos los UC - precondición de autenticación) |
| RF-02: Gestión de Quedadas | UC-01, UC-03, UC-05 |
| RF-03: Comparador de Precios Amazon | UC-02, UC-06, UC-08 |
| RF-04: Marketplace Estudiantil | UC-04, UC-07 |
| RF-05: Sistema de Ubicación | UC-05, UC-01 (integrado) |

---

> **Nota**: Estos casos de uso representan las funcionalidades core de UFV Shares. Casos de uso adicionales como "Editar Perfil", "Recuperar Contraseña", "Reportar Publicación", etc., están documentados en el análisis detallado del sistema.
