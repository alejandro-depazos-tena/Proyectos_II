# Cambios a desarrollar

## Objetivo

Este documento recoge una propuesta realista para llevar **UFVShares** a una versión más sólida, profesional y lista para despliegue, **después de revisar el código actual** de frontend y backend.

> **Nota importante:** este roadmap ya no parte de cero. Varias funcionalidades que inicialmente parecían pendientes **ya están implementadas** total o parcialmente, así que aquí se prioriza lo que realmente falta, lo que está a medias y lo que conviene profesionalizar.

> **Nota sobre estimaciones:** las horas son orientativas para una persona con conocimiento del proyecto. No incluyen reuniones, cambios de alcance ni validación externa.

---

## 0. Funcionalidades ya desarrolladas detectadas

Tras revisar la aplicación, estas piezas **ya existen** y no deben contarse como trabajo “desde cero”:

| Funcionalidad | Estado actual | Observación |
|---|---|---|
| Login y registro | Implementado | Existe `AuthController`, `AuthService` y página `auth.astro` / `login.astro` |
| Sesiones persistentes | Implementado | Las sesiones se guardan en base de datos mediante `SessionRepository` |
| Perfil de usuario | Implementado | `/api/me`, carga de perfil, cambio de foto y edición con confirmación por email |
| Publicación de productos | Implementado | Alta, edición, borrado y subida de fotos en `/api/me/productos` |
| Edición avanzada de producto | Implementado | Gestión de fotos, lightbox y cambios de estado en `product/edit.astro` |
| Chat entre usuarios | Implementado | Conversaciones, mensajes, buscador, emojis, ubicación y modal de usuario |
| Contacto desde producto | Implementado | Desde `product/view.astro` ya se abre o crea conversación |
| Modelo de contacto por chat | Decidido | El producto se orienta a que los usuarios negocien y cierren por chat, no mediante carrito/checkout |
| Favoritos | Implementado, pero local | Funcionan en frontend con `persistentAtom`, no en backend |
| Google Books API | Implementado | Ya se usa en `profile.astro` al publicar libros |
| Reportes de producto/usuario | Implementado en backend | Hay controladores y servicios; falta verificar flujo visual completo |
| Solicitudes y transacciones | Implementado en backend | Existen controladores y lógica, pero la UX final parece incompleta |

### Conclusión rápida de la auditoría

La app **ya tiene más trabajo hecho del que parecía**. El grueso pendiente no es “crear todo”, sino:

- cerrar huecos funcionales reales,
- conectar mejor frontend y backend en ciertos flujos basados en chat y solicitudes,
- pulir UX,
- y profesionalizar seguridad, despliegue y persistencia.

---

## 1. Cambios para terminar la aplicación

Estas son las piezas que conviene cerrar para considerar la aplicación como realmente terminada a nivel funcional:

| Cambio | Descripción realista | Prioridad | Horas estimadas |
|---|---|---:|---:|
| **Recuperar contraseña** | No se ha detectado flujo de “has olvidado la contraseña” ni endpoints de reseteo | Muy alta | 12-18 h |
| **Cerrar flujo solicitud → aceptación → transacción** | El backend existe, pero falta asegurar una UX completa y visible en frontend | Muy alta | 14-22 h |
| **Eliminar o archivar definitivamente checkout/carrito** | Dejar documentado y limpio que el cierre entre usuarios se hace por chat, evitando mantener un flujo que ya no forma parte del producto | Media | 2-4 h |
| **Frontend de reportes** | El backend de reportes existe, pero falta validar o construir una interfaz clara para denunciar producto/usuario | Alta | 8-14 h |
| **Búsqueda global de productos** | Hay filtros y buscador en chats, pero no un buscador global fuerte en catálogo | Alta | 10-16 h |
| **Gestión de sesión más completa** | Falta revisar logout visible, expiración, invalidación de sesión y redirecciones limpias | Media | 6-10 h |
| **Refactorización técnica de código** | Limpiar duplicaciones, separar mejor lógica, modularizar scripts largos y mejorar mantenibilidad | Alta | 16-28 h |
| **Plan de testing de aplicación** | Definir y cubrir pruebas unitarias, integración y validación de flujos críticos | Alta | 18-30 h |
| **Accesibilidad y navegación asistida** | Adaptar la app a criterios W3C/WCAG: contraste, foco, teclado, labels, semántica y lectura asistida | Muy alta | 16-26 h |
| **Gestión global de errores y cargas** | Hay partes cuidadas, pero aún conviene unificar errores, loaders y estados vacíos | Alta | 8-12 h |
| **Responsive y revisión de navegación** | Varias vistas están bien trabajadas, pero conviene una pasada completa móvil/tablet | Alta | 8-14 h |

**Total estimado bloque:** **126-188 horas**

---

## 2. Cambios menores (minor patch)

Aquí van mejoras pequeñas o medianas que aumentan mucho la sensación de calidad. En este bloque ya están corregidas las cosas que **sí existen** actualmente.

| Cambio menor | Qué habría que hacer realmente | Horas estimadas |
|---|---|---:|
| **Pulir la lupita de los chats** | El buscador de chats ya existe; toca mejorar comportamiento, feedback, vacío y quizá búsqueda dentro del chat activo | 2-5 h |
| **Revisar los 3 botones de acción** | Ajustar comportamiento, consistencia visual, hover, disabled y accesibilidad donde estén dando problemas | 3-5 h |
| Corregir bugs visuales pequeños | Espaciados, solapes, botones cortados, imágenes deformadas, tarjetas descuadradas | 5-8 h |
| Corregir bugs funcionales pequeños | Refresh de estados, errores de validación, algún edge case en edición/chat/productos | 6-10 h |
| Revisar favoritos multi-vista | Los favoritos ya funcionan, pero conviene revisar coherencia entre páginas y recargas | 3-5 h |
| Skeleton loaders y estados de carga | Ya hay algunos, pero no en toda la app de forma consistente | 4-6 h |
| Empty states más cuidados | Favoritos vacíos, sin resultados, sin publicaciones, sin transacciones, etc. | 3-5 h |
| Toasts y feedback coherentes | Ya hay toasts, pero conviene unificar tono, colores y mensajes | 2-4 h |
| Mejorar navegación por teclado | Tab order lógico, escape en modales, enter/space en acciones clave y atajos razonables | 4-7 h |
| Mejorar accesibilidad básica | Focus visible, labels, navegación por teclado, contraste | 5-8 h |
| Revisar textos y microcopy | Mensajes más claros y profesionales en acciones clave | 3-5 h |

**Total estimado bloque:** **40-68 horas**

---

## 3. Cambios más importantes

Aquí van los cambios de más impacto, teniendo en cuenta lo que **todavía no está** o está solo parcialmente resuelto.

| Cambio importante | Alcance | Prioridad | Horas estimadas |
|---|---|---:|---:|
| **Has olvidado la contraseña** | Solicitud por email, token temporal, nueva contraseña y expiración segura | Muy alta | 12-18 h |
| **Sistema de notificaciones real** | Centro de notificaciones dentro de la app + base para eventos relevantes | Muy alta | 16-24 h |
| **Panel de administración básico** | Moderación de reportes, productos y usuarios desde interfaz interna | Alta | 18-28 h |
| **Reputación / valoraciones** | Valorar usuarios tras transacción o trato completado | Alta | 10-16 h |
| **Favoritos en backend** | Pasar de favoritos solo locales a favoritos asociados al usuario y sincronizados entre dispositivos | Media | 10-16 h |
| **Historial de actividad** | Ver solicitudes, conversaciones recientes, publicaciones y movimientos relevantes | Media | 8-14 h |
| **Búsqueda global potente** | Buscador unificado con texto, filtros y ordenaciones útiles | Alta | 12-18 h |
| **Cobertura de tests automatizados** | Consolidar suite de pruebas para frontend y backend con foco en regresiones | Alta | 20-32 h |
| **Refactorización estructural por módulos** | Reorganizar componentes, servicios y scripts extensos para reducir deuda técnica | Alta | 18-30 h |

### APIs novedosas que pueden aportar valor

> **Importante:** la **Google Books API ya está integrada**, así que no tiene sentido ponerla como futura novedad principal.

#### Opción A: API de correo transaccional
Ejemplos: **Resend**, **SendGrid** o **Azure Communication Services**.

**Qué aportaría:**
- Recuperación de contraseña.
- Correos de confirmación más fiables.
- Avisos de aceptación/rechazo de solicitudes.

**Estimación:** **8-14 h**

#### Opción B: API de notificaciones push
Ejemplos: **Firebase Cloud Messaging** o **OneSignal**.

**Qué aportaría:**
- Aviso cuando te escriben.
- Aviso cuando aceptan una solicitud.
- Aviso cuando cambia el estado de una transacción.

**Estimación:** **14-24 h**

#### Opción C: Motor de búsqueda externo
Ejemplos: **Meilisearch**, **Algolia** o búsqueda propia mejorada.

**Qué aportaría:**
- Búsqueda rápida y relevante.
- Mejor experiencia de descubrimiento.
- Filtros y ordenaciones más útiles.

**Estimación:** **12-22 h**

**Total estimado bloque principal:** **124-202 horas**

---

## 3.1 Refactorización del código

La aplicación ya tiene bastante funcionalidad implementada, y precisamente por eso ahora empieza a ser importante **refactorizar** para que el proyecto siga siendo mantenible.

### Objetivos de refactorización

- Reducir scripts demasiado largos en páginas como perfil, chat o vista de producto.
- Separar lógica de UI, lógica de negocio y consumo de API.
- Evitar duplicaciones entre componentes o páginas.
- Estandarizar nombres, estructuras y manejo de estados.
- Hacer el código más fácil de testear y evolucionar.

| Tarea de refactorización | Qué implica | Horas estimadas |
|---|---|---:|
| Extraer utilidades compartidas | Formateo, manejo de tokens, helpers de fetch, errores, fechas, etc. | 6-10 h |
| Dividir scripts grandes de páginas | Mover lógica de `profile`, `chats`, `product/view` a módulos más pequeños | 10-16 h |
| Reorganizar servicios frontend | Centralizar acceso a API en vez de tener `fetch` disperso | 8-14 h |
| Homogeneizar tipos y contratos | Estructuras comunes para producto, usuario, chat, favoritos | 6-10 h |
| Revisar backend por capas | Afinar separación controlador/servicio/repositorio y validaciones | 8-14 h |

**Total estimado refactorización:** **38-64 horas**

### Beneficio esperado

- Menos deuda técnica.
- Menos bugs al tocar nuevas funcionalidades.
- Mejor base para testing y despliegue.
- Proyecto más profesional de cara a revisión o mantenimiento futuro.

---

## 3.2 Testing de la aplicación

Ahora mismo el proyecto necesita una estrategia de testing más explícita para evitar regresiones cuando sigáis iterando.

### Qué conviene cubrir

#### Backend
- Tests unitarios de servicios.
- Tests de controladores principales.
- Validación de auth, sesiones, productos, chat, solicitudes y transacciones.

#### Frontend
- Tests de componentes críticos.
- Validación de formularios.
- Comprobación de estados principales.
- Pruebas de flujos importantes.

#### End-to-end
- Registro / login.
- Publicar producto.
- Editar producto.
- Abrir chat desde producto.
- Enviar mensajes.
- Solicitudes / aceptación / cambio de estado.

| Tipo de test | Alcance | Horas estimadas |
|---|---|---:|
| Tests unitarios backend | Servicios, validaciones y reglas de negocio | 10-16 h |
| Tests de integración backend | Endpoints principales con base de datos de prueba | 10-18 h |
| Tests de componentes frontend | Componentes con lógica o interacción relevante | 8-14 h |
| Tests E2E | Flujos críticos del producto | 14-24 h |
| Revisión de cobertura y regresión | Detectar zonas sin proteger y estabilizar la suite | 6-10 h |

**Total estimado testing:** **48-82 horas**

### Herramientas recomendables

- **Backend:** JUnit + Spring Boot Test + MockMvc.
- **Frontend:** Vitest + Testing Library.
- **End-to-end:** Playwright.

### Beneficio esperado

- Más seguridad al cambiar código.
- Menos regresiones ocultas.
- Mejor calidad para despliegue y presentación.

---

## 3.3 Accesibilidad, navegación asistida y criterios W3C

Para que la aplicación sea profesional de verdad, no basta con que “se vea bien”: también debe ser **usable**, **legible** y **accesible**.

Aquí conviene alinear la app con criterios **W3C / WCAG** en lo importante.

### Qué revisar

#### Contraste de color
- Verificar contraste de texto/fondo.
- Revisar botones, badges, placeholders y estados disabled.
- Evitar combinaciones bonitas pero poco legibles.

#### Navegación por teclado
- Poder recorrer formularios, tarjetas y acciones con `Tab`.
- Activar botones con teclado.
- Cerrar modales con `Escape`.
- Mantener foco visible y orden lógico.

#### Semántica HTML y lectura asistida
- Usar bien encabezados `h1-h2-h3`.
- Labels asociados a inputs.
- `aria-label`, `aria-live`, `aria-modal`, `role` donde corresponda.
- Textos alternativos y feedback entendible por lectores de pantalla.

#### Formularios accesibles
- Errores comprensibles.
- Relación clara entre campo y mensaje.
- Placeholders no sustitutivos de labels.

#### Interacción y foco
- No perder foco en drawers, modales o lightboxes.
- Evitar trampas de teclado.
- Mantener comportamiento predecible.

| Mejora de accesibilidad | Alcance | Horas estimadas |
|---|---|---:|
| Auditoría W3C/WCAG inicial | Detectar incumplimientos principales | 4-6 h |
| Revisión de contraste | Botones, tipografía, badges, formularios y estados | 4-8 h |
| Navegación por teclado | Tabulación, foco, escape, botones y modales | 6-10 h |
| Etiquetas y semántica | Labels, headings, aria, landmarks | 6-10 h |
| Ajustes en formularios y mensajes | Validaciones accesibles y feedback legible | 5-8 h |
| Prueba manual con accesibilidad | Revisión final de experiencia real | 4-6 h |

**Total estimado accesibilidad:** **29-48 horas**

### Beneficio esperado

- Aplicación más profesional y usable.
- Mejor experiencia para más tipos de usuarios.
- Mejor cumplimiento de estándares y mejor percepción general de calidad.

---

## 4. Ideas y proposiciones

Estas mejoras no siempre son imprescindibles para cerrar versión, pero sí muy recomendables para que el producto destaque de verdad.

| Idea | Valor aportado | Horas estimadas |
|---|---|---:|
| Sistema de diseño unificado | Hace que toda la app se vea más profesional y consistente | 12-20 h |
| Página de producto aún más redonda | Mejor confianza, más información útil y CTA más claros | 6-10 h |
| Mejor onboarding | Explicar mejor el valor de la app desde home y auth | 6-10 h |
| SEO técnico y metadatos | Mejor compartir enlaces y mejor indexación | 8-14 h |
| Métricas básicas de uso | Saber qué se usa más y dónde se cae la gente | 5-8 h |
| Moderación visible | Dar confianza y sensación de plataforma cuidada | 6-10 h |
| Perfil profesional del vendedor | Antigüedad, actividad, reputación y datos públicos útiles | 8-14 h |
| Mejor analítica del chat/transacción | Ver dónde se quedan las operaciones a medias | 5-8 h |
| Accesibilidad cuidada | Mejor usabilidad, estándares y calidad percibida | 10-18 h |
| Suite de testing estable | Confianza para iterar sin romper funcionalidades | 12-20 h |
| Refactorización continua | Hace que el proyecto escale mejor sin volverse frágil | 10-18 h |

### Cambios fundamentales para que la app sea profesional y buena

Si hubiera que escoger solo lo más importante para que la aplicación se sienta realmente profesional, yo priorizaría esto:

1. **Endurecer seguridad de autenticación**
   - Sustituir el hash actual por **BCrypt** o **Argon2** en vez de SHA-256 directo.
   - Añadir reset de contraseña y expiración/invalidez de sesión.
   - **Estimación:** 14-24 h

2. **Cerrar bien el flujo de negocio real**
   - Solicitud, aceptación, transacción, cancelación y completado visibles en frontend.
   - Que no quede una parte potente solo en backend.
   - **Estimación:** 16-26 h

3. **Consistencia visual total**
   - Botones, inputs, badges, tarjetas, modales y estados con un mismo sistema.
   - **Estimación:** 12-20 h

4. **Accesibilidad y criterios W3C bien resueltos**
   - Contrastes correctos.
   - Navegación por teclado.
   - Semántica y formularios accesibles.
   - **Estimación:** 16-28 h

5. **Responsive real en móvil**
   - No solo “se adapta”, sino que es cómodo de usar.
   - **Estimación:** 8-12 h

6. **Chat y notificaciones muy fiables**
   - El chat ya existe y está bastante bien; lo profesional es hacerlo todavía más robusto y acompañarlo de notificaciones reales.
   - **Estimación:** 18-30 h

7. **Persistencia seria de favoritos y actividad**
   - Ahora los favoritos viven en localStorage; para una app más profesional deberían sincronizarse por usuario.
   - **Estimación:** 10-16 h

8. **Testing y refactorización continuos**
   - Sin pruebas y sin limpieza interna, el proyecto crecerá con más riesgo y más deuda técnica.
   - **Estimación:** 24-40 h

9. **Despliegue estable + base de datos seria + almacenamiento externo**
   - Para dejar de depender de entorno local y de `/uploads` en disco.
   - **Estimación:** 16-28 h

---

## 5. Migrar la base de datos y despliegue

La base funcional ya existe, así que aquí el trabajo no es “crear”, sino **migrar bien** y profesionalizar.

### Opción gratuita o de coste muy bajo

#### Base de datos
La opción más práctica sería:
- **PostgreSQL gratuito** en servicios como:
  - Supabase
  - Neon
  - Render

#### Cómo lo haría
1. Sustituir la configuración local por variables de entorno.
2. Migrar de la base actual a PostgreSQL.
3. Añadir **Flyway** para versionar esquema y datos base.
4. Verificar entidades de usuario, sesión, producto, foto, chat, solicitud y transacción.
5. Revisar compatibilidad de tipos y restricciones.

#### Despliegue
- **Frontend**: Vercel o Netlify.
- **Backend**: Render, Railway o Azure App Service si hay plan estudiante.

#### Ventajas
- Montaje rápido.
- Muy válido para portfolio, TFG o piloto.
- Menor coste.

#### Inconvenientes
- Recursos limitados.
- Posibles pausas en free tier.
- Menor robustez para producción real.

#### Horas estimadas
| Tarea | Horas |
|---|---:|
| Preparar PostgreSQL y credenciales | 2-4 h |
| Adaptar backend y configuración | 6-10 h |
| Crear migraciones iniciales con Flyway | 5-8 h |
| Revisar carga de seed y compatibilidad | 3-5 h |
| Desplegar frontend | 2-4 h |
| Desplegar backend | 4-8 h |
| Validación end-to-end | 4-6 h |

**Total opción gratuita:** **26-45 horas**

---

### Opción con Azure

Si queréis una solución más seria y enseñable, Azure encaja mejor.

#### Arquitectura recomendada en Azure
- **Frontend**: Azure Static Web Apps
- **Backend**: Azure App Service
- **Base de datos**: Azure Database for PostgreSQL
- **Archivos/imágenes**: Azure Blob Storage
- **Configuración/secretos**: App Settings o Azure Key Vault

#### Cómo lo haría
1. Preparar entornos `dev` y `prod`.
2. Migrar backend a PostgreSQL con Flyway.
3. Mover imágenes de `/uploads` a Blob Storage.
4. Desplegar frontend en Static Web Apps.
5. Desplegar backend en App Service.
6. Configurar CORS, variables y logs.
7. Validar login, perfil, productos, chat, solicitudes y transacciones.

#### Ventajas
- Más profesional.
- Mejor observabilidad.
- Mejor historia técnica para portfolio/defensa.

#### Inconvenientes
- Más configuración.
- Puede tener coste si no hay créditos.

#### Horas estimadas
| Tarea | Horas |
|---|---:|
| Diseño de arquitectura de despliegue | 3-5 h |
| Configuración PostgreSQL en Azure | 3-6 h |
| Migración de datos y entidades | 6-10 h |
| Flyway + scripts | 5-8 h |
| Configuración backend App Service | 4-8 h |
| Configuración frontend Static Web Apps | 3-5 h |
| Migración de uploads a Blob Storage | 8-14 h |
| Variables, CORS, secretos y logs | 4-6 h |
| Pruebas y corrección de incidencias | 6-10 h |

**Total opción Azure:** **42-72 horas**

---

## 6. Recomendación práctica de ejecución

### Fase 1 — Ajustar lo que ya existe
- bugs menores
- pulir buscador de chats ya implementado
- revisar los 3 botones problemáticos
- responsive
- errores visuales/funcionales
- loaders, vacíos y feedback

**Estimación:** **24-40 h**

### Fase 2 — Cerrar huecos funcionales reales
- recuperar contraseña
- flujo real de solicitudes/transacciones en frontend
- revisar logout/expiración de sesión
- frontend de reportes
- búsqueda global
- base mínima de testing en flujos críticos

**Estimación:** **50-80 h**

### Fase 3 — Profesionalización
- notificaciones
- favoritos en backend
- sistema de diseño
- reputación
- SEO y analítica básica
- endurecimiento de autenticación
- accesibilidad W3C / WCAG
- refactorización estructural

**Estimación:** **70-110 h**

### Fase 4 — Infraestructura
- migración de base de datos
- despliegue
- almacenamiento de imágenes
- variables seguras y logs
- ampliación de testing y regresión automática

**Estimación:** **38-82 h**

---

## 7. Resumen ejecutivo

Después de revisar la aplicación, el orden recomendado cambia respecto al documento anterior:

1. **No rehacer lo que ya existe**: auth, perfil, productos, chat y Google Books ya tienen bastante trabajo hecho.
2. **Cerrar huecos reales**: recuperar contraseña, solicitudes/transacciones visibles, reportes y búsqueda global.
3. **Profesionalizar seguridad, testing y accesibilidad**: contraseñas, sesiones, favoritos, navegación asistida y calidad técnica.
4. **Desplegar con una base de datos y almacenamiento más serios**.

### Estimación global aproximada

| Escenario | Horas estimadas |
|---|---:|
| Mejora mínima seria sobre lo actual | 70-110 h |
| Versión muy buena para presentación/portfolio | 140-220 h |
| Versión muy sólida y profesional | 220-320 h |

---

## 8. Siguiente paso recomendado

Convertir este documento en un backlog real con:

- prioridad `P1`, `P2`, `P3`,
- reparto entre **frontend**, **backend** e **infraestructura**,
- dependencias,
- y planificación por sprint.

Así el desarrollo se apoyará en el estado real del proyecto, no en supuestos.
