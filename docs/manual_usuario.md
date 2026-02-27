# 📖 Manual de Usuario - UFV Share

## Guía completa de uso de la plataforma

> **Versión:** 1.0 - Sprint 1  
> **Fecha:** Febrero 2026  
> **Estado:** En desarrollo - Funcionalidades básicas implementadas

---

## 📑 Índice

1. [Introducción](#introducción)
2. [Primeros pasos](#primeros-pasos)
3. [Registro de cuenta](#registro-de-cuenta)
4. [Inicio de sesión](#inicio-de-sesión)
5. [Explorar el catálogo](#explorar-el-catálogo)
6. [Buscar y filtrar productos](#buscar-y-filtrar-productos)
7. [Ver detalles de un producto](#ver-detalles-de-un-producto)
8. [Publicar un artículo](#publicar-un-artículo)
9. [Gestionar favoritos](#gestionar-favoritos)
10. [Proceso de compra/alquiler](#proceso-de-compra-alquiler)
11. [Perfil de usuario](#perfil-de-usuario)
12. [Preguntas frecuentes](#preguntas-frecuentes)

---

## 🎯 Introducción

### ¿Qué es UFV Share?

**UFV Share** es la plataforma oficial de intercambio de material entre estudiantes de la Universidad Francisco de Vitoria. Permite:

- 📚 **Comprar** libros, apuntes y material académico de segunda mano
- 💻 **Adquirir** tecnología, equipamiento y dispositivos electrónicos
- 🎵 **Rentar** instrumentos musicales, equipos audiovisuales y herramientas
- 🤝 **Conectar** con otros estudiantes de tu campus

### Objetivos de la plataforma

- ✅ Facilitar el acceso a material universitario a precios reducidos
- ♻️ Promover la economía circular y sostenibilidad
- 🔗 Fortalecer la comunidad universitaria UFV
- 💰 Permitir a estudiantes monetizar material que ya no utilizan

### Funcionalidades actuales (Sprint 1)

| Funcionalidad | Estado | Descripción |
|--------------|--------|-------------|
| Registro de usuarios | ✅ Implementado | Crear cuenta con email UFV |
| Inicio de sesión | ✅ Implementado | Autenticación segura con JWT |
| Catálogo de productos | ✅ Implementado | Ver listado completo |
| Filtrado por categoría | ✅ Implementado | Venta o alquiler |
| Publicar artículos | ✅ Implementado | Subir productos propios |
| Gestión de favoritos | ✅ Implementado | Guardar artículos de interés |
| Sistema de pago | 🔄 Próximamente | APIs externas pendientes |
| Mensajería interna | 🔄 Sprint 2 | Chat entre usuarios |
| Sistema de valoraciones | 🔄 Sprint 3 | Reputación y reviews |

---

## 🚀 Primeros pasos

### Requisitos previos

Para utilizar UFV Share necesitas:

1. **Email institucional UFV** (`@alumnos.ufv.es`)
2. **Navegador web moderno** (Chrome, Firefox, Safari, Edge)
3. **Conexión a Internet**

### Acceder a la plataforma

1. Abre tu navegador web
2. Accede a la URL: **`https://ufvshare.azurewebsites.net`** *(en producción)*
   - O en desarrollo: `http://localhost:4321`
3. Verás la página de bienvenida con dos opciones principales:
   - **Registrarse** (si es tu primera vez)
   - **Iniciar sesión** (si ya tienes cuenta)

_[ESPACIO PARA CAPTURA: Pantalla de bienvenida]_

---

## 📝 Registro de cuenta

### Paso 1: Acceder al formulario de registro

1. En la página principal, haz clic en **"Registrarse"** o **"Crear cuenta"**
2. Serás redirigido a `/auth` con la pestaña de registro activa

_[ESPACIO PARA CAPTURA: Botón de registro en landing]_

### Paso 2: Completar el formulario

El formulario de registro solicita los siguientes datos:

| Campo | Descripción | Validación |
|-------|-------------|------------|
| **Nombre completo** | Tu nombre y apellidos | Obligatorio, mínimo 3 caracteres |
| **Email institucional** | Tu correo @alumnos.ufv.es | Formato email válido, dominio UFV |
| **Contraseña** | Mínimo 8 caracteres | Letras, números y símbolos |
| **Confirmar contraseña** | Repetir contraseña | Debe coincidir |

_[ESPACIO PARA CAPTURA: Formulario de registro completo]_

### Paso 3: Validaciones del sistema

El sistema verifica automáticamente:

- ✅ El email es del dominio `@alumnos.ufv.es`
- ✅ El email no está registrado previamente
- ✅ La contraseña cumple requisitos de seguridad
- ✅ Las contraseñas coinciden

### Paso 4: Crear cuenta

1. Revisa que todos los datos sean correctos
2. Haz clic en **"Crear cuenta"**
3. Si todo es correcto, verás un mensaje de éxito
4. Serás redirigido automáticamente al **dashboard principal**

_[ESPACIO PARA CAPTURA: Mensaje de registro exitoso]_

### ⚠️ Errores comunes al registrarse

| Error | Causa | Solución |
|-------|-------|----------|
| "Email ya registrado" | Ya existe una cuenta con ese email | Usa "Iniciar sesión" o recupera contraseña |
| "Email no válido" | No es un correo @alumnos.ufv.es | Usa tu email institucional oficial |
| "Contraseña débil" | No cumple requisitos mínimos | Usa letras, números y símbolos |
| "Las contraseñas no coinciden" | Error al confirmar | Revisa ambos campos |

---

## 🔐 Inicio de sesión

### Paso 1: Acceder al formulario

1. En la página principal, haz clic en **"Iniciar sesión"** o **"Login"**
2. Serás redirigido a `/auth` con la pestaña de login activa

_[ESPACIO PARA CAPTURA: Botón de login en navbar]_

### Paso 2: Introducir credenciales

Ingresa tu información de acceso:

- **Email:** Tu correo @alumnos.ufv.es registrado
- **Contraseña:** La contraseña que creaste al registrarte

_[ESPACIO PARA CAPTURA: Formulario de login]_

### Paso 3: Autenticación

1. Haz clic en **"Iniciar sesión"**
2. El sistema validará tus credenciales
3. Si son correctas, recibirás un **token de sesión** (JWT)
4. Serás redirigido automáticamente al **dashboard** (`/app`)

_[ESPACIO PARA CAPTURA: Dashboard después del login]_

### 🔒 Seguridad de tu sesión

- Tu sesión permanece activa mientras uses la plataforma
- El token JWT se almacena de forma segura en tu navegador
- Las contraseñas están encriptadas con BCrypt
- Nunca compartas tu contraseña con nadie

### ⚠️ Errores comunes al iniciar sesión

| Error | Causa | Solución |
|-------|-------|----------|
| "Credenciales inválidas" | Email o contraseña incorrectos | Verifica tus datos o recupera contraseña |
| "Usuario no encontrado" | Email no registrado | Crea una cuenta primero |
| "Sesión expirada" | Token caducado | Vuelve a iniciar sesión |

---

## 🏠 Explorar el catálogo

### Vista principal del dashboard

Una vez hayas iniciado sesión, accederás al dashboard principal que muestra:

**Elementos de la interfaz:**

1. **Barra de navegación superior**
   - Logo UFV Share (volver al inicio)
   - Buscador de productos
   - Icono de favoritos
   - Icono de carrito
   - Avatar de perfil

2. **Filtros laterales** (panel izquierdo)
   - Categorías de productos
   - Tipo: Venta / Alquiler
   - Rango de precios
   - Estado del artículo

3. **Grid de productos** (área central)
   - Cards de productos con:
     - Imagen destacada
     - Título del producto
     - Precio
     - Tipo (Venta/Alquiler)
     - Usuario vendedor
     - Botón "Ver detalles"

4. **Barra lateral derecha** (opcional)
   - Productos destacados
   - Últimas publicaciones

_[ESPACIO PARA CAPTURA: Vista completa del dashboard]_

### Tipos de productos disponibles

La plataforma actualmente cuenta con **50 productos de prueba** en 10 categorías:

| Categoría | Ejemplos | Cantidad |
|-----------|----------|----------|
| 📚 **Libros académicos** | Álgebra, Java, Economía, Física | 10 |
| 💻 **Tecnología** | iPad, MacBook, monitores, discos duros | 8 |
| 🎵 **Instrumentos musicales** | Guitarras, teclado, batería electrónica | 4 |
| 🔧 **Equipamiento especializado** | Calculadoras, cámaras, micrófonos | 9 |
| 🏠 **Muebles para estudiantes** | Escritorios, sillas, estanterías | 4 |
| 🎮 **Videojuegos y consolas** | PS5, Nintendo Switch, juegos Xbox | 3 |
| 🚴 **Transporte** | Bicicletas, longboard, patinete eléctrico | 3 |
| 👔 **Ropa formal (alquiler)** | Trajes, vestidos de gala | 2 |
| 🍳 **Electrodomésticos** | Cafeteras, batidoras | 2 |
| 📖 **Colecciones** | Harry Potter, Dungeons & Dragons | 2 |

_[ESPACIO PARA CAPTURA: Grid de productos con varias categorías]_

---

## 🔍 Buscar y filtrar productos

### Buscador principal

**Ubicación:** Barra superior del dashboard

**Funcionalidad:**
1. Escribe palabras clave en el buscador
2. El sistema busca coincidencias en:
   - Títulos de productos
   - Descripciones
   - Nombres de usuarios vendedores
3. Los resultados se actualizan en tiempo real

_[ESPACIO PARA CAPTURA: Buscador en acción con resultados]_

**Ejemplos de búsquedas:**
- "Java" → Muestra libros de programación
- "Guitarra" → Muestra instrumentos musicales
- "IKEA" → Muestra muebles
- "iPad" → Muestra tecnología Apple

### Filtros por categoría

**Panel de filtros laterales:**

#### 1. Filtro por tipo de transacción

- 🛒 **Venta:** Productos en venta definitiva
- 🔄 **Alquiler:** Productos disponibles para rentar

_[ESPACIO PARA CAPTURA: Toggle de filtro Venta/Alquiler]_

**Ejemplo práctico:**
- Selecciona "Alquiler" para ver solo equipamiento que puedes rentar temporalmente (cámaras, proyectores, trajes formales, etc.)

#### 2. Filtro por rango de precios

- **Rango:** 0€ - 1000€
- **Control:** Slider de doble manija
- **Actualización:** Dinámica al mover el slider

_[ESPACIO PARA CAPTURA: Slider de rango de precios]_

#### 3. Filtro por categoría

Lista de categorías disponibles:
- [ ] Libros y material académico
- [ ] Tecnología y electrónica
- [ ] Instrumentos musicales
- [ ] Equipamiento especializado
- [ ] Muebles
- [ ] Videojuegos
- [ ] Transporte
- [ ] Ropa y accesorios
- [ ] Electrodomésticos
- [ ] Otros

_[ESPACIO PARA CAPTURA: Checkboxes de categorías]_

### Ordenar resultados

**Opciones de ordenamiento:**

| Criterio | Descripción |
|----------|-------------|
| Más recientes | Productos publicados recientemente |
| Más antiguos | Productos con más tiempo en catálogo |
| Menor precio | Del más barato al más caro |
| Mayor precio | Del más caro al más barato |
| Alfabético A-Z | Por título del producto |

_[ESPACIO PARA CAPTURA: Dropdown de ordenamiento]_

---

## 📦 Ver detalles de un producto

### Acceder a la vista de detalle

Desde el catálogo:
1. Localiza el producto que te interesa
2. Haz clic en la card del producto o en "Ver detalles"
3. Serás redirigido a `/product/[id]`

_[ESPACIO PARA CAPTURA: Card de producto con botón "Ver detalles"]_

### Componentes de la página de detalle

#### 1. Galería de imágenes

- **Imagen principal:** Vista ampliada del producto
- **Miniaturas:** Imágenes adicionales (si están disponibles)
- **Zoom:** Funcionalidad de ampliar imagen

_[ESPACIO PARA CAPTURA: Galería de imágenes del producto]_

#### 2. Información principal

**Ubicación:** Panel derecho junto a imágenes

**Datos mostrados:**

```
📦 Título del producto
Ej: "MacBook Air M1 2020 8GB/256GB"

💰 Precio
Ej: 750,00€ (venta) o 50€/semana (alquiler)

🏷️ Tipo de transacción
Badge: VENTA o ALQUILER

👤 Vendedor/Propietario
Ej: "Javier Romero"
📧 javier.romero@alumnos.ufv.es

📅 Fecha de publicación
Ej: "Publicado hace 3 días"

✅ Estado
Ej: "Disponible"
```

_[ESPACIO PARA CAPTURA: Panel de información principal]_

#### 3. Descripción completa

**Sección expandible con:**
- Descripción detallada del producto
- Estado de conservación
- Accesorios incluidos
- Características técnicas
- Condiciones de venta/alquiler

_[ESPACIO PARA CAPTURA: Descripción expandida]_

**Ejemplo real:**

> **MacBook Air M1 2020 8GB/256GB**
> 
> Color oro. Perfecto estado, apenas usado. Batería al 97% de salud. Incluye cargador original. Ideal para diseño y programación.
> 
> **Especificaciones:**
> - Procesador: Apple M1
> - RAM: 8GB
> - Almacenamiento: 256GB SSD
> - Pantalla: 13.3" Retina
> - Año: 2020
> 
> **Incluye:**
> - MacBook Air
> - Cargador MagSafe original
> - Caja original

#### 4. Datos del vendedor

**Sección del propietario:**
- Avatar o iniciales
- Nombre completo
- Email de contacto
- Productos publicados (contador)
- Valoración media (🔄 próximamente)

_[ESPACIO PARA CAPTURA: Card del vendedor]_

#### 5. Botones de acción

**Acciones disponibles:**

| Botón | Función | Estado actual |
|-------|---------|---------------|
| 💚 **Añadir a favoritos** | Guardar en lista personal | ✅ Funcional |
| 🛒 **Añadir al carrito** | Iniciar proceso de compra | ✅ Funcional |
| 💬 **Contactar vendedor** | Enviar mensaje directo | 🔄 Sprint 2 |
| 📤 **Compartir** | Compartir en redes sociales | 🔄 Sprint 2 |
| 🚩 **Reportar** | Indicar contenido inapropiado | 🔄 Sprint 3 |

_[ESPACIO PARA CAPTURA: Botones de acción principales]_

### Productos relacionados

**Ubicación:** Parte inferior de la página

**Funcionalidad:**
- Muestra 4-6 productos similares
- Basado en categoría y precio
- Facilita exploración del catálogo

_[ESPACIO PARA CAPTURA: Carrusel de productos relacionados]_

---

## ➕ Publicar un artículo

### Acceder al formulario de publicación

**Ubicación del botón:**
1. Dashboard principal → Botón flotante **"+ Publicar"** (esquina inferior derecha)
2. O Navbar → **"Vender/Alquilar"**
3. Redirige a `/product/new`

_[ESPACIO PARA CAPTURA: Botón flotante de publicar]_

### Formulario de publicación

#### Paso 1: Información básica

**Campos requeridos:**

| Campo | Descripción | Validación |
|-------|-------------|------------|
| **Título** | Nombre descriptivo del producto | 5-100 caracteres |
| **Tipo** | Venta o Alquiler | Radio button obligatorio |
| **Precio** | Cantidad en euros | Número positivo, máx 2 decimales |
| **Categoría** | Tipo de producto | Selección de dropdown |

_[ESPACIO PARA CAPTURA: Campos de información básica]_

**Ejemplo de título correcto:**
- ✅ "Guitarra española Alhambra modelo 1C con funda"
- ✅ "MacBook Air M1 2020 8GB/256GB color oro"
- ❌ "Guitarra" (muy genérico)
- ❌ "VENDO PORTÁTIL URGENTE!!!!" (mayúsculas, excesivo)

#### Paso 2: Descripción detallada

**Editor de texto:**
- Área de texto amplia (mínimo 50 caracteres)
- Soporte para saltos de línea
- Preview en tiempo real

**Qué incluir en la descripción:**
- ✅ Estado de conservación del producto
- ✅ Año de compra (si aplica)
- ✅ Motivo de venta
- ✅ Accesorios incluidos
- ✅ Características técnicas relevantes
- ✅ Condiciones especiales (si hay)

_[ESPACIO PARA CAPTURA: Editor de descripción]_

**Plantilla sugerida:**

```
[ESTADO DEL PRODUCTO]
Describe el estado actual: nuevo, como nuevo, buen estado, etc.

[CARACTERÍSTICAS]
Lista las especificaciones técnicas importantes.

[QUÉ INCLUYE]
Detalla todos los accesorios y elementos adicionales.

[CONDICIONES]
Indica condiciones de entrega, garantías, etc.
```

#### Paso 3: Imágenes del producto

**Gestor de imágenes:**
- **Cantidad:** 1-5 imágenes
- **Formato:** JPG, PNG, WebP
- **Tamaño máximo:** 5MB por imagen
- **Recomendación:** Mínimo 800x600px

_[ESPACIO PARA CAPTURA: Zona de carga de imágenes]_

**Tips para buenas fotos:**
1. 📸 Usa buena iluminación natural
2. 🖼️ Fondo limpio y neutro
3. 📏 Muestra el producto desde varios ángulos
4. 🔍 Destaca detalles importantes
5. 📐 Incluye foto de accesorios juntos
6. ❌ Evita filtros o ediciones excesivas

#### Paso 4: Información de contacto

**Datos de contacto:**
- Email automáticamente asignado (tu email institucional)
- Opción de añadir teléfono (opcional)
- Preferencias de contacto (email, WhatsApp)

_[ESPACIO PARA CAPTURA: Sección de contacto]_

#### Paso 5: Revisión y publicación

**Antes de publicar:**

Checklist automático:
- [ ] Título descriptivo completo
- [ ] Tipo de transacción seleccionado
- [ ] Precio ingresado correctamente
- [ ] Categoría asignada
- [ ] Descripción detallada (mín. 50 caracteres)
- [ ] Al menos 1 imagen cargada
- [ ] Información de contacto correcta

_[ESPACIO PARA CAPTURA: Preview del producto antes de publicar]_

**Publicar:**
1. Revisa todos los campos en el preview
2. Acepta los términos de uso (checkbox)
3. Haz clic en **"Publicar artículo"**
4. Confirma la publicación en el modal
5. ✅ Tu producto aparecerá inmediatamente en el catálogo

_[ESPACIO PARA CAPTURA: Modal de confirmación de publicación]_

### Gestionar tus publicaciones

**Ubicación:** Perfil → **"Mis publicaciones"**

**Acciones disponibles:**
- ✏️ **Editar:** Modificar información del producto
- 🗑️ **Eliminar:** Quitar del catálogo
- 📊 **Estadísticas:** Ver visitas y favoritos (🔄 Sprint 2)
- ⏸️ **Pausar:** Marcar como no disponible temporalmente (🔄 Sprint 2)

_[ESPACIO PARA CAPTURA: Panel de gestión de publicaciones]_

---

## ❤️ Gestionar favoritos

### Agregar a favoritos

**Desde cualquier producto:**
1. Haz clic en el icono de corazón ❤️
2. El corazón se rellena indicando que se guardó
3. Aparece notificación: "Añadido a favoritos"

_[ESPACIO PARA CAPTURA: Botón de favoritos en hover y activado]_

**Ubicaciones del botón:**
- Card de producto en el catálogo (esquina superior derecha)
- Página de detalle del producto (junto a "Añadir al carrito")

### Ver lista de favoritos

**Acceso:**
1. Click en icono de corazón en la navbar
2. O desde Perfil → **"Mis favoritos"**
3. Redirige a `/favorites`

_[ESPACIO PARA CAPTURA: Navbar con icono de favoritos]_

### Página de favoritos

**Elementos mostrados:**

```
❤️ Mis Favoritos (12)

┌─────────────────────────────────────────┐
│  [Imagen] MacBook Air M1                │
│  750€ · Venta · Javier Romero           │
│  [Ver] [Eliminar] [Añadir al carrito]   │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│  [Imagen] Guitarra Alhambra             │
│  180€ · Venta · Isabel Prieto           │
│  [Ver] [Eliminar] [Añadir al carrito]   │
└─────────────────────────────────────────┘
```

_[ESPACIO PARA CAPTURA: Lista completa de favoritos]_

**Funcionalidades:**
- **Filtrar:** Por tipo (venta/alquiler)
- **Ordenar:** Por precio, fecha añadido
- **Eliminar:** Quitar de favoritos individualmente
- **Eliminar todos:** Vaciar lista completa
- **Añadir varios al carrito:** Selección múltiple (🔄 Sprint 2)

### Notificaciones de favoritos

**Alertas automáticas (🔄 Sprint 2):**
- 💰 Bajada de precio
- ✅ Producto disponible nuevamente
- ⚠️ Producto próximo a venderse
- 🗑️ Producto eliminado por el vendedor

_[ESPACIO PARA CAPTURA: Notificación de cambio en favorito]_

---

## 🛒 Proceso de compra/alquiler

> **⚠️ IMPORTANTE:** El sistema de pago completo con pasarelas externas está planificado para **Sprint 2**. Actualmente puedes añadir productos al carrito y contactar directamente con el vendedor.

### Paso 1: Añadir al carrito

**Desde la página del producto:**
1. Haz clic en **"Añadir al carrito"** 🛒
2. Aparece notificación de confirmación
3. El contador del carrito se actualiza

_[ESPACIO PARA CAPTURA: Botón "Añadir al carrito" y notificación]_

**Información añadida al carrito:**
- Producto seleccionado
- Precio actual
- Tipo de transacción (venta/alquiler)
- Datos del vendedor

### Paso 2: Ver carrito

**Acceder al carrito:**
1. Click en icono 🛒 en navbar
2. O navega a `/cart`
3. Se muestra resumen de productos

_[ESPACIO PARA CAPTURA: Vista del carrito con productos]_

**Información mostrada:**

```
🛒 Carrito de Compras (3 productos)

┌─────────────────────────────────────────┐
│  VENTA                                   │
│  [IMG] MacBook Air M1 2020               │
│  Vendedor: Javier Romero                 │
│  Precio: 750,00€                         │
│  [Eliminar] [Ver detalles]               │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│  ALQUILER (1 semana)                     │
│  [IMG] Cámara Canon EOS M50              │
│  Vendedor: Laura Sánchez                 │
│  Precio: 50,00€                          │
│  [Eliminar] [Ver detalles]               │
└─────────────────────────────────────────┘

───────────────────────────────────────────
Subtotal venta:     750,00€
Subtotal alquiler:   50,00€
───────────────────────────────────────────
TOTAL:              800,00€
```

### Paso 3: Revisar resumen

**Resumen del pedido:**
- Lista de productos a adquirir
- Detalles de cada vendedor
- Precios individuales y totales
- Tipo de transacción (venta/alquiler)
- Condiciones especiales

_[ESPACIO PARA CAPTURA: Resumen completo del carrito]_

### Paso 4: Contactar con vendedores

**Actualmente (Sprint 1):**

Cada producto muestra:
- 📧 Email del vendedor
- 📞 Teléfono (si está disponible)
- 💬 Botón "Contactar al vendedor"

**Al hacer clic en "Contactar al vendedor":**
1. Se abre tu cliente de email predeterminado
2. Destinatario: email del vendedor
3. Asunto: "Interesado en [Nombre del producto]"
4. Redacta tu mensaje personalmente

_[ESPACIO PARA CAPTURA: Botón de contacto y email generado]_

**Plantilla sugerida para el mensaje:**

```
Asunto: Interesado en MacBook Air M1 2020

Hola Javier,

Me interesa tu MacBook Air M1 que vi en UFV Share.

Quisiera saber:
- ¿Sigue disponible?
- ¿Dónde podemos quedar para verlo?
- ¿Aceptas algún método de pago en particular?

Gracias,
[Tu nombre]
```

### Paso 5: Proceso de pago (🔄 Próximamente - Sprint 2)

**Funcionalidades planificadas:**

#### Métodos de pago integrados:
- 💳 **Tarjeta de crédito/débito** (Stripe)
- 🏦 **Transferencia bancaria** (confirmación automática)
- 📱 **Bizum** (integración API)
- 💰 **PayPal** (checkout directo)
- 🤝 **Efectivo en persona** (opción con confirmación)

_[ESPACIO RESERVADO: Mockup de selección de método de pago]_

#### Pasarela de pago segura:
1. Selección de método de pago
2. Ingreso de datos de pago (encriptados)
3. Confirmación de transacción
4. Notificación a vendedor y comprador
5. Sistema de garantía (retención temporal)

_[ESPACIO RESERVADO: Mockup de pasarela de pago]_

#### Sistema de garantías:
- El dinero se retiene hasta confirmar entrega
- Protección comprador y vendedor
- Resolución de disputas automatizada
- Rating obligatorio post-transacción

### Paso 6: Confirmación de compra/alquiler

**Una vez completada la transacción:**

**Comprador recibe:**
- ✅ Email de confirmación
- 📄 Recibo digital descargable
- 📊 Tracking del proceso
- 💬 Chat activado con vendedor
- 📍 Punto de encuentro sugerido (si aplica)

**Vendedor recibe:**
- 🔔 Notificación de venta
- 📧 Datos del comprador
- 💰 Confirmación de pago
- 📋 Instrucciones de entrega

_[ESPACIO PARA CAPTURA: Email de confirmación de compra]_

### Caso especial: Alquileres

**Información adicional para alquileres:**

| Campo | Descripción |
|-------|-------------|
| **Duración** | Periodo de alquiler (días/semanas) |
| **Precio por periodo** | Coste de alquiler |
| **Fianza** | Depósito de seguridad (si aplica) |
| **Fecha inicio** | Cuándo recoger el producto |
| **Fecha fin** | Cuándo devolver el producto |
| **Estado de recogida** | Checklist del producto |
| **Estado de devolución** | Verificación al devolver |

_[ESPACIO PARA CAPTURA: Formulario específico de alquiler]_

**Proceso de alquiler:**

1️⃣ **Solicitud de alquiler**
- Selecciona fechas de inicio y fin
- Sistema verifica disponibilidad
- Calcula precio total automáticamente

2️⃣ **Confirmación del propietario**
- Vendedor aprueba o rechaza
- Propone alternativas si no está disponible

3️⃣ **Pago y reserva**
- Se paga el alquiler + fianza
- Producto queda reservado

4️⃣ **Recogida**
- Acuerdo de punto de encuentro
- Checklist de estado del producto
- Fotos de referencia (recomendado)

5️⃣ **Uso del producto**
- Periodo de alquiler activo
- Recordatorios antes de vencimiento

6️⃣ **Devolución**
- Re-checklist del estado
- Comparación con fotos iniciales
- Devolución de fianza (si todo OK)

_[ESPACIO PARA CAPTURA: Timeline del proceso de alquiler]_

---

## 👤 Perfil de usuario

### Acceder a tu perfil

**Ubicación:**
1. Click en avatar/iniciales en navbar (esquina superior derecha)
2. Selecciona **"Mi perfil"**
3. Redirige a `/profile`

_[ESPACIO PARA CAPTURA: Menú dropdown del avatar]_

### Secciones del perfil

#### 1. Información personal

**Datos visibles:**
- Foto de perfil (avatar)
- Nombre completo
- Email institucional (público)
- Teléfono (opcional, oculto por defecto)
- Miembro desde [fecha]
- Última conexión

_[ESPACIO PARA CAPTURA: Cabecera del perfil]_

**Botón editar:**
- Cambiar foto de perfil
- Actualizar nombre
- Modificar visibilidad del teléfono
- Cambiar contraseña

_[ESPACIO PARA CAPTURA: Modal de edición de perfil]_

#### 2. Mis publicaciones

**Vista de grid de productos publicados:**

```
📦 Mis Publicaciones (5)

[+ Publicar nuevo artículo]

┌─────────────────────────────────────────┐
│  [IMG] Libro de Álgebra                  │
│  25€ · Venta · 12 visitas · 3 favoritos  │
│  Estado: Activo                          │
│  [Editar] [Pausar] [Eliminar]            │
└─────────────────────────────────────────┘
```

_[ESPACIO PARA CAPTURA: Grid de publicaciones propias]_

**Estadísticas por producto:**
- 👁️ Número de visitas
- ❤️ Número de favoritos
- 📅 Fecha de publicación
- 📊 Interacciones (mensajes recibidos)

#### 3. Mis compras

**Historial de transacciones:**
- Productos comprados
- Productos alquilados
- Fecha de transacción
- Estado (completado, pendiente, en disputa)
- Valoración dada

_[ESPACIO PARA CAPTURA: Lista de compras históricas]_

#### 4. Mis ventas

**Productos vendidos/alquilados:**
- Fecha de venta
- Comprador
- Precio final
- Estado de entrega
- Valoración recibida

_[ESPACIO PARA CAPTURA: Lista de ventas]_

#### 5. Mis favoritos

**Acceso rápido a productos guardados:**
- Ver lista completa
- Gestionar favoritos
- Recibir alertas de cambios

#### 6. Configuración

**Opciones disponibles:**

| Sección | Opciones |
|---------|----------|
| **Privacidad** | Perfil público/privado, mostrar email, mostrar teléfono |
| **Notificaciones** | Email, push, SMS (🔄 Sprint 2) |
| **Seguridad** | Cambiar contraseña, sesiones activas, 2FA (🔄 Sprint 3) |
| **Preferencias** | Idioma, zona horaria, categorías favoritas |

_[ESPACIO PARA CAPTURA: Panel de configuración]_

#### 7. Valoraciones (🔄 Sprint 3)

**Sistema de reputación:**
- ⭐ Valoración media (1-5 estrellas)
- 💬 Comentarios recibidos
- 📊 Estadísticas de fiabilidad
- 🏆 Insignias y logros

_[ESPACIO RESERVADO: Sistema de valoraciones]_

---

## ❓ Preguntas frecuentes

### Generales

**¿Necesito ser estudiante UFV para usar la plataforma?**
> Sí, actualmente UFV Share es exclusivo para estudiantes, profesores y personal de la Universidad Francisco de Vitoria. Necesitas un email institucional `@alumnos.ufv.es` para registrarte.

**¿Es gratuito usar UFV Share?**
> Sí, crear cuenta, publicar productos y usar todas las funcionalidades de la plataforma es completamente gratuito. No hay comisiones por venta (aplicable en Sprint 1).

**¿Qué tipos de productos puedo vender/alquilar?**
> Cualquier artículo relacionado con la vida universitaria: libros, apuntes, tecnología, instrumentos musicales, muebles, ropa formal, equipamiento deportivo, etc. No se permiten productos prohibidos o ilegales.

**¿Puedo vender productos fuera del campus?**
> La plataforma está diseñada para facilitar el intercambio dentro de la comunidad UFV, por lo que recomendamos encuentros en el campus. Sin embargo, puedes acordar entregas fuera del campus directamente con el comprador.

### Seguridad y confianza

**¿Es seguro comprar a través de UFV Share?**
> Sí. Todos los usuarios están verificados con su email institucional UFV. Además, implementaremos un sistema de valoraciones y garantías de pago en Sprint 2 para mayor seguridad.

**¿Qué hago si hay un problema con una transacción?**
> En Sprint 3 implementaremos un sistema de resolución de disputas. Actualmente, puedes contactar con soporte a través de `soporte@ufvshares.es` para mediar en conflictos.

**¿Cómo reporto un producto o usuario sospechoso?**
> En cada producto encontrarás un botón "Reportar" (disponible en Sprint 3). Mientras tanto, envía un email a `soporte@ufvshares.es` con detalles del problema.

### Publicaciones

**¿Cuántos productos puedo publicar?**
> Actualmente no hay límite. Puedes publicar tantos productos como desees de forma gratuita.

**¿Cuánto tiempo permanece activa una publicación?**
> Las publicaciones permanecen activas indefinidamente hasta que tú las elimines o marques como vendidas. Recibirás recordatorios cada 30 días para renovar o eliminar publicaciones antiguas.

**¿Puedo editar un producto después de publicarlo?**
> Sí. Ve a "Mi perfil" → "Mis publicaciones" → "Editar" para modificar cualquier información del producto.

**¿Debo incluir fotos reales del producto?**
> Sí, es obligatorio usar fotos reales del producto que vendes. No se permiten imágenes de internet. Esto genera confianza y evita malentendidos.

### Pagos y transacciones

**¿Cómo funciona el pago?**
> **Actualmente (Sprint 1):** Contactas directamente con el vendedor por email y acordáis el método de pago (efectivo, Bizum, transferencia).
> 
> **Próximamente (Sprint 2):** Integraremos pasarelas de pago seguras (Stripe, PayPal, Bizum) directamente en la plataforma con sistema de garantías.

**¿Qué métodos de pago se aceptan?**
> Los vendedores pueden especificar sus métodos preferidos. Los más comunes son:
> - 💵 Efectivo en mano
> - 📱 Bizum
> - 🏦 Transferencia bancaria
> - 💳 Tarjeta (a través de la plataforma en Sprint 2)

**¿Hay comisiones por vender?**
> **Actualmente:** No, UFV Share es 100% gratuito sin comisiones.
> 
> **Futuro:** Se evaluará una pequeña comisión (2-3%) solo para transacciones con pago integrado para cubrir costes de pasarela de pago.

### Alquileres

**¿Cómo funcionan los alquileres?**
> El propietario especifica el precio por periodo (día, semana, mes). Contactas con él, acordáis fechas, y opcionalmente se pide una fianza que se devuelve al entregar el producto en buen estado.

**¿Qué pasa si devuelvo el producto dañado?**
> El propietario puede retener total o parcialmente la fianza según el daño. En Sprint 3 implementaremos un sistema de checklist fotográfico automático para evitar disputas.

**¿Puedo extender un periodo de alquiler?**
> Sí, contacta con el propietario antes de que expire el periodo. Si el producto no tiene nuevas reservas, generalmente pueden extenderte el alquiler.

### Favoritos y notificaciones

**¿Para qué sirven los favoritos?**
> Te permiten guardar productos que te interesan para revisarlos después. En Sprint 2 añadiremos alertas automáticas de cambios de precio y disponibilidad.

**¿Recibiré notificaciones por email?**
> Sí. Puedes configurar qué notificaciones deseas recibir en "Perfil" → "Configuración" → "Notificaciones". Por defecto recibirás:
> - Confirmaciones de compra/venta
> - Mensajes de otros usuarios (Sprint 2)
> - Cambios en favoritos (Sprint 2)

### Cuenta y perfil

**¿Cómo cambio mi contraseña?**
> Ve a "Mi perfil" → "Configuración" → "Seguridad" → "Cambiar contraseña". Necesitarás ingresar tu contraseña actual.

**¿Puedo eliminar mi cuenta?**
> Sí. En "Configuración" → "Cuenta" → "Eliminar cuenta". Ten en cuenta que esto eliminará permanentemente todas tus publicaciones, favoritos e historial.

**¿Qué pasa con mis datos personales?**
> UFV Share cumple con RGPD. Tus datos solo son visibles para otros usuarios de forma limitada (nombre, email para contacto). Nunca compartimos información con terceros sin tu consentimiento.

### Problemas técnicos

**La página no carga correctamente**
> - Actualiza la página (F5)
> - Limpia caché del navegador
> - Prueba en modo incógnito
> - Verifica tu conexión a Internet
> - Usa un navegador actualizado (Chrome, Firefox, Edge)

**No puedo subir imágenes**
> Verifica que:
> - El archivo sea JPG, PNG o WebP
> - El tamaño sea menor a 5MB
> - Tu navegador tenga permisos de acceso a archivos
> - La imagen no esté corrupta

**No recibo el email de confirmación**
> - Revisa tu carpeta de SPAM
> - Verifica que escribiste correctamente tu email
> - Añade `noreply@ufvshares.es` a tus contactos
> - Contacta con soporte si persiste el problema

---

## 📞 Soporte y contacto

### Canales de ayuda

**Email de soporte:**
📧 `soporte@ufvshares.es`
- Tiempo de respuesta: 24-48h laborables
- Para: problemas técnicos, disputas, sugerencias

**Reportar bugs:**
🐛 GitHub Issues: `github.com/ufv-shares/issues`
- Describe el problema detalladamente
- Incluye capturas de pantalla
- Indica navegador y versión

**Comunidad:**
💬 Discord UFV Share (🔄 Próximamente)
- Chat con otros usuarios
- Consejos y mejores prácticas
- Novedades y actualizaciones

### Feedback y sugerencias

**¿Tienes ideas para mejorar UFV Share?**

Nos encantaría escucharte:
- 📝 Formulario de feedback: `/feedback` (🔄 Sprint 2)
- 📧 Email: `feedback@ufvshares.es`
- 🗳️ Encuestas trimestrales
- 💡 Buzón de sugerencias en la plataforma

---

## 🔮 Próximas funcionalidades

### Sprint 2 (En desarrollo)

| Funcionalidad | Descripción | Estado |
|---------------|-------------|--------|
| 💬 **Mensajería interna** | Chat en tiempo real entre usuarios | 🔄 |
| 💳 **Pasarela de pago** | Stripe, PayPal, Bizum integrados | 🔄 |
| 🔔 **Sistema de notificaciones** | Push, email y SMS | 🔄 |
| 📊 **Dashboard de estadísticas** | Analytics de tus publicaciones | 🔄 |
| 🔍 **Búsqueda avanzada** | Filtros complejos y guardados | 🔄 |

### Sprint 3 (Planificado)

| Funcionalidad | Descripción | Estado |
|---------------|-------------|--------|
| ⭐ **Sistema de valoraciones** | Rating y reviews de usuarios | 📅 |
| 🏆 **Gamificación** | Insignias y niveles de usuario | 📅 |
| 🤝 **Sistema de garantías** | Protección comprador/vendedor | 📅 |
| 🚩 **Reportes y moderación** | Denunciar contenido inapropiado | 📅 |
| 🔐 **Autenticación 2FA** | Seguridad adicional | 📅 |

### Sprint 4 (Futuro)

| Funcionalidad | Descripción | Estado |
|---------------|-------------|--------|
| 📱 **App móvil** | iOS y Android nativo | 💡 |
| 🌍 **Internacionalización** | Soporte multiidioma | 💡 |
| 🤖 **Recomendaciones IA** | Sugerencias personalizadas | 💡 |
| 📍 **Mapa de encuentros** | Puntos seguros en campus | 💡 |
| 🎓 **Integración académica** | Sincronizar con horarios UFV | 💡 |

**Leyenda:**
- ✅ Implementado
- 🔄 En desarrollo
- 📅 Planificado
- 💡 Idea/Propuesta

---

## 📚 Glosario de términos

**Términos de la plataforma:**

- **Catálogo:** Listado completo de productos disponibles
- **Dashboard:** Página principal después de iniciar sesión
- **Favoritos:** Lista personal de productos guardados
- **Grid:** Vista en cuadrícula de productos
- **JWT:** Token de autenticación de sesión (JSON Web Token)
- **PBI:** Product Backlog Item (elemento del backlog en SCRUM)
- **Sprint:** Periodo de desarrollo (2 semanas)
- **Timeline:** Línea temporal de eventos o acciones

**Términos de transacciones:**

- **Alquiler:** Renta temporal de un producto
- **Comprador:** Usuario que adquiere un producto
- **Fianza:** Depósito de seguridad para alquileres
- **Pasarela de pago:** Sistema seguro para procesar pagos
- **Vendedor/Propietario:** Usuario que publica un producto
- **Venta:** Transferencia definitiva de propiedad

**Términos técnicos:**

- **API:** Application Programming Interface (interfaz de programación)
- **CORS:** Cross-Origin Resource Sharing (compartir recursos entre dominios)
- **RGPD:** Reglamento General de Protección de Datos
- **TDD:** Test Driven Development (desarrollo guiado por tests)
- **WCAG:** Web Content Accessibility Guidelines (pautas de accesibilidad)

---

## 📄 Información legal

### Términos de uso

Al usar UFV Share aceptas:
- Ser estudiante, profesor o personal UFV
- Usar tu email institucional real
- No publicar contenido ilegal o inapropiado
- Respetar los precios de mercado (no especulación)
- Entregar productos en el estado descrito
- Resolver disputas de forma amigable

### Privacidad de datos

UFV Share protege tu información:
- Solo recopilamos datos necesarios para el servicio
- Tu email es visible solo para usuarios registrados
- No vendemos ni compartimos datos con terceros
- Cumplimos con RGPD (Reglamento Europeo)
- Puedes solicitar exportación o eliminación de datos

### Contenido prohibido

No está permitido publicar:
- ❌ Armas o material peligroso
- ❌ Drogas o sustancias ilegales
- ❌ Productos falsificados o plagiados
- ❌ Material protegido por derechos de autor (sin permiso)
- ❌ Contenido discriminatorio u ofensivo
- ❌ Servicios ilegales o inmorales

### Responsabilidad

- UFV Share es un intermediario, no parte de las transacciones
- Los usuarios son responsables de sus publicaciones
- Verificamos identidad pero no garantizamos honestidad
- Recomendamos encuentros en lugares públicos del campus
- No nos hacemos responsables de disputas entre usuarios

---

## 🎓 Acerca de UFV Share

### Nuestro equipo

**UFV Share** es un proyecto desarrollado por estudiantes de 3º de Ingeniería Informática UFV como parte de la asignatura **Proyectos II**.

**Equipo de desarrollo:**
- 👨‍💼 **Scrum Master:** Mario (Team Leader)
- 💻 **Backend Developer:** [Nombre]
- 🎨 **Frontend Developer:** [Nombre]
- 🧪 **QA Tester:** [Nombre]
- ♿ **Accessibility Specialist:** [Nombre]

**Metodología:**
- 🔄 SCRUM con sprints de 2 semanas
- 🌿 Git Flow para control de versiones
- 🤖 CI/CD con Azure DevOps
- ✅ TDD (Test Driven Development)
- ♿ WCAG AA para accesibilidad

### Tecnologías utilizadas

**Frontend:**
- ⚡ Astro 5.17.1 (Framework)
- ⚛️ React (Componentes interactivos)
- 🎨 Tailwind CSS (Estilos)
- 📦 Nanostores (Estado global)

**Backend:**
- ☕ Spring Boot 3.3.6 (Framework)
- 🔐 Spring Security (Autenticación)
- 🗄️ H2 Database (Desarrollo)
- 🐬 MySQL (Producción)

**DevOps:**
- 🔀 GitHub (Repositorio)
- 🚀 Azure App Service (Hosting)
- 🔄 GitHub Actions (CI/CD)
- 📊 Azure DevOps (Gestión de proyecto)

### Versión del manual

- **Versión actual:** 1.0
- **Última actualización:** 27 de febrero de 2026
- **Estado del proyecto:** Sprint 1 completado
- **Funcionalidades:** Autenticación, catálogo, publicaciones, favoritos

---

## 📝 Historial de cambios

### v1.0 - Sprint 1 (Febrero 2026)
✅ **Funcionalidades implementadas:**
- Sistema de registro e inicio de sesión
- Catálogo de productos con 50 items de prueba
- Filtrado por tipo (venta/alquiler)
- Publicación de artículos
- Sistema de favoritos
- Perfiles de usuario básicos
- Base de datos H2 con persistencia

🔄 **Pendiente para Sprint 2:**
- Mensajería interna entre usuarios
- Pasarelas de pago (Stripe, PayPal, Bizum)
- Sistema de notificaciones
- Estadísticas de publicaciones
- Búsqueda avanzada

---

## 🙏 Agradecimientos

Queremos agradecer a:
- **Universidad Francisco de Vitoria** por el apoyo al proyecto
- **Profesores de Proyectos II** por la guía y mentoría
- **Estudiantes beta testers** por su feedback invaluable
- **Comunidad UFV** por inspirar esta plataforma

---

## 📞 Contacto final

**¿Necesitas ayuda con algo específico?**

- 📧 Soporte técnico: `soporte@ufvshares.es`
- 💡 Sugerencias: `feedback@ufvshares.es`
- 🐛 Reportar bugs: GitHub Issues
- 📱 Redes sociales: [En desarrollo]

---

<div align="center">

**📚 UFV Share - Compartiendo conocimiento, construyendo comunidad**

Hecho con ❤️ por estudiantes UFV

_© 2026 UFV Share - Todos los derechos reservados_

</div>
